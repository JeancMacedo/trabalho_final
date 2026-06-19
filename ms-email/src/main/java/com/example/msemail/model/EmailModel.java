package com.example.msemail.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_model")
public class EmailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    private Long userId;

    @Column(nullable = false)
    private String emailFrom;

    @Column(nullable = false)
    private String emailTo;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    private LocalDateTime sendDateEmail;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    public Long getEmailId() { return emailId; }
    public void setEmailId(Long emailId) { this.emailId = emailId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmailFrom() { return emailFrom; }
    public void setEmailFrom(String emailFrom) { this.emailFrom = emailFrom; }
    public String getEmailTo() { return emailTo; }
    public void setEmailTo(String emailTo) { this.emailTo = emailTo; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDateTime getSendDateEmail() { return sendDateEmail; }
    public void setSendDateEmail(LocalDateTime sendDateEmail) { this.sendDateEmail = sendDateEmail; }
    public EmailStatus getStatus() { return status; }
    public void setStatus(EmailStatus status) { this.status = status; }
}
