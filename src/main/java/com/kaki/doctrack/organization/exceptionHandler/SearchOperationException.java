package com.kaki.doctrack.organization.exceptionHandler;

import lombok.Getter;

@Getter
public class SearchOperationException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public SearchOperationException(String message) {
        super("Error during search operation: " + message);
        this.errorMessage = "Error during search operation: " + message;
        this.errorCode = "SEARCH_OPERATION_ERROR";
    }
}
