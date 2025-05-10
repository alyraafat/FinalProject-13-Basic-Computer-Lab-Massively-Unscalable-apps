package com.redditclone.user_service.controllers;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.RefreshTokenRepository;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.services.MailService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestConfiguration
class NoMailConfig {
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }
}

@Import(NoMailConfig.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RefreshTokenRepository refreshTokenRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockitoBean
    private MailService mailService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        userRepo.deleteAll();
        refreshTokenRepo.deleteAll();
    }
    @BeforeAll
    static void setUpParser() {
        RestAssured.defaultParser = Parser.JSON;
    }
    @Test
    void signup_newUser_then201() {
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
                .statusCode(200)
                .body("message", equalTo("User Registered Successfully"));
    }

    @Test
    void signup_existingUser_then500() {
        // pre-create
        userRepo.save(User.builder()
                .email("b@example.com")
                .username("bob")
                .password(passwordEncoder.encode("secret"))
                .fullName("Bob")
                .build());

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
                .statusCode(500);
    }

    @Test
    void login_unknownUser_then500() {
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
                .statusCode(500);
    }

    @Test
    void login_wrongPassword_then500() {
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
                .statusCode(500);
    }

    @Test
    void login_correct_thenTokensReturned() {
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
                .statusCode(200)
                .body("data.accessToken", notNullValue())
                .body("data.refreshToken", notNullValue());
    }

    @Test
    void refresh_and_logout_cycle() {
        // 1) signup & login to get refresh token
        var signup = """
            {
                "email":"e@ex.com",
                "username":"eve",
                "password":"1234",
                "fullName":"Eve",
                "bio":""
            }
            """;
        var login = """
            {
                "username":"eve",
                "password":"1234"
            }
            """;

        given().contentType(ContentType.JSON).body(signup)
                .post("/public/signup").then().statusCode(200);

        // make sure that user is now activated
        var user = userRepo.findByUsername("eve").orElseThrow();
        user.setActivated(true);
        userRepo.save(user);

        String refreshToken =
                given().contentType(ContentType.JSON).body(login)
                        .post("/public/login")
                        .then().statusCode(200)
                        .extract().path("data.refreshToken");

        var refreshBody = String.format("{\"refreshToken\":\"%s\"}", refreshToken);
        given().contentType(ContentType.JSON).body(refreshBody)
                .post("/public/refreshToken")
                .then().statusCode(200);

        given().contentType(ContentType.JSON).body(refreshBody)
                .post("/public/logout")
                .then().statusCode(200)
                .body("message", equalTo("User Logged Out Successfully"));
    }
}
