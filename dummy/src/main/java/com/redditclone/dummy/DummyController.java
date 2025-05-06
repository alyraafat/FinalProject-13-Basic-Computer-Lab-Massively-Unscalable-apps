package com.redditclone.dummy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
@RequestMapping
public class DummyController {

    @GetMapping("/test_dummy")
    public String dummy() {
        return "Hi, this is a dummy endpoint!";
    }

    @GetMapping("/test_userid")
    public String dummy(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        if (userId != null) {
            System.out.println("Received X-User-Id: " + userId);
        } else {
            System.out.println("No X-User-Id header provided");
        }
        return "Hello from Dummy Service!";
    }

    @GetMapping("/test_dummy/auth")
    public String health() {
        return "Dummy service is authenticated!";
    }

    @GetMapping("/expose_request")
    public String dummy(HttpServletRequest request) {
        // Print request method and URI
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());

        // Print all headers
        System.out.println("Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }

        // Print all query parameters
        System.out.println("Query Parameters:");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println(key + ": " + String.join(", ", values));
        });

        return "Hello from Dummy Service!";
    }
}
