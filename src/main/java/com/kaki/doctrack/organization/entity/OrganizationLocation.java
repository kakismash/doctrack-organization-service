package com.kaki.doctrack.organization.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.Objects;

@Getter
@Setter
@Entity(name = "organization_location")
@Table(name = "organization_location", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"organization_id", "location_id"}, name = "organization_location_organization_id_location_id_key")
})
@org.springframework.data.relational.core.mapping.Table("organization_location")
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationLocation {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "location_id")
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationLocation that = (OrganizationLocation) o;
        return Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, locationId);
    }
}