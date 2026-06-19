package com.example.msuser.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.msuser.dto.UpdateProfileDto;
import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.model.Role;
import com.example.msuser.model.User;
import com.example.msuser.repository.RoleRepository;
import com.example.msuser.repository.UserRepository;
import com.example.msuser.security.JwtTokenUtil;
import com.example.msuser.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtUtils;
    @Autowired
    UserService userService;

    record CreateUserRequest(String username, String password, String role) {}
    record LoginRequest(String username, String password) {}
    record JwtResponse(String token) {}

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User u = new User(req.username(), passwordEncoder.encode(req.password()));
        Set<Role> roles = new HashSet<>();
        roleRepository.findByName(req.role() == null ? "ROLE_CUSTOMER" : req.role())
                .ifPresent(roles::add);
        u.setRoles(roles);
        userRepository.save(u);
        return ResponseEntity.ok(u);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        String token = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/update-profile")
    public ResponseEntity<UserProfileDto> updateProfile(Authentication authentication,
                                                        @RequestBody UpdateProfileDto dto) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> me(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/test/customer")
    public ResponseEntity<?> testCustomer() {
        return ResponseEntity.ok("OK - customer access");
    }
}
