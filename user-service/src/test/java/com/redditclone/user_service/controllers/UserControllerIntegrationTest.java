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
                .username("alice")
                .email("alice@example.com")
                .password("pwd")
                .fullName("Alice A")
                .bio("Bio A")
                .activated(true)
                .build());
        bob = userRepo.save(User.builder()
                .username("bob")
                .email("bob@example.com")
                .password("pwd")
                .fullName("Bob B")
                .bio("Bio B")
                .activated(true)
                .build());
    }

    /**
     * GET /api/users → should return list of all users.
     */
    @Test
    void getAllUsers_returnsSeededUsers() {
        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("username", hasItems("alice", "bob"));
    }

    /**
     * GET /api/users/{id} for existing user → should return that user’s DTO.
     */
    @Test
    void getUserById_existing_thenOk() {
        given()
                .when()
                .get("/api/users/{id}", alice.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(alice.getId().toString()))
                .body("username", equalTo("alice"));
    }

    /**
     * GET /api/users/{id} for non-existent user → should return 404 Not Found.
     */
    @Test
    void getUserById_nonexistent_thenNotFound() {
        UUID fake = UUID.randomUUID();
        given()
                .when()
                .get("/api/users/{id}", fake)
                .then()
                .statusCode(404);
    }

    /**
     * POST /api/users → create a new user; expect returned DTO with generated ID.
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
                .post("/api/users")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("username", equalTo("charlie"));
    }

    /**
     * PUT /api/users/{id} → update existing user’s bio; expect updated DTO.
     */
    @Test
    void updateUser_thenBioUpdated() {
        var update = """
            {
              "username":"alice",
              "email":"alice@example.com",
              "password":"pwd",
              "fullName":"Alice A",
              "bio":"New Bio",
              "activated":true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/api/users/{id}", alice.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(alice.getId().toString()))
                .body("bio", equalTo("New Bio"));
    }

    /**
     * DELETE /api/users/{id} → delete that user; expect 204 No Content,
     * then GET again returns 404.
     */
    @Test
    void deleteUser_thenNotFoundAfter() {
        // Delete Alice
        given()
                .when()
                .delete("/api/users/{id}", alice.getId())
                .then()
                .statusCode(204);

        // Subsequent GET should 404
        given()
                .when()
                .get("/api/users/{id}", alice.getId())
                .then()
                .statusCode(404);
    }

    /**
     * GET /api/users/search?keyword={kw}&currentUserId={id}&strategyType=username
     * → should return users whose username contains the keyword.
     */
    @Test
    void searchUsers_byUsernameKeyword_returnsMatches() {
        // keyword "ali" should match "alice" but not "bob"
        given()
                .queryParam("keyword", "ali")
                .queryParam("currentUserId", alice.getId())
                .queryParam("strategyType", "username")
                .when()
                .get("/api/users/search")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].username", equalTo("alice"));
    }

    /**
     * Search by email:
     * 1) Use keyword equal to the domain part "example.com".
     * 2) strategyType=email should match both Alice and Bob by their email.
     */
    @Test
    void searchUsers_byEmailKeyword_returnsBoth() {
        given()
                .queryParam("keyword", "example.com")
                .queryParam("currentUserId", alice.getId())
                .queryParam("strategyType", "email")
                .when()
                .get("/api/users/search")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("email", hasItems("alice@example.com", "bob@example.com"));
    }

    /**
     * Search with no matching keyword:
     * Should return an empty list and HTTP 200.
     */
    @Test
    void searchUsers_noMatches_returnsEmptyList() {
        given()
                .queryParam("keyword", "nomatch")
                .queryParam("currentUserId", alice.getId())
                .queryParam("strategyType", "username")
                .when()
                .get("/api/users/search")
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
//                .get("/api/users/search")
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
                .queryParam("keyword", "ALICE")
                .queryParam("currentUserId", bob.getId())  // search performed by Bob
                .queryParam("strategyType", "username")
                .when()
                .get("/api/users/search")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].username", equalTo("alice"));
    }

    /**
     * GET /api/users/test_auth → should return generic ResponseHandler payload.
     */
    @Test
    void testAuth_endpoint() {
        given()
                .when()
                .get("/api/users/test_auth")
                .then()
                .statusCode(200)
                .body("message", equalTo("Test Auth"))
                .body("status", equalTo(200))
                .body("data", nullValue());
    }
}
