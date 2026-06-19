package com.example.msuser.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.msuser.repository.UserRepository;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
            .or(() -> userRepository.findByEmail(username))
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());

        String principalName = user.getEmail() != null ? user.getEmail() : user.getUsername();
        return new org.springframework.security.core.userdetails.User(principalName, user.getPassword(), authorities);
    }
}
