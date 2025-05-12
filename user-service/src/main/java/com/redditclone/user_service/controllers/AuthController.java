package com.redditclone.user_service.controllers;

import com.redditclone.user_service.dtos.LoginObject;
import com.redditclone.user_service.dtos.LogoutRequest;
import com.redditclone.user_service.dtos.RegisterObject;
import com.redditclone.user_service.dtos.TokenRefreshRequest;
import com.redditclone.user_service.services.AuthService;
import com.redditclone.user_service.utils.ResponseHandler;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/public")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test_permit")
    public ResponseEntity<Object> testPermit() {
        return ResponseHandler.generateResponse("Test Permit", HttpStatus.OK, null);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginObject loginObject) {
        return ResponseHandler.generateResponse("User Logged In Successfully", HttpStatus.OK, authService.login(loginObject));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody RegisterObject registerObject) {
        authService.signup(registerObject);
        return ResponseHandler.generateResponse("User Registered Successfully", HttpStatus.OK, null);
    }

    @GetMapping("/activateAccount/{token}")
    public ResponseEntity<Object> activateAccount(@PathVariable String token) {
        authService.activateAccount(token);
        return ResponseHandler.generateResponse("Account Activated Successfully", HttpStatus.OK, null);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken(
            @RequestBody TokenRefreshRequest refreshRequest,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(refreshRequest.getRefreshToken(), response);
        return ResponseHandler.generateResponse("Token Refreshed Successfully", HttpStatus.OK, null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            @RequestBody LogoutRequest logoutRequest
            ) {
        authService.logout(logoutRequest.getRefreshToken());
        return ResponseHandler.generateResponse("User Logged Out Successfully", HttpStatus.OK, null);
    }
}
