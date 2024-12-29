package com.kaki.doctrack.organization.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaki.doctrack.organization.dto.location.LocationDto;
import com.kaki.doctrack.organization.entity.Organization;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Organization}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganizationDto(Long id,
                              String name, String description, String phone, String address1, String address2,
                              String city, String state, String zip, String country, String email, String contactName,
                              String website, String logo, String type, String status,
                              String stripeCustomerId,
                              Set<LocationDto> locations,
                              String createdBy, Long createdDate, String lastModifiedBy, Long lastModifiedDate) implements Serializable {

    public static OrganizationDto fromEntity(Organization organization, Set<LocationDto> locations) {
        return new OrganizationDto(organization.getId(),
                organization.getName(), organization.getDescription(), organization.getPhone(), organization.getAddress1(),
                organization.getAddress2(), organization.getCity(), organization.getState(), organization.getZip(),
                organization.getCountry(), organization.getEmail(), organization.getContactName(), organization.getWebsite(),
                organization.getLogo(), organization.getType(), organization.getStatus(), organization.getStripeCustomerId(),
                locations, organization.getCreatedBy(), organization.getCreatedDate(),
                organization.getLastModifiedBy(), organization.getLastModifiedDate());
    }
}