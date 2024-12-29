package com.kaki.doctrack.organization.auditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.ReactiveAuditorAware;
import reactor.core.publisher.Mono;

public class HeaderBasedAuditorAware implements ReactiveAuditorAware<String> {

    private static final String USER_NAME_KEY = "X-User-Name";

    private final Logger logger = LoggerFactory.getLogger(HeaderBasedAuditorAware.class);

    @Override
    public Mono<String> getCurrentAuditor() {
        return Mono.deferContextual(ctx -> {
            logger.info("Reactive Context Keys: {}", ctx.stream().toList());
            if (ctx.hasKey("X-User-Name")) {
                return Mono.just(ctx.get("X-User-Name"));
            } else {
                return Mono.error(new IllegalStateException("X-User-Name not found in Reactor context"));
            }
        });
    }
}
