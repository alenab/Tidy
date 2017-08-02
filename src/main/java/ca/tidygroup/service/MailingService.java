package ca.tidygroup.service;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.dto.EmailMessage;
import ca.tidygroup.event.NewBookingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailingService {

    private static final Logger log = LoggerFactory.getLogger(MailingService.class);

    @Value("${tidy.email.from}")
    private String mailBoxUserEmail;

    @Value("${tidy.admin.email}")
    private String adminEmail;

    private JavaMailSender mailSender;

    @Autowired
    public MailingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void handleNewBookingEvent(NewBookingEvent event) {
        EmailMessage email = new EmailMessage();
        BookingForm form = event.getBookingForm();
        email.setEmail(form.getEmail());
        email.setName(String.format("%s %s", form.getFirstName(), form.getLastName()));
        email.setSubject("New Booking!");
        email.setMessage(form.toString());

        sendEmailMessage(email);
    }

    @Async
    public void sendEmailMessage(EmailMessage message) {
        log.info("Going to send a feedback to admin");
        if (log.isDebugEnabled()) {
            log.debug("Using mailbox: {} and sending to: {}", mailBoxUserEmail, adminEmail);
        }
        SimpleMailMessage mes = new SimpleMailMessage();
        mes.setFrom(mailBoxUserEmail);
        mes.setTo(adminEmail);
        mes.setSubject(message.getSubject());
        mes.setText("You have new message on the website from: \n\r" + message.getName() + "\n"
                + message.getEmail() + "\n\r" + message.getMessage());
        try {
            mailSender.send(mes);
            log.debug("Email sent successfully");
        } catch (MailException e) {
            log.error("Exception is thrown at attempt to send an email", e);
        }
    }
}
