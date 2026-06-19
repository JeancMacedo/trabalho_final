package com.example.msuser.service;

import java.util.Comparator;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.msuser.dto.UpdateProfileDto;
import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.model.Role;
import com.example.msuser.model.User;
import com.example.msuser.repository.RoleRepository;
import com.example.msuser.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UserProfileDto updateProfile(String email, UpdateProfileDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        user.setName(normalize(dto.name()));
        user.getRoles().clear();
        user.getRoles().add(resolveRole(dto.role()));

        return toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        return toDto(user);
    }

    private Role resolveRole(String roleName) {
        String requestedRole = normalize(roleName);
        if (requestedRole == null) {
            requestedRole = "ROLE_CUSTOMER";
        }

        return roleRepository.findByName(requestedRole)
                .or(() -> roleRepository.findByName("ROLE_CUSTOMER"))
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_CUSTOMER not configured"));
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private UserProfileDto toDto(User user) {
        String role = user.getRoles().stream()
                .map(Role::getName)
                .max(Comparator.naturalOrder())
                .orElse(null);
        return new UserProfileDto(user.getId(), user.getName(), user.getEmail(), role);
    }
}