package com.kaki.doctrack.organization.service.stripe;

import com.kaki.doctrack.organization.dto.stripe.CustomerUpdateDto;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @PostConstruct
    public void init() {
        com.stripe.Stripe.apiKey = secretKey;
        logger.info("Stripe secret key: {}", secretKey);
    }

    public Customer createCustomer(String email, String name) {
        try {
            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(name)
                    .build();

            return Customer.create(params);
        } catch (StripeException e) {
            logger.error("Error creating customer: {}", e.getMessage());
            return null;
        }
    }

    public Customer retrieveCustomerByCustomerId(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            logger.error("Error retrieving customer: {}", e.getMessage());
            return null;
        }
    }

    public Customer retrieveCustomerByEmail(String email) {
        try {
            Map<String, Object> params = Map.of("email", email);
            return Customer.list(params).getData().getFirst();
        } catch (StripeException e) {
            logger.error("Error retrieving customer: {}", e.getMessage());
            return null;
        }
    }

    public Customer updateCustomer(String customerId, CustomerUpdateDto customerUpdateDto) {
        try {
            Customer customer = Customer.retrieve(customerId);

            CustomerUpdateParams params = CustomerUpdateParams.builder()
                    .setName(customerUpdateDto.name())
                    .setAddress(customerUpdateDto.toAddress())
                    .setShipping(customerUpdateDto.toShipping())
                    .setPhone(customerUpdateDto.phone())
                    .setEmail(customerUpdateDto.email())
                    .setDescription(customerUpdateDto.description())
                    .build();
            return customer.update(params);
        } catch (StripeException e) {
            logger.error("Error updating customer: {}", e.getMessage());
            return null;
        }
    }

    public Customer deleteCustomer(String customerId) {
        try {
            Customer customer = Customer.retrieve(customerId);
            return customer.delete();
        } catch (StripeException e) {
            logger.error("Error deleting customer: {}", e.getMessage());
            return null;
        }
    }

}
