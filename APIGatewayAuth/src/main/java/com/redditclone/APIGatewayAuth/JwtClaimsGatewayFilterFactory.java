package com.redditclone.APIGatewayAuth;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtClaimsGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    //        @Override
//        public GatewayFilter apply(Object config) {
//            return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
//                    .map(SecurityContext::getAuthentication)
//                    .flatMap(authentication -> {
//                        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
//                            System.out.println(jwt.getClaims());
//                            exchange.getRequest().mutate()
//                                    .header("X-User-Id", jwt.getClaimAsString("sub"));
//                        }
//                        else
//                            System.out.println("No JWT claims found in the security context");
//                        return chain.filter(exchange);
//                    });
//        }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))  // short-circuit if no context
                .flatMap(securityContext -> {
                    var authentication = securityContext.getAuthentication();
                    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                        System.out.println(jwt.getClaims());
                        System.out.println(jwt.getClaimAsString("sub"));
                        return chain.filter(exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("X-User-Id", jwt.getClaimAsString("sub"))
                                        .build())
                                .build());
                    }
                    return chain.filter(exchange);
                }).doOnError(throwable -> {
                    System.err.println("Error in X-User-Id GatewayFilter: " + throwable.getMessage());
                    throwable.printStackTrace(System.err);
                });
    }
}