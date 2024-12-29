package com.kaki.doctrack.organization.repository;

import com.kaki.doctrack.organization.entity.Organization;
import com.kaki.doctrack.organization.entity.OrganizationLocation;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrganizationLocationRepository extends R2dbcRepository<OrganizationLocation, Long> {

    @Query("SELECT * FROM organization_location " +
            "WHERE organization_id = :organizationId")
    Flux<OrganizationLocation> findAllByOrganizationId(Long organizationId);

    @Query("DELETE FROM organization_location " +
            "WHERE organization_id = :organizationId " +
            "AND location_id = :locationId")
    Mono<Void> deleteByOrganizationIdAndLocationId(Long organizationId, Long id);

    Mono<Void> deleteByOrganizationId(Long organizationId);
}
