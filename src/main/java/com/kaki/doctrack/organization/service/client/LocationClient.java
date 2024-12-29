package com.kaki.doctrack.organization.service.client;

import com.kaki.doctrack.organization.dto.location.LocationDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
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

    public LocationClient(
            WebClient.Builder webClientBuilder,
            @Value("${api.internal.apiGatewayPathUrl}") String apiGatewayPathUrl,
            @Value("${api.internal.locationServicePath}") String locationServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(apiGatewayPathUrl.concat(locationServiceUrl)).build();
    }

    public Flux<LocationDto> getLocationsByIds(Mono<Set<Long>> locationIds) {
        return locationIds
                .map(ids -> ids.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .flatMapMany(idsParam ->
                        webClient.get()
                                .uri(uriBuilder -> uriBuilder.path("/batch")
                                        .queryParam("ids", idsParam)
                                        .build())
                                .header(internalHeader, organizationServiceName)
                                .header("X-Internal-Api-Key", apiKey)
                                .retrieve()
                                .bodyToFlux(LocationDto.class)
                );
    }
}
