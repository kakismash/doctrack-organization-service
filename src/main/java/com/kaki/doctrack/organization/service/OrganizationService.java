package com.kaki.doctrack.organization.service;

import com.kaki.doctrack.organization.dto.organization.CreateOrUpdateOrganizationDto;
import com.kaki.doctrack.organization.dto.organization.OrganizationDto;
import com.kaki.doctrack.organization.dto.stripe.CustomerUpdateDto;
import com.kaki.doctrack.organization.entity.Organization;
import com.kaki.doctrack.organization.entity.OrganizationLocation;
import com.kaki.doctrack.organization.exceptionHandler.OrganizationAlreadyExistsException;
import com.kaki.doctrack.organization.exceptionHandler.OrganizationNotFoundException;
import com.kaki.doctrack.organization.exceptionHandler.SearchOperationException;
import com.kaki.doctrack.organization.exceptionHandler.SearchTermException;
import com.kaki.doctrack.organization.repository.OrganizationLocationRepository;
import com.kaki.doctrack.organization.repository.OrganizationRepository;
import com.kaki.doctrack.organization.service.client.LocationClient;
import com.kaki.doctrack.organization.service.stripe.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    private final CustomerService customerService;

    private final OrganizationRepository organizationRepository;
    private final LocationClient locationClient;
    private final OrganizationLocationRepository organizationLocationRepository;

    @Transactional
    public Mono<OrganizationDto> addOrganization(CreateOrUpdateOrganizationDto createOrganizationDto) {
        return organizationRepository.findByEmailIgnoreCase(createOrganizationDto.email())
                .flatMap(existingOrg -> Mono.<OrganizationDto>error(new OrganizationAlreadyExistsException(createOrganizationDto.email())))
                .switchIfEmpty(Mono.defer(() -> {
                    Organization organization = createOrganizationDto.toEntity();

                    // Save the organization first
                    return organizationRepository.save(organization)
                            .doOnNext(org -> logger.info("Organization created: {}", org))
                            .flatMap(orgSaved -> updateOrganizationLocations(orgSaved.getId(), createOrganizationDto.locations())
                                    .thenReturn(orgSaved))
                            // Create a customer in Stripe and update organization with customer ID
                            .flatMap(org -> Mono.fromCallable(() -> customerService.createCustomer(org.getEmail(), org.getName()))
                                    .doOnNext(customer -> org.setStripeCustomerId(customer.getId()))

                                    // Save the organization with updated Stripe customer ID
                                    .flatMap(customer -> organizationRepository.save(org)))

                            // Retrieve and set locations
                            .flatMap(this::retrieveLocations)
                            .doOnError(e -> logger.error("Error creating organization", e));
                }));
    }

    @SneakyThrows
    private Mono<OrganizationDto> retrieveLocations(Organization organization) {
        // Retrieve all locations at once by passing the set of location IDs
        return locationClient.getLocationsByIds(getOrganizationLocationIds(organization.getId()))
                .collect(Collectors.toSet()) // Collect all LocationDto into a Set
                .map(locations -> OrganizationDto.fromEntity(organization, locations)) // Map to OrganizationDto
                .doOnError(e -> logger.error("Error retrieving locations for organization: {}", organization.getId(), e));
    }

    @Transactional
    public Mono<Void> updateOrganizationLocations(Long organizationId, Set<Long> newLocationIds) {
        return organizationRepository.findById(organizationId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Organization not found")))
                .flatMap(organization -> {
                    // Persist changes in the join table
                    return organizationLocationRepository.deleteByOrganizationId(organization.getId())
                            .thenMany(Flux.fromIterable(newLocationIds)
                                    .flatMap(locationId -> {
                                        OrganizationLocation organizationLocation = new OrganizationLocation();
                                        organizationLocation.setOrganizationId(organizationId);
                                        organizationLocation.setLocationId(locationId);
                                        return organizationLocationRepository.save(organizationLocation);
                                    }))
                            .then();
                });
    }

    @Transactional
    public Mono<OrganizationDto> updateOrganization(Long organizationId, CreateOrUpdateOrganizationDto updateOrganizationDto) {
        return organizationRepository.findById(organizationId)
                .switchIfEmpty(Mono.error(new OrganizationNotFoundException(organizationId)))
                .flatMap(existingOrganization -> {
                    boolean needUpdateCustomer = updateOrganizationDto.onEntity(existingOrganization);

                    // Save the organization and update Stripe customer if needed
                    return organizationRepository.save(existingOrganization)
                            .doOnSuccess(org -> logger.info("Organization updated: {}", org))
                            .flatMap(savedOrganization -> updateOrganizationLocations(savedOrganization.getId(), updateOrganizationDto.locations())
                                    .thenReturn(savedOrganization))
                            .flatMap(updatedOrganization -> {
                                if (needUpdateCustomer) {
                                    logger.info("Updating Stripe customer for organization: {}", organizationId);
                                    return updateStripeCustomer(existingOrganization);
                                }
                                return Mono.just(updatedOrganization);
                            });
                })
                .flatMap(this::retrieveLocations)
                .doOnError(e -> logger.error("Error updating organization: {}", organizationId, e));
    }

    // Helper method to update the Stripe customer asynchronously
    private Mono<Organization> updateStripeCustomer(Organization organization) {
        return Mono.justOrEmpty(organization.getStripeCustomerId())
                .flatMap(customerId -> Mono.fromCallable(() -> customerService.retrieveCustomerByCustomerId(customerId))
                        .flatMap(customer -> Mono.fromRunnable(() ->
                                customerService.updateCustomer(customer.getId(), CustomerUpdateDto.fromEntityOrganization(organization))
                        ))
                        .thenReturn(organization)
                        .doOnSuccess(cust -> logger.info("Stripe customer updated successfully: {}", customerId))
                        .doOnError(e -> logger.error("Error updating Stripe customer for organization: {}", organization.getId(), e))
                );
    }

    public Mono<Void> deleteOrganizationById(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .switchIfEmpty(Mono.error(new OrganizationNotFoundException(organizationId)))
                .flatMap(existingOrganization -> {
                    // Log finding the organization
                    logger.info("Found organization to delete: {}", existingOrganization);

                    // Attempt to delete the Stripe customer
                    if (existingOrganization.getStripeCustomerId() != null) {
                        return Mono.fromCallable(() -> customerService.deleteCustomer(existingOrganization.getStripeCustomerId()))
                                .doOnSuccess(unused -> logger.info("Stripe customer deleted successfully for organization: {}", organizationId))
                                .doOnError(e -> logger.error("Error deleting Stripe customer for organization: {}", organizationId, e))
                                .then(organizationRepository.deleteById(organizationId))
                                .doOnSuccess(unused -> logger.info("Organization deleted successfully: {}", organizationId))
                                .then();
                    } else {
                        // No Stripe customer to delete, just delete the organization
                        return organizationRepository.deleteById(organizationId)
                                .doOnSuccess(unused -> logger.info("Organization deleted successfully without Stripe customer: {}", organizationId))
                                .then();
                    }
                })
                .doOnError(e -> logger.error("Error deleting organization: {}", organizationId, e));
    }

    public Mono<OrganizationDto> findOrganizationById(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .switchIfEmpty(Mono.error(new OrganizationNotFoundException(organizationId)))
                .flatMap(this::retrieveLocations);
    }

    public Mono<PageImpl<OrganizationDto>> findWithSearchTermAndPageable(String searchTerm, int page, int size) {
        int offset = page * size;
        logger.info("Searching for organizations with term: '{}' on page: {} with size: {}", searchTerm, page, size);

        return organizationRepository.findAllWithSearchTerm(searchTerm, size, offset)
                .collectList()
                .doOnNext(results -> logger.info("Found {} organizations matching the search term", results.size()))
                .zipWith(organizationRepository.count()) // Get total count for pagination
                .flatMap(tuple -> {
                    List<Organization> organizations = tuple.getT1();
                    long total = tuple.getT2();
                    logger.info("Total organizations found: {}", total);

                    // Convert each organization to OrganizationDto with retreiveLocations
                    return Flux.fromIterable(organizations)
                            .flatMap(this::retrieveLocations) // Call retreiveLocations reactively for each organization
                            .collectList()
                            .map(dtoList -> {
                                Pageable pageable = PageRequest.of(page, size);
                                return new PageImpl<>(dtoList, pageable, total);
                            });
                })
                .doOnError(e -> logger.error("Error occurred while searching organizations: {}", e.getMessage(), e))
                .onErrorResume(e -> {
                    logger.error("Handling error: {}", e.getMessage(), e);
                    if (e instanceof IllegalArgumentException) {
                        return Mono.error(new SearchTermException("Invalid search term provided"));
                    } else {
                        return Mono.error(new SearchOperationException("An unexpected error occurred during search"));
                    }
                });
    }

    public Mono<Set<Long>> getOrganizationLocationIds(Long organizationId) {
        return organizationLocationRepository.findAllByOrganizationId(organizationId)
                .collectList()
                .map(locations -> locations.stream()
                        .map(OrganizationLocation::getLocationId)
                        .collect(Collectors.toSet()));
    }

}
