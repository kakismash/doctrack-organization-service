package com.kaki.doctrack.organization.exceptionHandler;

import lombok.Getter;

@Getter
public class SearchTermException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public SearchTermException(String searchTerm) {
        super("Invalid search term provided: " + searchTerm);
        this.errorMessage = "Invalid search term provided: " + searchTerm;
        this.errorCode = "SEARCH_TERM_INVALID";
    }
}
