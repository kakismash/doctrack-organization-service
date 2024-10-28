package com.kaki.doctrack.organization.repository;

import com.kaki.doctrack.organization.entity.Organization;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrganizationRepository extends R2dbcRepository<Organization, Long>, ReactiveSortingRepository<Organization, Long> {

    Mono<Organization> findByEmailIgnoreCase(String email);

    @Query("SELECT * FROM organization WHERE " +
            "LOWER(name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(address1) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(address2) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(zip) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(country) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY name LIMIT :limit OFFSET :offset")
    Flux<Organization> findAllWithSearchTerm(
            @Param("searchTerm") String searchTerm,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

}