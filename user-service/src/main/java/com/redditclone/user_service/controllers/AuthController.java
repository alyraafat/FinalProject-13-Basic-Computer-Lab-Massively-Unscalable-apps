package com.redditclone.user_service.controllers;

import com.redditclone.user_service.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("/test_auth")
    public ResponseEntity<Object> testAuth() {
        return ResponseHandler.generateResponse("Test Auth", HttpStatus.OK, null);
    }

    @GetMapping("/test_permit")
    public ResponseEntity<Object> testPermit() {
        return ResponseHandler.generateResponse("Test Permit", HttpStatus.OK, null);
    }
}
