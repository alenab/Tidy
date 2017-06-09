package ca.tidygroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailingService {

    private static final Logger log = LoggerFactory.getLogger(MailingService.class);

    @Value("${spring.mail.username}")
    private String mailBoxUserEmail;

    @Value("${tidy.admin.email}")
    private String adminEmail;

    private JavaMailSender mailSender;

    @Autowired
    public MailingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(Object obj) {
        log.info("Going to send an email to admin");
        if (log.isDebugEnabled()) {
            log.debug("Using mailbox: {} and sending to: {}", mailBoxUserEmail, adminEmail);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailBoxUserEmail);
        message.setTo(adminEmail);
        message.setSubject("New booking!");
        message.setText("You have new booking on the website!\n\r" + obj);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Exception is thrown at attempt to send an email", e);
        }
    }
}
