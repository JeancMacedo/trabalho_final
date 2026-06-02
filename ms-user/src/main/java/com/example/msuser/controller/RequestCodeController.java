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

import com.example.msuser.dto.EmailDTO;
import com.example.msuser.mq.UserProducer;
import com.example.msuser.model.Role;
import com.example.msuser.model.User;
import com.example.msuser.repository.RoleRepository;
import com.example.msuser.repository.UserRepository;
import com.example.msuser.service.CodigoCacheService;

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

    record EmailRequest(String email) {}

    @PostMapping("/request-code")
    public ResponseEntity<?> requestCode(@RequestBody EmailRequest req) {
        String email = req.email().toLowerCase().trim();
        String code = codigoCacheService.generateCode(email);

        Optional<User> uOpt = userRepository.findByEmail(email);
        if (uOpt.isEmpty()) {
            String username = "temp_" + UUID.randomUUID().toString().substring(0, 8);
            String rawPass = UUID.randomUUID().toString().substring(0, 8);
            User u = new User(username, passwordEncoder.encode(rawPass));
            u.setEmail(email);
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("ROLE_CUSTOMER").ifPresent(roles::add);
            u.setRoles(roles);
            userRepository.save(u);
        }

        EmailDTO dto = new EmailDTO(email, "Seu código de acesso", "Seu código é: " + code);
        userProducer.sendEmail(dto);
        return ResponseEntity.ok().body("Código enviado");
    }
}
