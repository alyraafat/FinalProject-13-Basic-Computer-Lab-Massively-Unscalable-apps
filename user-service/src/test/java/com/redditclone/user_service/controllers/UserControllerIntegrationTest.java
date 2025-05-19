package com.redditclone.user_service.controllers;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    User alice;
    User bob;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        userRepo.deleteAll();

        // Seed two users for tests
        alice = userRepo.save(User.builder()
                .username("aliceUser")
                .email("aliceUser@example.com")
                .password("pwd")
                .fullName("Alice A")
                .bio("Bio A")
                .activated(true)
                .build());
        bob = userRepo.save(User.builder()
                .username("bobUser")
                .email("bobUser@example.com")
                .password("pwd")
                .fullName("Bob B")
                .bio("Bio B")
                .activated(true)
                .build());
    }

    /**
     * GET  → should return list of all users.
     */
    @Test
    void getAllUsers_returnsSeededUsers() {
        given()
                .when()
                .get("/get_all")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("username", hasItems("aliceUser", "bobUser"));
    }

    /**
     * GET /{id} for existing user → should return that user’s DTO.
     */
    @Test
    void getUserById_existing_thenOk() {
        given()
                .when()
                .get("/{id}", alice.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(alice.getId().toString()))
                .body("username", equalTo("aliceUser"));
    }

    /**
     * GET /{id} for non-existent user → should return 404 Not Found.
     */
    @Test
    void getUserById_nonexistent_thenNotFound() {
        UUID fake = UUID.randomUUID();
        given()
                .when()
                .get("/{id}", fake)
                .then()
                .statusCode(404);
    }

    /**
     * POST  → create a new user; expect returned DTO with generated ID.
     */
    @Test
    void createUser_thenReturnsCreatedDto() {
        var newUser = """
            {
              "username":"charlie",
              "email":"charlie@example.com",
              "password":"pwd",
              "fullName":"Charlie C",
              "bio":"Bio C",
              "activated":true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("username", equalTo("charlie"));
    }

    /**
     * PUT /{id} → update existing user’s bio; expect updated DTO.
     */
    @Test
    void updateUser_thenBioUpdated() {
        var update = """
            {
              "username":"aliceUser",
              "email":"aliceUser@example.com",
              "password":"pwd",
              "fullName":"Alice A",
              "bio":"New Bio",
              "activated":true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .header("X-User-Id", alice.getId().toString())
                .when()
                .put("")
                .then()
                .statusCode(200)
                .body("id", equalTo(alice.getId().toString()))
                .body("bio", equalTo("New Bio"));
    }

    /**
     * DELETE /{id} → delete that user; expect 204 No Content,
     * then GET again returns 404.
     */
    @Test
    void deleteUser_thenNotFoundAfter() {
        // Delete Alice
        given()
                .when()
                .delete("/{id}", alice.getId())
                .then()
                .statusCode(204);

        // Subsequent GET should 404
        given()
                .when()
                .get("/{id}", alice.getId())
                .then()
                .statusCode(404);
    }

    /**
     * GET /search?keyword={kw}&currentUserId={id}&strategyType=username
     * → should return users whose username contains the keyword.
     */
    @Test
    void searchUsers_byUsernameKeyword_returnsMatches() {
        // keyword "ali" should match "alice" but not "bob"
        given()
                .header("X-User-Id", alice.getId().toString())
                .queryParam("keyword", "ali")
                .queryParam("strategyType", "username")
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].username", equalTo("aliceUser"));
    }

    /**
     * Search by email:
     * 1) Use keyword equal to the domain part "example.com".
     * 2) strategyType=email should match both Alice and Bob by their email.
     */
    @Test
    void searchUsers_byEmailKeyword_returnsBoth() {
        given()
                .header("X-User-Id", alice.getId().toString())
                .queryParam("keyword", "example.com")
                .queryParam("strategyType", "email")
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("email", hasItems("aliceUser@example.com", "bobUser@example.com"));
    }

    /**
     * Search with no matching keyword:
     * Should return an empty list and HTTP 200.
     */
    @Test
    void searchUsers_noMatches_returnsEmptyList() {
        given()
                .header("X-User-Id", alice.getId().toString())
                .queryParam("keyword", "nomatch")
                .queryParam("strategyType", "username")
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

    /**
     * Search should exclude the current user:
     * Keyword "ali" matches Alice, but since currentUserId=alice, result should be empty.
     */
//    @Test
//    void searchUsers_excludesCurrentUser() {
//        given()
//                .queryParam("keyword", "ali")
//                .queryParam("currentUserId", alice.getId())
//                .queryParam("strategyType", "username")
//                .when()
//                .get("/search")
//                .then()
//                .statusCode(200)
//                .body("", hasSize(0));
//    }

    /**
     * Case-insensitive search:
     * Keyword "ALICE" should still match "alice" in username search.
     */
    @Test
    void searchUsers_caseInsensitiveUsername() {
        given()
                .header("X-User-Id", bob.getId().toString())
                .queryParam("keyword", "ALICE")// search performed by Bob
                .queryParam("strategyType", "username")
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].username", equalTo("aliceUser"));
    }

    /**
     * GET /test_auth → should return generic ResponseHandler payload.
     */
    @Test
    void testAuth_endpoint() {
        given()
                .when()
                .get("/test_auth")
                .then()
                .statusCode(200)
                .body("message", equalTo("Test Auth"))
                .body("status", equalTo(200))
                .body("data", nullValue());
    }
}
