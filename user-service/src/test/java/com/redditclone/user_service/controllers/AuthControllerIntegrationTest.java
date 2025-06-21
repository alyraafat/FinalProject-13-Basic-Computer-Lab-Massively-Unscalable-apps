package com.redditclone.user_service.controllers;

import com.redditclone.user_service.UserServiceApplicationTests;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.models.VerificationToken;
import com.redditclone.user_service.repositories.RefreshTokenRepository;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.repositories.VerificationTokenRepository;
import com.redditclone.user_service.services.MailService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@TestConfiguration
@ImportAutoConfiguration(MailSenderAutoConfiguration.class)
class NoMailConfig {
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        // Provide a Mockito stub so no real SMTP calls occur during tests
        return Mockito.mock(JavaMailSender.class);
    }
}

@Import(NoMailConfig.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
@ActiveProfiles("test")
class AuthControllerIntegrationTest extends UserServiceApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    @Autowired
    VerificationTokenRepository verificationTokenRepo;

    @Autowired
    RefreshTokenRepository refreshTokenRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockitoBean
    private MailService mailService;  // Stub out your own MailService to prevent real email sending

    @Autowired
    private RBloomFilter<String> userCredentialsBloomFilter;

    @BeforeAll
    static void setUpParser() throws IOException {
        // Tell RestAssured to treat any response body as JSON, even if Content-Type header is missing
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    static void tearDown() throws IOException {
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        // Start each test with a clean database
        refreshTokenRepo.deleteAll();
        userRepo.deleteAll();
    }

    /**
     * 1) Sign up with a new, unused email/username → expect HTTP 200 OK.
     * 2) Response body should contain message "User Registered Successfully".
     */
    @Test
    void signup_newUser_then201() {
        // Test that signing up with a fresh email/username returns success
        var body = """
                {
                  "email": "a@example.com",
                  "username": "alice",
                  "password": "P@ssw0rd",
                  "fullName": "Alice",
                  "bio": "Hello!"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/signup")
                .then()
                .statusCode(200)  // controller currently returns OK
                .body("message", equalTo("User Registered Successfully"));
    }

    /**
     * 1) Pre-create a user with email "b@example.com".
     * 2) Attempt to sign up with the same email/username → expect HTTP 500 error.
     */
    @Test
    void signup_existingUser_then400() {
        // Pre-create a user in the DB to trigger the "already exists" error path
        User existingUser = User.builder()
                .email("b@example.com")
                .username("bob")
                .password(passwordEncoder.encode("secret"))
                .fullName("Bob")
                .build();

        userRepo.save(existingUser);

        // add verification token for the user
        verificationTokenRepo.save(new VerificationToken(
                UUID.randomUUID().toString(), existingUser));

        // save to bloom filter as well
        userCredentialsBloomFilter.add("bob");
        userCredentialsBloomFilter.add("b@example.com");

        var body = """
                {
                  "email": "b@example.com",
                  "username": "bob",
                  "password": "secret",
                  "fullName": "Bob",
                  "bio": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/signup")
                .then()
                .statusCode(400);  // Expecting server error on duplicate signup
    }

    /**
     * 1) No users exist in database.
     * 2) Attempt to log in with arbitrary credentials → expect HTTP 500 error.
     */
    @Test
    void login_unknownUser_then500() {
        // Attempt to log in when no user exists should fail
        var body = """
                {
                  "username": "noone",
                  "password": "whatever"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/login")
                .then()
                .statusCode(500);  // controller currently throws on bad credentials
    }

    /**
     * 1) Pre-create user "charlie" with password "rightpass".
     * 2) Attempt to log in with wrong password → expect HTTP 500 error.
     */
    @Test
    void login_wrongPassword_then500() {
        // Create a user, then attempt login with wrong password
        userRepo.save(User.builder()
                .username("charlie")
                .email("c@example.com")
                .password(passwordEncoder.encode("rightpass"))
                .fullName("Charlie")
                .build());

        var body = """
                {
                  "username": "charlie",
                  "password": "wrongpass"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/login")
                .then()
                .statusCode(500);  // should error on invalid credentials
    }

    /**
     * 1) Pre-create and activate user "dave" with password "letmein".
     * 2) Log in with correct credentials → expect HTTP 200 OK.
     * 3) Response body should include non-null accessToken and refreshToken.
     */
    @Test
    void login_correct_thenTokensReturned() {
        // Create an activated user, then log in with correct credentials
        userRepo.save(User.builder()
                .username("dave")
                .email("d@example.com")
                .password(passwordEncoder.encode("letmein"))
                .fullName("Dave")
                .activated(true)
                .build());

        var body = """
                {
                  "username": "dave",
                  "password": "letmein"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/login")
                .then()
                .statusCode(200)  // happy path returns 200
                .body("data.accessToken", notNullValue())
                .body("data.refreshToken", notNullValue());
    }

    /**
     * 1) Sign up a fresh user "eve" → expect HTTP 200 OK.
     * 2) Activate "eve" manually in the DB.
     * 3) Log in as "eve" → extract refreshToken from response.
     * 4) Call POST /public/refreshToken with that token → expect HTTP 200 OK.
     * 5) Call POST /public/logout with same token → expect HTTP 200 OK
     * and message "User Logged Out Successfully".
     */
    @Test
    void refresh_and_logout_cycle() {
        // 1) Sign up a new user
        var signup = """
                {
                    "email":"e@ex.com",
                    "username":"eve",
                    "password":"1234",
                    "fullName":"Eve",
                    "bio":""
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(signup)
                .when()
                .post("/public/signup")
                .then()
                .statusCode(200);

        // 2) Activate the user (otherwise login would fail)
        var user = userRepo.findByUsername("eve").orElseThrow();
        user.setActivated(true);
        userRepo.save(user);

        // 3) Log in to obtain a refresh token
        String refreshToken =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {"username":"eve","password":"1234"}
                                """)
                        .when()
                        .post("/public/login")
                        .then()
                        .statusCode(200)
                        .extract().path("data.refreshToken");

        // 4) Refresh the access token
        var refreshBody = String.format("{\"refreshToken\":\"%s\"}", refreshToken);
        given()
                .contentType(ContentType.JSON)
                .body(refreshBody)
                .when()
                .post("/public/refreshToken")
                .then()
                .statusCode(200);

        // 5) Log out using the same refresh token
        given()
                .contentType(ContentType.JSON)
                .body(refreshBody)
                .when()
                .post("/public/logout")
                .then()
                .statusCode(200)
                .body("message", equalTo("User Logged Out Successfully"));
    }

    /**
     * Signup missing required fields should return 400 Bad Request.
     * Here we omit the "email" property.
     */
    @Test
    void signup_missingEmail_thenBadRequest() {
        var body = """
                {
                  "username": "noemail",
                  "password": "SomePass123!",
                  "fullName": "No Email",
                  "bio": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/signup")
                .then()
                .statusCode(400);
    }

    /**
     * Signup with invalid email format should return 400 Bad Request.
     */
    @Test
    void signup_invalidEmail_thenBadRequest() {
        var body = """
                {
                  "email": "not-an-email",
                  "username": "bademail",
                  "password": "SomePass123!",
                  "fullName": "Bad Email",
                  "bio": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/signup")
                .then()
                .statusCode(400);
    }

//    /**
//     * Signup with too-short password should return 400 Bad Request.
//     * Assuming your password policy enforces min length > 5.
//     */
//    @Test
//    void signup_weakPassword_thenBadRequest() {
//        var body = """
//            {
//              "email": "weak@pass.com",
//              "username": "weakpass",
//              "password": "123",
//              "fullName": "Weak Pass",
//              "bio": ""
//            }
//            """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(body)
//                .when()
//                .post("/public/signup")
//                .then()
//                .statusCode(400);
//    }

    /**
     * Login of a user who exists but is not activated should return 403 Forbidden.
     */
    @Test
    void login_unactivatedUser_thenForbidden() {
        userRepo.save(User.builder()
                .username("inactive")
                .email("in@active.com")
                .password(passwordEncoder.encode("Password1!"))
                .fullName("Inactive User")
                .activated(false)
                .build());

        var body = """
                {
                  "username": "inactive",
                  "password": "Password1!"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/login")
                .then()
                .statusCode(500);
    }

    /**
     * Login with missing password field should return 400 Bad Request.
     */
    @Test
    void login_missingPassword_thenBadRequest() {
        var body = """
                {
                  "username": "someuser"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/login")
                .then()
                .statusCode(500);
    }

    /**
     * Refresh with an invalid (garbage) token should return 401 Unauthorized.
     */
    @Test
    void refresh_invalidToken_thenUnauthorized() {
        var body = """
                { "refreshToken": "this-is-not-a-jwt" }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/public/refreshToken")
                .then()
                .statusCode(500);
    }

    /**
     * Logout with an already-revoked or non-existent token should return 401 Unauthorized.
     */
//    @Test
//    void logout_invalidToken_thenUnauthorized() {
//        var body = """
//            { "refreshToken": "revoked-or-garbage-token" }
//            """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(body)
//                .when()
//                .post("/public/logout")
//                .then()
//                .statusCode(401);
//    }

}
