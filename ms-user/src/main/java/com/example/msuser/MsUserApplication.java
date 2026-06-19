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
            if (roleRepository.findByName("ROLE_CUSTOMER").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_CUSTOMER"));
            }
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_ADMIN"));
            }
            if (roleRepository.findByName("ROLE_ADMINISTRATOR").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_ADMINISTRATOR"));
            }
        };
    }
}
