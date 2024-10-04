package com.Hindol.Uber.Service;

public interface EmailSenderService {
    void sendEmail(String toEmail, String subject, String body);
    void sendEmail(String[] toEmail, String subject, String body);
}
