package com.kaki.doctrack.organization.service.client;

import com.kaki.doctrack.organization.dto.location.LocationDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LocationClient {

    @Value("${spring.application.name}")
    private String organizationServiceName;

    @Value("${api.internal.header}")
    private String internalHeader;

    @Value("${api.internal.apiGatewayPathUrl}")
    private String apiGatewayPathUrl;

    @Value("${api.internal.locationServicePath}")
    private String locationServiceUrl;

    @Value("${api.internal.apiKey}")
    private String apiKey;

    private final Logger logger = LoggerFactory.getLogger(LocationClient.class);

    private final WebClient webClient;

    public Flux<LocationDto> getLocationsByIds(Set<Long> locationIds) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(locationServiceUrl.concat("/batch"))
                        .queryParam("ids", locationIds)
                        .build())
                .header(internalHeader, organizationServiceName)
                .retrieve()
                .bodyToFlux(LocationDto.class);
    }
}
