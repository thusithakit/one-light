package com.thusithakit.apigateway.filter;

import com.thusithakit.apigateway.dto.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
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

            // No token
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
                    .bodyToMono(TokenValidationResponse.class)
                    .flatMap(validation -> {

                        // Invalid token
                        if (!validation.valid()
                                || validation.userId() == null
                                || validation.email() == null) {

                            return unauthorized(exchange);
                        }

                        /*
                         * Remove client-supplied identity headers first.
                         * This prevents header spoofing.
                         */
                        ServerWebExchange mutatedExchange =
                                exchange.mutate()
                                        .request(request ->
                                                request.headers(headers -> {

                                                    headers.remove("X-User-Id");
                                                    headers.remove("X-User-Email");

                                                    headers.set(
                                                            "X-User-Id",
                                                            validation.userId().toString()
                                                    );

                                                    headers.set(
                                                            "X-User-Email",
                                                            validation.email()
                                                    );
                                                })
                                        )
                                        .build();

                        return chain.filter(mutatedExchange);
                    })
                    .onErrorResume(error ->
                            unauthorized(exchange)
                    );
        };
    }

    private Mono<Void> unauthorized(
            ServerWebExchange exchange
    ) {
        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse().setComplete();
    }
}