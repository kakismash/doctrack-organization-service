package com.kaki.doctrack.organization.entity;

import com.kaki.doctrack.organization.auditor.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "organization")
@Table(name = "organization", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "stripeCustomerId"}, name = "organization_email_stripe_customer_id_key")
})
@org.springframework.data.relational.core.mapping.Table("organization")
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends AbstractAuditingEntity implements Serializable {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;

    @Column(unique = true)
    private String email;

    @Column(name = "contact_name")
    private String contactName;

    private String website;
    private String logo;
    private String type;
    private String status;

    @Column(name = "stripe_customer_id", unique = true)
    private String stripeCustomerId;

    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;
    private String shippingName;
    private String shippingPhone;

}