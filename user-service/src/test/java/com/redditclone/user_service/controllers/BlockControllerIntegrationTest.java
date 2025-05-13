package com.redditclone.user_service.controllers;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.BlockRepository;
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
class BlockControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepo;

    @Autowired
    BlockRepository blockRepo;

    User alice;
    User bob;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // wipe out all users and blocks
        blockRepo.deleteAll();
        userRepo.deleteAll();

        // create two test users
        alice = userRepo.save(User.builder()
                .email("alice@example.com")
                .username("alice")
                .password("irrelevant")   // password not used here
                .fullName("Alice")
                .activated(true)
                .build());

        bob = userRepo.save(User.builder()
                .email("bob@example.com")
                .username("bob")
                .password("irrelevant")
                .fullName("Bob")
                .activated(true)
                .build());
    }

    /**
     * 1) Alice blocks Bob via POST   → should return HTTP 201 Created.
     * 2) Check if Bob is blocked via GET /users/{alice}/block/{bob}   → should return HTTP 200 and body "true".
     * 3) Retrieve Alice’s blocked list via GET /users/{alice}/block   → should return a JSON array of size 1 containing Bob’s user object.
     * 4) Retrieve combined blocks via GET /users/{alice}/block/allblocks   → should return a JSON array of size 1 containing Bob’s UUID.
     * 5) Alice unblocks Bob via DELETE   → should return HTTP 204 No Content.
     * 6) Re-check if Bob is blocked via GET /users/{alice}/block/{bob}   → should return HTTP 200 and body "false".
     */
    @Test
    void block_and_unblock_happyPath() {
        // 1. Alice blocks Bob → 201
        given()
                .when()
                .post("/users/{userId}/block/{blockedId}", alice.getId(), bob.getId())
                .then()
                .statusCode(201);

        // 2. Check isBlocked → true
        given()
                .when()
                .get("/users/{userId}/block/{blockedId}", alice.getId(), bob.getId())
                .then()
                .statusCode(200)
                .body(equalTo("true"));

        // 3. getBlockedUsers for Alice → list containing Bob
        given()
                .when()
                .get("/users/{userId}/block", alice.getId())
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", equalTo(bob.getId().toString()));

        // 4. getAllblocks for Alice → also contains Bob’s UUID
        given()
                .when()
                .get("/users/{userId}/block/allblocks", alice.getId())
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0]", equalTo(bob.getId().toString()));

        // 5. Alice unblocks Bob → 204 No Content
        given()
                .when()
                .delete("/users/{userId}/block/{blockedId}", alice.getId(), bob.getId())
                .then()
                .statusCode(204);

        // 6. isBlocked now → false
        given()
                .when()
                .get("/users/{userId}/block/{blockedId}", alice.getId(), bob.getId())
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }

    /**
     * 1) Attempt to block with a non-existent userId → expect 404 Not Found.
     * 2) Attempt to block with a non-existent blockedId → expect 404 Not Found.
     * 3) Attempt to unblock a non-existent block relationship → expect 404 Not Found.
     * 4) Check isBlocked for non-existent relationship → expect 404 Not Found.
     * 5) Retrieve blocked list for a non-existent user → expect 404 Not Found.
     * 6) Retrieve allblocks for a non-existent user → expect 404 Not Found.
     */
    @Test
    void operations_onNonexistentUser_orBlockee_return404() {
        UUID fakeUser = UUID.randomUUID();
        UUID fakeOther = UUID.randomUUID();

        // block with unknown userId
        given()
                .when()
                .post("/users/{userId}/block/{blockedId}", fakeUser, bob.getId())
                .then()
                .statusCode(404);

        // block with unknown blockedId
        given()
                .when()
                .post("/users/{userId}/block/{blockedId}", alice.getId(), fakeOther)
                .then()
                .statusCode(404);

        // unblock unknown combination
        given()
                .when()
                .delete("/users/{userId}/block/{blockedId}", fakeUser, fakeOther)
                .then()
                .statusCode(404);

        // isBlocked on unknown → 404
        given()
                .when()
                .get("/users/{userId}/block/{blockedId}", fakeUser, fakeOther)
                .then()
                .statusCode(404);

        // getBlockedUsers for nonexistent user
        given()
                .when()
                .get("/users/{userId}/block", fakeUser)
                .then()
                .statusCode(404);

        // getAllblocks for nonexistent user
        given()
                .when()
                .get("/users/{userId}/block/allblocks", fakeUser)
                .then()
                .statusCode(404);
    }

    /**
     * Complex scenario for allBlocks endpoint:
     * 1) Seed two additional users C and D.
     * 2) Alice (A) blocks Bob (B) → expect HTTP 201 Created.
     * 3) Charlie (C) blocks Alice (A) → expect HTTP 201 Created.
     * 4) Fetch combined blocks for Alice via GET /users/{A}/block/allblocks:
     *    - Should return HTTP 200 OK.
     *    - Response list should contain exactly two entries.
     *    - First entry is C’s UUID (because C blocked A).
     *    - Second entry is B’s UUID (because A blocked B).
     *    - D’s UUID must not appear in the list.
     */
    @Test
    void allBlocks_complexScenario_returnsOnlyBlockedAndBlockedBy() {
        // Create two more users: C and D
        User c = userRepo.save(User.builder()
                .email("c@example.com")
                .username("charlie")
                .password("irrelevant")
                .fullName("Charlie")
                .activated(true)
                .build());

        User d = userRepo.save(User.builder()
                .email("d@example.com")
                .username("david")
                .password("irrelevant")
                .fullName("David")
                .activated(true)
                .build());

        // 1) A (alice) blocks B (bob)
        given()
                .when()
                .post("/users/{userId}/block/{blockedId}", alice.getId(), bob.getId())
                .then()
                .statusCode(201);

        // 2) C (charlie) blocks A (alice)
        given()
                .when()
                .post("/users/{userId}/block/{blockedId}", c.getId(), alice.getId())
                .then()
                .statusCode(201);

        // 3) Fetch allblocks for A (alice)
        given()
                .when()
                .get("/users/{userId}/block/allblocks", alice.getId())
                .then()
                .statusCode(200)
                // should only contain C then B
                .body("size()", is(2))
                .body("[0]", equalTo(c.getId().toString()))
                .body("[1]", equalTo(bob.getId().toString()))
                // confirm D is not in the list
                .body(not(hasItem(d.getId().toString())));
    }


    /**
     * 1) If Alice has no blocks (and nobody has blocked her),
     *    GET /users/{alice}/block should return an empty JSON array.
     */
    @Test
    void getBlockedUsers_whenNone_thenEmptyList() {
        given()
                .when()
                .get("/users/{userId}/block", alice.getId())
                .then()
                .statusCode(200)
                .body("", hasSize(0));
    }

    /**
     * 2) If Alice never blocked Bob, DELETE /users/{alice}/block/{bob}
     *    should return 404 Not Found.
     */
    @Test
    void unblock_whenNotBlocked_then404() {
        given()
                .when()
                .delete("/users/{userId}/block/{blockedId}", alice.getId(), bob.getId())
                .then()
                .statusCode(404);
    }

    /**
     * 3) Self-block attempt: Alice tries to block herself.
     *    Depending on your service logic you might return 400 or 404.
     *    Here we assert 400 Bad Request for a self-block guard.
     */
    @Test
    void block_self_thenBadRequest() {
        given()
                .when()
                .post("/users/{userId}/block/{blockedId}", alice.getId(), alice.getId())
                .then()
                .statusCode(400);
    }

}
