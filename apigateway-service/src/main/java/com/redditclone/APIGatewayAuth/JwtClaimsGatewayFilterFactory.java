package com.redditclone.APIGatewayAuth;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtClaimsGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))  // short-circuit if no context
                .flatMap(securityContext -> {
                    var authentication = securityContext.getAuthentication();
                    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                        return chain.filter(exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("X-User-Id", jwt.getClaimAsString("sub"))
                                        .header("X-User-Name", jwt.getClaimAsString("username"))
                                        .header("X-User-Email", jwt.getClaimAsString("email"))
                                        .build())
                                .build());
                    }
                    return chain.filter(exchange);
                }).doOnError(throwable -> {
                    throwable.printStackTrace(System.err);
                });
    }
}