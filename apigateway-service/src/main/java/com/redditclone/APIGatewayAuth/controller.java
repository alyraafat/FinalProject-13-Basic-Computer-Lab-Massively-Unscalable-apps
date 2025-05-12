package com.redditclone.APIGatewayAuth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway/public")
public class controller {
    @RequestMapping("/healthcheck")
    public String healthCheck() {
        return "API Gateway is up and running!";
    }
}
