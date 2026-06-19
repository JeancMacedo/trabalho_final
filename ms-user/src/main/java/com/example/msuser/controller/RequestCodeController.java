package com.example.msuser.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.msuser.dto.EmailRecordDto;
import com.example.msuser.mq.UserProducer;
import com.example.msuser.model.Role;
import com.example.msuser.model.User;
import com.example.msuser.repository.RoleRepository;
import com.example.msuser.repository.UserRepository;
import com.example.msuser.service.CodigoCacheService;
import com.example.msuser.security.JwtTokenUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/auth")
public class RequestCodeController {
    @Autowired
    private CodigoCacheService codigoCacheService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserProducer userProducer;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    record EmailRequest(String email) {}
    record VerifyCodeRequest(String email, String code) {}
    record JwtResponse(String token) {}

    @PostMapping("/request-code")
    public ResponseEntity<?> requestCode(@RequestBody EmailRequest req) {
        String email = req.email().toLowerCase().trim();
        String code = codigoCacheService.generateCode(email);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            String username = "temp_" + UUID.randomUUID().toString().substring(0, 8);
            String rawPass = UUID.randomUUID().toString().substring(0, 8);
            user = new User(username, passwordEncoder.encode(rawPass));
            user.setEmail(email);
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("ROLE_CUSTOMER").ifPresent(roles::add);
            user.setRoles(roles);
            user = userRepository.save(user);
        }

        EmailRecordDto dto = new EmailRecordDto(user.getId(), email, "Seu código de acesso", "Seu código é: " + code);
        userProducer.sendEmail(dto);
        return ResponseEntity.ok().body("Código enviado");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest req) {
        String email = req.email().toLowerCase().trim();
        String code = req.code().trim();

        if (!codigoCacheService.consumeCode(email, code)) {
            return ResponseEntity.badRequest().body("Código inválido ou expirado");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        UserDetails principal = new org.springframework.security.core.userdetails.User(
            user.getEmail() != null ? user.getEmail() : user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        String token = jwtTokenUtil.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
