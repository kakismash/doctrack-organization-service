package com.kaki.doctrack.organization.exceptionHandler;

import lombok.Getter;

@Getter
public class OrganizationAlreadyExistsException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public OrganizationAlreadyExistsException(String organizationEmail) {
        super("Organization already exists with email: " + organizationEmail);
        errorMessage = "Organization already exists with email: " + organizationEmail;
        this.errorCode = "ORGANIZATION_TYPE_409";
    }
}
