package com.redditclone.notification_service;

import com.redditclone.notification_service.clients.UserClient;
import com.redditclone.notification_service.models.dto.NotificationRequest;
import com.redditclone.notification_service.models.dto.PreferenceUpdateRequest;
import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.models.enums.NotificationPreference;
import com.redditclone.notification_service.models.enums.NotificationType;
import com.redditclone.notification_service.repositories.NotificationRepository;
import com.redditclone.notification_service.repositories.PreferenceRepository;
import com.redditclone.notification_service.repositories.UserNotifyRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NotificationControllerIntegrationTest {

    static final Network network = Network.newNetwork();

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:5.0.8")
            .withNetwork(network)
            .withNetworkAliases("mongo");
    ;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withDatabaseName("user_service")
            .withUsername("postgres")
            .withPassword("1234");

    @Container
    static GenericContainer<?> userService = new GenericContainer<>("redditclone/user-service:latest")
            .withImagePullPolicy(PullPolicy.alwaysPull())
            .withNetwork(network)
            .dependsOn(postgres)
            .withNetworkAliases("user-service")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres:5432/user_service")
            .withEnv("SPRING_DATASOURCE_USERNAME", "postgres")
            .withEnv("SPRING_DATASOURCE_PASSWORD", "1234")
            .withExposedPorts(8080)
            .withEnv("SPRING_PROFILES_ACTIVE", "dev");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
        registry.add("user.service.url", () -> {
            String host = userService.getHost();
            Integer port = userService.getMappedPort(8080);
            return "http://" + host + ":" + port;
        });
    }

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserNotifyRepository userNotifyRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private UserClient userClient;

    private UUID testUserId;
    private UUID testSenderId;
    private NotificationRequest baseReq;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void cleanup() {
        userNotifyRepository.deleteAll();
        notificationRepository.deleteAll();
        preferenceRepository.deleteAll();

        testUserId = UUID.fromString(userClient.getIdByUsername("alice"));
        testSenderId = UUID.randomUUID();
        baseReq = new NotificationRequest(
                "Hello!", List.of(testUserId), testSenderId, "Alice", NotificationType.USER_SPECIFIC
        );
        baseReq.setType(NotificationType.COMMUNITY);
        baseReq.setCreatedAt(Instant.now());
    }

    //Tests the full notification lifecycle: create a notification, retrieve it as unread, mark it as read, and then retrieve it as read.

    @Test
    void create_then_get_unread_then_mark_read_then_get_read() {
        // POST → create
        ResponseEntity<String> post = rest.postForEntity(
                "/api/notification/", baseReq, String.class
        );
        assertThat(post.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(post.getBody()).isEqualTo("Sent!");

        // GET unread
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", testUserId.toString());
        HttpEntity<UUID> readReq = new HttpEntity<>(headers);
        ResponseEntity<UserNotification[]> getUnread = rest.exchange(
                "/api/notification",
                HttpMethod.GET,
                readReq,
                UserNotification[].class
        );
        assertThat(getUnread.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUnread.getBody()).hasSize(1);
        String notificationId = getUnread.getBody()[0].getNotification().getId();
        assertThat(getUnread.getBody()[0].getStatus()).isEqualTo("unread");

        // PUT → mark read
        ResponseEntity<String> readResp = rest.exchange(
                "/api/notification/read?notificationId={nid}",
                HttpMethod.PUT, readReq, String.class, notificationId
        );
        assertThat(readResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResp.getBody()).isEqualTo("Read!");

        // GET read
        ResponseEntity<UserNotification[]> getRead = rest.exchange(
                "/api/notification?status=read",
                HttpMethod.GET,
                readReq,
                UserNotification[].class
        );
        assertThat(getRead.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRead.getBody()).hasSize(1);
        assertThat(getRead.getBody()[0].getStatus()).isEqualTo("read");
        assertThat(getRead.getBody()[0].getReadAt()).isNotNull();
    }

    //    Tests updating notification preferences: happy path for PUSH and bad path for invalid preference.
    @Test
    void update_preferences_happy_and_bad_path() {
        // happy path
        PreferenceUpdateRequest prefReq = new PreferenceUpdateRequest(testUserId, NotificationPreference.PUSH);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", testUserId.toString());
        headers.set("X-User-Email", "bob@example.com");
        HttpEntity<PreferenceUpdateRequest> req = new HttpEntity<>(prefReq, headers);

        ResponseEntity<String> ok = rest.exchange(
                "/api/notification/preferences/{preference}",
                HttpMethod.PUT, req, String.class, NotificationPreference.PUSH
        );
        assertThat(ok.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ok.getBody()).isEqualTo("Preferences updated");

        // verify in DB
        var saved = preferenceRepository.findByUserId(testUserId).orElseThrow();
        assertThat(saved.getPreference()).isEqualTo(NotificationPreference.PUSH);
        assertThat(saved.getUserEmail()).isEqualTo("bob@example.com");

        // bad path: null preference → 400
        ResponseEntity<String> bad = rest.exchange(
                "/api/notification/preferences/{preference}",
                HttpMethod.PUT, req, String.class, "INVALID"
        );
        assertThat(bad.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Tests the USER_SPECIFIC notification flow: creation, unread retrieval, correct deliveredMessage, and marking as read.
    @Test
    void userSpecificNotificationFlow() {
        // arrange: a single receiver (alice)
        baseReq.setType(NotificationType.USER_SPECIFIC);
        baseReq.setReceiversId(List.of(testUserId));

        // POST → create
        ResponseEntity<String> post = rest.postForEntity(
                "/api/notification/", baseReq, String.class
        );
        assertThat(post.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(post.getBody()).isEqualTo("Sent!");

        // GET unread for that user
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", testUserId.toString());
        HttpEntity<Void> getReq = new HttpEntity<>(headers);

        ResponseEntity<UserNotification[]> getUnread = rest.exchange(
                "/api/notification",
                HttpMethod.GET,
                getReq,
                UserNotification[].class
        );
        assertThat(getUnread.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUnread.getBody()).hasSize(1);
        UserNotification un = getUnread.getBody()[0];
        // should carry exactly the original message
        assertThat(un.getNotification().getType()).isEqualTo(NotificationType.USER_SPECIFIC);
        assertThat(un.getDeliveredMessage()).isEqualTo(baseReq.getRawMessage());
        assertThat(un.getStatus()).isEqualTo("unread");

        // PUT → mark read
        String notificationId = un.getNotification().getId();
        ResponseEntity<String> readResp = rest.exchange(
                "/api/notification/read?notificationId={nid}",
                HttpMethod.PUT,
                getReq,
                String.class,
                notificationId
        );
        assertThat(readResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResp.getBody()).isEqualTo("Read!");

        // GET read
        ResponseEntity<UserNotification[]> getRead = rest.exchange(
                "/api/notification?status=read",
                HttpMethod.GET,
                getReq,
                UserNotification[].class
        );
        assertThat(getRead.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRead.getBody()).hasSize(1);
        assertThat(getRead.getBody()[0].getStatus()).isEqualTo("read");
    }

    // Tests the THREAD notification flow: verifies the thread owner receives the owner-specific message and a follower receives the original message.
    @Test
    void threadNotificationFlow() {
        // arrange: two receivers (thread owner = alice, plus bob)
        UUID bobId = UUID.fromString(userClient.getIdByUsername("bob"));
        baseReq.setType(NotificationType.THREAD);
        baseReq.setReceiversId(List.of(testUserId, bobId));

        // POST → create
        ResponseEntity<String> post = rest.postForEntity(
                "/api/notification/", baseReq, String.class
        );
        assertThat(post.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(post.getBody()).isEqualTo("Sent!");

        // --- verify thread‐owner (alice) sees the owner‐specific message  ---
        HttpHeaders ownerHeaders = new HttpHeaders();
        ownerHeaders.set("X-User-Id", testUserId.toString());
        HttpEntity<Void> ownerReq = new HttpEntity<>(ownerHeaders);
        ResponseEntity<UserNotification[]> ownerNotifs = rest.exchange(
                "/api/notification",
                HttpMethod.GET,
                ownerReq,
                UserNotification[].class
        );
        assertThat(ownerNotifs.getBody()).hasSize(1);
        UserNotification ownerUn = ownerNotifs.getBody()[0];
        assertThat(ownerUn.getNotification().getType()).isEqualTo(NotificationType.THREAD);
        // ThreadNotifier sets a custom owner message
        assertThat(ownerUn.getDeliveredMessage())
                .isEqualTo("Hello thread owner, you have a new notification!");
        assertThat(ownerUn.getStatus()).isEqualTo("unread");

        // --- verify follower (bob) sees the original thread message ---
        HttpHeaders bobHeaders = new HttpHeaders();
        bobHeaders.set("X-User-Id", bobId.toString());
        HttpEntity<Void> bobReq = new HttpEntity<>(bobHeaders);
        ResponseEntity<UserNotification[]> bobNotifs = rest.exchange(
                "/api/notification",
                HttpMethod.GET,
                bobReq,
                UserNotification[].class
        );
        assertThat(bobNotifs.getBody()).hasSize(1);
        UserNotification bobUn = bobNotifs.getBody()[0];
        assertThat(bobUn.getNotification().getType()).isEqualTo(NotificationType.THREAD);
        // the “regular” thread‐update message should be the rawMessage
        assertThat(bobUn.getDeliveredMessage()).isEqualTo(baseReq.getRawMessage());
        assertThat(bobUn.getStatus()).isEqualTo("unread");
    }

}
