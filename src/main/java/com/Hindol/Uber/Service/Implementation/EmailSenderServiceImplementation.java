package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.Service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImplementation implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);
            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully");
        }
        catch (MessagingException e) {
            log.info("Cannot send Email - {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmail(String[] toEmail, String subject, String body) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
            log.info("Email sent successfully");
        }
        catch (Exception e) {
            log.info("Cannot send Email - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
