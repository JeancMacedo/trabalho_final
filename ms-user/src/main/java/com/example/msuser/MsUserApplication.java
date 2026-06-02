package com.example.msuser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.msuser.model.Role;
import com.example.msuser.repository.RoleRepository;

@SpringBootApplication
public class MsUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsUserApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(null, "ROLE_CUSTOMER"));
                roleRepository.save(new Role(null, "ROLE_ADMIN"));
            }
        };
    }
}
