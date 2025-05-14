package com.redditclone.APIGatewayAuth.configs;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Bean
    @Order(1)
    public SecurityWebFilterChain publicSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityMatcher(exchange -> {
                    String path = exchange.getRequest().getURI().getPath();
                    if (path.contains("/public/"))
                        return ServerWebExchangeMatcher.MatchResult.match();
                    else
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                })
//                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/user/public/**", "/api/gateway/public/**"))
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated())
                .exceptionHandling(exceptionHandling ->
                                exceptionHandling.authenticationEntryPoint(unauthorizedHandler())  // handle missing/invalid JWT
                        //                .accessDeniedHandler(accessDeniedHandler())       // handle 403 forbidden
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] secretBytes = Decoders.BASE64.decode(SECRET_KEY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretBytes);
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public ServerAuthenticationEntryPoint unauthorizedHandler() {
        return (exchange, ex) -> {
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            var buffer = response.bufferFactory().wrap("{\"error\": \"Unauthorized or invalid JWT\"}".getBytes());
            response.getHeaders().add("Content-Type", "application/json");
            return response.writeWith(Mono.just(buffer));
        };
    }
//
//    @Bean
//    public ServerAccessDeniedHandler accessDeniedHandler() {
//        return (exchange, denied) -> {
//            var response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            var buffer = response.bufferFactory().wrap("{\"error\": \"Access denied\"}".getBytes());
//            response.getHeaders().add("Content-Type", "application/json");
//            return response.writeWith(Mono.just(buffer));
//        };
//    }
}
