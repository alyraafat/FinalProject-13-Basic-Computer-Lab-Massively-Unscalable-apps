package com.redditclone.notification_service.services.strategy.impl;

import com.redditclone.notification_service.clients.UserClient;
import com.redditclone.notification_service.models.entity.Notification;
import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.models.entity.UserPreference;
import com.redditclone.notification_service.repositories.PreferenceRepository;
import com.redditclone.notification_service.services.strategy.DeliveryStrategy;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component("emailStrategy")
public class EmailStrategy implements DeliveryStrategy {

    private final PreferenceRepository preferenceRepository;

    private final UserClient userClient;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;

    public EmailStrategy(
            PreferenceRepository preferenceRepository,
            UserClient userClient
    ) {
        this.preferenceRepository = preferenceRepository;
        this.userClient = userClient;
    }


    @Override
    public void deliver(UserNotification userNotification) {
        try {
            UUID userId = userNotification.getUserId();
            Notification notification = userNotification.getNotification();
            // Fetch user preferences
            UserPreference userPreference = preferenceRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User preference not found for userId: " + userId));

            String email = userPreference.getUserEmail();

            // Check if the email is not empty
            if (email != null && !email.isEmpty()) {
                // Use the email from preferences
                System.out.println("Using email from preferences: " + email);
            } else {
                // Fetch the email using the UserClient if preference email is empty
                List<String> emails = userClient.getEmailsByIds(List.of(userId));
                if (emails != null && !emails.isEmpty()) {
                    email = emails.getFirst(); // Assuming you get the email from the list
                    userPreference.setUserEmail(email);
                    preferenceRepository.save(userPreference); // Save the email in preferences
                    System.out.println("Using email from UserClient: " + email);
                } else {
                    throw new RuntimeException("Email not found for userId: " + userId);
                }
            }

            // Proceed with delivering the email
            System.out.println("Delivering mail");
            String emailContent = formatEmail(userNotification);
            boolean delivered = sendEmail(email, emailContent);

            if (!delivered) {
                System.err.println("Email delivery failed for notification: " + notification.getId());
            } else {
                System.out.println("Email delivered for notification: " + notification.getId());
            }

        } catch (Exception e) {
            System.err.println("Email delivery error: " + e.getMessage());
            throw new RuntimeException("Email delivery failed", e);
        }
    }

    private String formatEmail(UserNotification userNotification) {
        return String.format(
                "Subject: Notification - %s\n" +
                        "Body: %s\n" +
                        "Timestamp: %s\n" +
                        "Type: %s",
                userNotification.getNotification().getType(),
                userNotification.getDeliveredMessage(),
                userNotification.getNotification().getCreatedAt(),
                userNotification.getNotification().getType()
        );
    }

    private boolean sendEmail(String toEmail, String emailContent) {
        try {
            // Create SendGrid email object
            Email from = new Email(senderEmail);
            Email to = new Email(toEmail);

            Content content = new Content("text/plain", emailContent);
            Mail mail = new Mail(from, "Notification", to, content);

            // Create a SendGrid API client
            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            // Send the email
            Response response = sendGrid.api(request);

            // Check if the email was sent successfully
            if (response.getStatusCode() != 202) {
                System.out.println("Failed to send email. Status Code: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
                return false;
            }

            System.out.println("[EMAIL] Successfully sent email.");
            return true;

        } catch (IOException e) {
            System.err.println("Failed to send email via SendGrid: " + e.getMessage());
            return false;
        }
    }
}
