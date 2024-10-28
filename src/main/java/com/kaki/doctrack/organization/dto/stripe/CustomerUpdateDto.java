package com.kaki.doctrack.organization.dto.stripe;

import com.kaki.doctrack.organization.entity.Organization;
import com.stripe.param.CustomerUpdateParams;

public record CustomerUpdateDto(
        String email,
        String name,
        String description,
        String phone,
        String address1,
        String address2,
        String city,
        String state,
        String zip,
        String country,
        String shippingAddress1,
        String shippingAddress2,
        String shippingCity,
        String shippingState,
        String shippingZip,
        String shippingCountry,
        String shippingName,
        String shippingPhone
) {
    public CustomerUpdateParams.Address toAddress() {
        return CustomerUpdateParams.Address
                .builder()
                .setLine1(address1)
                .setLine2(address2)
                .setCity(city)
                .setState(state)
                .setPostalCode(zip)
                .setCountry(country)
                .build();
    }

    private CustomerUpdateParams.Shipping.Address toShippingAddress() {
        return CustomerUpdateParams.Shipping.Address
                .builder()
                .setLine1(shippingAddress1)
                .setLine2(shippingAddress2)
                .setCity(shippingCity)
                .setState(shippingState)
                .setPostalCode(shippingZip)
                .setCountry(shippingCountry)
                .build();
    }

    public CustomerUpdateParams.Shipping toShipping() {
        return CustomerUpdateParams.Shipping
                .builder()
                .setAddress(this.toShippingAddress())
                .setName(shippingName)
                .setPhone(shippingPhone)
                .build();
    }

    public static CustomerUpdateDto fromEntityOrganization(Organization organization) {
        return new CustomerUpdateDto(
                organization.getEmail(),
                organization.getName(),
                organization.getDescription(),
                organization.getPhone(),
                organization.getAddress1(),
                organization.getAddress2(),
                organization.getCity(),
                organization.getState(),
                organization.getZip(),
                organization.getCountry(),
                organization.getShippingAddress1(),
                organization.getShippingAddress2(),
                organization.getShippingCity(),
                organization.getShippingState(),
                organization.getShippingZip(),
                organization.getShippingCountry(),
                organization.getShippingName(),
                organization.getShippingPhone()
        );
    }
}
