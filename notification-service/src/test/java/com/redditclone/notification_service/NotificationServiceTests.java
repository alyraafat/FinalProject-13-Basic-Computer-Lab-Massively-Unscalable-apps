//package com.example.miniapp;
//
//import com.example.miniapp.models.dto.NotificationRequest;
//import com.example.miniapp.models.dto.NotificationResponse;
//import com.example.miniapp.models.entity.Notification;
//import com.example.miniapp.models.entity.UserNotification;
//import com.example.miniapp.models.entity.UserPreference;
//import com.example.miniapp.models.enums.NotificationType;
//import com.example.miniapp.repositories.NotificationRepository;
//import com.example.miniapp.repositories.PreferenceRepository;
//import com.example.miniapp.repositories.UserNotifyRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = {
//        // Clean up tables in proper order
//        "DELETE FROM user_notifications;",
//        "DELETE FROM notifications;",
//        "DELETE FROM user_preferences;",
//
//        // Create test data if needed
//        "INSERT INTO user_preferences (user_id, email_notifications, push_notifications, user_email) " +
//                "VALUES ('test-user-123', false, true, 'test@example.com');"
//})
//@Transactional
//class NotificationServiceTests {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private UserNotifyRepository userNotifyRepository;
//
//    @Autowired
//    private PreferenceRepository preferenceRepository;
//
//    private String BASE_URL;
//
//    @BeforeEach
//    public void setup() {
//        BASE_URL = "http://localhost:" + port + "/notifications";
//    }
//
//    @Test
//    public void testCreateUserSpecificNotification() {
//        // Create notification request
//        NotificationRequest request = new NotificationRequest(
//                "Test user-specific notification",
//                List.of(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
//                UUID.fromString("223e4567-e89b-12d3-a456-426614174001"), // senderId
//                "user"
//        );
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<NotificationRequest> httpRequest = new HttpEntity<>(request, headers);
//
//        // Call endpoint
//        ResponseEntity<NotificationResponse> response = testRestTemplate.postForEntity(
//                BASE_URL,
//                httpRequest,
//                NotificationResponse.class
//        );
//
//        // Verify response
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        // Verify notification was created
//        List<Notification> notifications = notificationRepository.findAll();
//        assertEquals(1, notifications.size());
//        assertEquals("USER_SPECIFIC", notifications.get(0).getType());
//
//        // Verify user notification was created
//        List<UserNotification> userNotifications = userNotifyRepository.findAll();
//        assertEquals(1, userNotifications.size());
//        assertEquals("test-user-123", userNotifications.get(0).getUserId().toString());
//    }
//}