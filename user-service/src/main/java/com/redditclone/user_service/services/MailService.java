package com.redditclone.user_service.services;



import com.redditclone.user_service.dtos.NotificationEmail;
import com.redditclone.user_service.exceptions.RedditAppException;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessage.setSender(new InternetAddress(sender));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(notificationEmail.getRecipient()));
            mimeMessage.setSubject(notificationEmail.getSubject());
            messageHelper.setText(build(notificationEmail.getBody()), true);
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Sent Activation email to {}", notificationEmail.getRecipient());
        }
        catch (MailException e){
            throw new RedditAppException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
        }

    }
}
