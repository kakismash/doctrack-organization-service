package com.kaki.doctrack.organization.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class ContextSetupFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(ContextSetupFilter.class);

    private static final String USER_ID = "X-User-Id";
    private static final String USER_NAME = "X-User-Name";
    private static final String USER_ROLE = "X-User-Role";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userId = headers.getFirst(USER_ID);
        String username = headers.getFirst(USER_NAME);
        String userRole = headers.getFirst(USER_ROLE);

        logger.info("Extracted headers - UserID: {}, Username: {}, UserRole: {}", userId, username, userRole);

        return chain.filter(exchange)
                .contextWrite(ctx -> {
                    if (userId != null) ctx = ctx.put(USER_ID, userId);
                    if (username != null) ctx = ctx.put(USER_NAME, username);
                    if (userRole != null) ctx = ctx.put(USER_ROLE, userRole);
                    return ctx;
                });
    }
}
