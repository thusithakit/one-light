package com.thusithakit.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class JwtValidationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(
            WebClient.Builder webClientBuilder,
            @Value("${auth.service.url}") String authServiceUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
    }

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            String authorization =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst(HttpHeaders.AUTHORIZATION);

            if (authorization == null ||
                    !authorization.startsWith("Bearer ")) {

                return unauthorized(exchange);
            }

            return webClient
                    .post()
                    .uri("/api/v1/auth/validate")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            authorization
                    )
                    .retrieve()
                    .toBodilessEntity()

                    // Auth Service returned 2xx
                    .flatMap(response ->
                            chain.filter(exchange)
                    )

                    // Auth Service returned 401/5xx/etc.
                    .onErrorResume(error ->
                            unauthorized(exchange)
                    );
        };
    }

    private Mono<Void> unauthorized(
            org.springframework.web.server.ServerWebExchange exchange
    ) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse().setComplete();
    }
}