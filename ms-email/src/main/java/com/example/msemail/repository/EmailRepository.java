package com.example.msemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.msemail.model.EmailModel;

public interface EmailRepository extends JpaRepository<EmailModel, Long> {
}
