package com.example.msemail.dto;

public class EmailRecordDto {
    private Long userId;
    private String emailTo;
    private String subject;
    private String text;

    public EmailRecordDto() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmailTo() { return emailTo; }
    public void setEmailTo(String emailTo) { this.emailTo = emailTo; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
