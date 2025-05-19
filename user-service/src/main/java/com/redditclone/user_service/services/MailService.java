package com.redditclone.user_service.services;

import com.redditclone.user_service.dtos.NotificationEmail;
import com.redditclone.user_service.exceptions.RedditAppException;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final TemplateEngine templateEngine;

    @Value("${sendgrid.sender.email}")
    private String sender;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        String emailContent = build(notificationEmail.getBody());

        Email from = new Email(sender);
        Email to = new Email(notificationEmail.getRecipient());
        Content content = new Content("text/html", emailContent);
        Mail mail = new Mail(from, notificationEmail.getSubject(), to, content);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            if (response.getStatusCode() != 202) {
                log.error("Failed to send email. Status Code: {}, Response Body: {}", response.getStatusCode(), response.getBody());
                throw new RedditAppException("SendGrid failed to send email.");
            }

            log.info("Sent email to {}", notificationEmail.getRecipient());
        } catch (IOException e) {
            log.error("SendGrid email sending failed", e);
            throw new RedditAppException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
        }
    }
}