package com.example.miniapp;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.dto.PreferenceUpdateRequest;
import com.example.miniapp.models.enums.NotificationPreference;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.models.entity.UserNotification;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.repositories.PreferenceRepository;
import com.example.miniapp.repositories.UserNotifyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NotificationControllerIntegrationTest {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:5.0.8");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserNotifyRepository userNotifyRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

    private UUID testUserId;
    private UUID testSenderId;
    private NotificationRequest baseReq;

    @BeforeEach
    void cleanup() {
        userNotifyRepository.deleteAll();
        notificationRepository.deleteAll();
        preferenceRepository.deleteAll();

        testUserId   = UUID.fromString("7ebaf65e-d346-460b-98fe-7ab870f10f66");
        testSenderId = UUID.randomUUID();
        baseReq = new NotificationRequest(
                "Hello!", List.of(testUserId), testSenderId, "Alice"
        );
        baseReq.setType(NotificationType.COMMUNITY);
        baseReq.setCreatedAt(Instant.now());
    }

    @Test
    void create_then_get_unread_then_mark_read_then_get_read() {
        // POST → create
        ResponseEntity<String> post = rest.postForEntity(
                "/api/notifications/", baseReq, String.class
        );
        assertThat(post.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(post.getBody()).isEqualTo("Sent!");

        // GET unread
        ResponseEntity<UserNotification[]> getUnread = rest.getForEntity(
                "/api/notifications/?userId={uid}", UserNotification[].class, testUserId
        );
        assertThat(getUnread.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUnread.getBody()).hasSize(1);
        String notificationId = getUnread.getBody()[0].getId();
        assertThat(getUnread.getBody()[0].getStatus()).isEqualTo("unread");

        // PUT → mark read
        HttpEntity<UUID> readReq = new HttpEntity<>(testUserId);
        ResponseEntity<String> readResp = rest.exchange(
                "/api/notifications/read?notificationId={nid}",
                HttpMethod.PUT, readReq, String.class, notificationId
        );
        assertThat(readResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResp.getBody()).isEqualTo("Read!");

        // GET read
        ResponseEntity<UserNotification[]> getRead = rest.getForEntity(
                "/api/notifications/?userId={uid}&status=read",
                UserNotification[].class, testUserId
        );
        assertThat(getRead.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRead.getBody()).hasSize(1);
        assertThat(getRead.getBody()[0].getStatus()).isEqualTo("read");
        assertThat(getRead.getBody()[0].getReadAt()).isNotNull();
    }

    @Test
    void update_preferences_happy_and_bad_path() {
        // happy path
        PreferenceUpdateRequest prefReq = new PreferenceUpdateRequest(testUserId, NotificationPreference.PUSH);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Email", "bob@example.com");
        HttpEntity<PreferenceUpdateRequest> req = new HttpEntity<>(prefReq, headers);

        ResponseEntity<String> ok = rest.exchange(
                "/api/notifications/preferences",
                HttpMethod.PUT, req, String.class
        );
        assertThat(ok.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ok.getBody()).isEqualTo("Preferences updated");

        // verify in DB
        var saved = preferenceRepository.findByUserId(testUserId).orElseThrow();
        assertThat(saved.getPreference()).isEqualTo(NotificationPreference.PUSH);
        assertThat(saved.getUserEmail()).isEqualTo("bob@example.com");

        // bad path: null preference → 400
        prefReq = new PreferenceUpdateRequest(testUserId, null);
        req = new HttpEntity<>(prefReq, headers);
        ResponseEntity<String> bad = rest.exchange(
                "/api/notifications/preferences",
                HttpMethod.PUT, req, String.class
        );
        assertThat(bad.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(bad.getBody()).contains("Preference must be set");
    }
}
