package com.kaki.doctrack.organization.controller;

import com.kaki.doctrack.organization.dto.organization.CreateOrUpdateOrganizationDto;
import com.kaki.doctrack.organization.dto.organization.OrganizationDto;
import com.kaki.doctrack.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    private final OrganizationService organizationService;

    @GetMapping()
    public Mono<ResponseEntity<Page<OrganizationDto>>> getOrganizations(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Role") String role,
            @RequestParam(value = "page", defaultValue = "0") int page,  // Default page = 0
            @RequestParam(value = "size", defaultValue = "10") int size,  // Default size = 10
            @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm) {

        logger.info("getOrganizations");

        if (role.equals("SUPER_ADMIN") || role.equals("ADMIN")) {
            return organizationService.findWithSearchTermAndPageable(searchTerm, page, size)
                    .map(ResponseEntity::ok);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrganizationDto>> getOrganizationById(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        logger.info("getOrganizationById");

        if (role.equals("SUPER_ADMIN") || role.equals("ADMIN") || role.equals("ORGANIZATION_ADMIN")) {
            return organizationService.findOrganizationById(id)
                    .map(ResponseEntity::ok);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
    }

    @PostMapping()
    public Mono<ResponseEntity<OrganizationDto>> addOrganization(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateOrUpdateOrganizationDto organizationDto) {

        logger.info("addOrganization");

        if (role.equals("SUPER_ADMIN") || role.equals("ADMIN")) {
            return organizationService.addOrganization(organizationDto)
                    .map(ResponseEntity::ok);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrganizationDto>> updateOrganization(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestBody CreateOrUpdateOrganizationDto organizationDto) {

        logger.info("updateOrganization");

        if (role.equals("SUPER_ADMIN") || role.equals("ADMIN")) {
            return organizationService.updateOrganization(id, organizationDto)
                    .map(ResponseEntity::ok);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrganization(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        logger.info("deleteOrganization");

        if (role.equals("SUPER_ADMIN") || role.equals("ADMIN")) {
            return organizationService.deleteOrganizationById(id)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build()));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
    }
}
