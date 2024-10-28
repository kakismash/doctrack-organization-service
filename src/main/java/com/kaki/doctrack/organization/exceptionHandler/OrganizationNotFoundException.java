package com.kaki.doctrack.organization.exceptionHandler;

import lombok.Getter;

@Getter
public class OrganizationNotFoundException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public OrganizationNotFoundException(String organizationEmail) {
        super("Organization not found with email: " + organizationEmail);
        errorMessage = "Organization not found with email: " + organizationEmail;
        this.errorCode = "ORGANIZATION_TYPE_404";
    }

    public OrganizationNotFoundException(Long organizationId) {
        super("Organization not found with id: " + organizationId);
        errorMessage = "Organization not found with id: " + organizationId;
        this.errorCode = "ORGANIZATION_TYPE_404";
    }
}
