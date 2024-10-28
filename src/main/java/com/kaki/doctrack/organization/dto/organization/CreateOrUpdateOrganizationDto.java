package com.kaki.doctrack.organization.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaki.doctrack.organization.entity.Organization;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Organization}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateOrUpdateOrganizationDto(String name, String description, String phone, String address1, String address2,
                                            String city, String state, String zip, String country, String email,
                                            String contactName, String website, String logo, String type,
                                            String status,
                                            String shippingAddress1,
                                            String shippingAddress2,
                                            String shippingCity,
                                            String shippingState,
                                            String shippingZip,
                                            String shippingCountry,
                                            String shippingName,
                                            String shippingPhone, Set<Long> locationIds) implements Serializable {
  public Organization toEntity() {
    Organization organization = new Organization();

    if (StringUtils.isNotBlank(name)) {
      organization.setName(name);
    }

    if (StringUtils.isNotBlank(description)) {
      organization.setDescription(description);
    }

    if (StringUtils.isNotBlank(phone)) {
      organization.setPhone(phone);
    }

    if (StringUtils.isNotBlank(address1)) {
      organization.setAddress1(address1);
    }

    if (StringUtils.isNotBlank(address2)) {
      organization.setAddress2(address2);
    }

    if (StringUtils.isNotBlank(city)) {
      organization.setCity(city);
    }

    if (StringUtils.isNotBlank(state)) {
      organization.setState(state);
    }

    if (StringUtils.isNotBlank(zip)) {
      organization.setZip(zip);
    }

    if (StringUtils.isNotBlank(country)) {
      organization.setCountry(country);
    }

    if (StringUtils.isNotBlank(email)) {
      organization.setEmail(email);
    }

    if (StringUtils.isNotBlank(contactName)) {
      organization.setContactName(contactName);
    }

    if (StringUtils.isNotBlank(website)) {
      organization.setWebsite(website);
    }

    if (StringUtils.isNotBlank(logo)) {
      organization.setLogo(logo);
    }

    if (StringUtils.isNotBlank(type)) {
      organization.setType(type);
    }

    if (StringUtils.isNotBlank(status)) {
      organization.setStatus(status);
    }

    if (StringUtils.isNotBlank(shippingAddress1)) {
      organization.setShippingAddress1(shippingAddress1);
    }

    if (StringUtils.isNotBlank(shippingAddress2)) {
      organization.setShippingAddress2(shippingAddress2);
    }

    if (StringUtils.isNotBlank(shippingCity)) {
      organization.setShippingCity(shippingCity);
    }

    if (StringUtils.isNotBlank(shippingState)) {
      organization.setShippingState(shippingState);
    }

    if (StringUtils.isNotBlank(shippingZip)) {
      organization.setShippingZip(shippingZip);
    }

    if (StringUtils.isNotBlank(shippingCountry)) {
      organization.setShippingCountry(shippingCountry);
    }

    if (StringUtils.isNotBlank(shippingName)) {
      organization.setShippingName(shippingName);
    }

    if (StringUtils.isNotBlank(shippingPhone)) {
      organization.setShippingPhone(shippingPhone);
    }

    if (locationIds != null) {
      organization.setLocationIds(locationIds);
    }

    return organization;
  }

  // Return true if the organization needs to be updated in Stripe as a Customer
  public Boolean onEntity(Organization organizationToUpdate) {

    boolean needCustomerUpdate = false;

    if (StringUtils.isNotBlank(description) && !description.equals(organizationToUpdate.getDescription())) {
      organizationToUpdate.setDescription(description);
    }

    if (StringUtils.isNotBlank(phone) && !phone.equals(organizationToUpdate.getPhone())) {
      needCustomerUpdate = true;
      organizationToUpdate.setPhone(phone);
    }

    if (StringUtils.isNotBlank(address1) && !address1.equals(organizationToUpdate.getAddress1())) {
      needCustomerUpdate = true;
      organizationToUpdate.setAddress1(address1);
    }

    if (StringUtils.isNotBlank(address2) && !address2.equals(organizationToUpdate.getAddress2())) {
      needCustomerUpdate = true;
      organizationToUpdate.setAddress2(address2);
    }

    if (StringUtils.isNotBlank(city) && !city.equals(organizationToUpdate.getCity())) {
      organizationToUpdate.setCity(city);
    }

    if (StringUtils.isNotBlank(state) && !state.equals(organizationToUpdate.getState())) {
      needCustomerUpdate = true;
      organizationToUpdate.setState(state);
    }

    if (StringUtils.isNotBlank(zip) && !zip.equals(organizationToUpdate.getZip())) {
      needCustomerUpdate = true;
      organizationToUpdate.setZip(zip);
    }

    if (StringUtils.isNotBlank(country) && !country.equals(organizationToUpdate.getCountry())) {
      needCustomerUpdate = true;
      organizationToUpdate.setCountry(country);
    }

    if (StringUtils.isNotBlank(contactName) && !contactName.equals(organizationToUpdate.getContactName())) {
      organizationToUpdate.setContactName(contactName);
    }

    if (StringUtils.isNotBlank(website) && !website.equals(organizationToUpdate.getWebsite())) {
      organizationToUpdate.setWebsite(website);
    }

    if (StringUtils.isNotBlank(logo) && !logo.equals(organizationToUpdate.getLogo())) {
      organizationToUpdate.setLogo(logo);
    }

    if (StringUtils.isNotBlank(type) && !type.equals(organizationToUpdate.getType())) {
      organizationToUpdate.setType(type);
    }

    if (StringUtils.isNotBlank(status) && !status.equals(organizationToUpdate.getStatus())) {
      needCustomerUpdate = true;
      organizationToUpdate.setStatus(status);
    }

    if (StringUtils.isNotBlank(shippingAddress1) && !shippingAddress1.equals(organizationToUpdate.getShippingAddress1())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingAddress1(shippingAddress1);
    }

    if (StringUtils.isNotBlank(shippingAddress2) && !shippingAddress2.equals(organizationToUpdate.getShippingAddress2())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingAddress2(shippingAddress2);
    }

    if (StringUtils.isNotBlank(shippingCity) && !shippingCity.equals(organizationToUpdate.getShippingCity())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingCity(shippingCity);
    }

    if (StringUtils.isNotBlank(shippingState) && !shippingState.equals(organizationToUpdate.getShippingState())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingState(shippingState);
    }

    if (StringUtils.isNotBlank(shippingZip) && !shippingZip.equals(organizationToUpdate.getShippingZip())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingZip(shippingZip);
    }

    if (StringUtils.isNotBlank(shippingCountry) && !shippingCountry.equals(organizationToUpdate.getShippingCountry())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingCountry(shippingCountry);
    }

    if (StringUtils.isNotBlank(shippingName) && !shippingName.equals(organizationToUpdate.getShippingName())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingName(shippingName);
    }

    if (StringUtils.isNotBlank(shippingPhone) && !shippingPhone.equals(organizationToUpdate.getShippingPhone())) {
      needCustomerUpdate = true;
      organizationToUpdate.setShippingPhone(shippingPhone);
    }

    if (StringUtils.isNotBlank(email) && !email.equals(organizationToUpdate.getEmail())) {
      needCustomerUpdate = true;
      organizationToUpdate.setEmail(email);
    }

    if (StringUtils.isNotBlank(name) && !name.equals(organizationToUpdate.getName())) {
      needCustomerUpdate = true;
      organizationToUpdate.setName(name);
    }

    if (shouldUpdateLocations(organizationToUpdate.getLocationIds(), locationIds)) {
      organizationToUpdate.setLocationIds(locationIds);
    }

    return needCustomerUpdate;

  }

  private boolean shouldUpdateLocations(Set<Long> dbLocationIds, Set<Long> dtoLocationIds) {
    return dtoLocationIds != null && (
            // Check if DTO wants to clear the list (empty DTO set vs non-empty DB set)
            (dtoLocationIds.isEmpty() && !dbLocationIds.isEmpty()) ||

                    // Use Streams to check if any element in dtoLocationIds is missing in dbLocationIds or vice versa
                    dbLocationIds.stream().anyMatch(id -> !dtoLocationIds.contains(id)) ||
                    dtoLocationIds.stream().anyMatch(id -> !dbLocationIds.contains(id))
    );
  }
}