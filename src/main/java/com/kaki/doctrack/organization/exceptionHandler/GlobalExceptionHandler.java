package com.kaki.doctrack.organization.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrganizationNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleOrganizationNotFoundException(OrganizationNotFoundException ex) {
        logger.error("Organization not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

    @ExceptionHandler(OrganizationAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleOrganizationAlreadyExistsException(OrganizationAlreadyExistsException ex) {
        logger.error("Organization already exists", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

    @ExceptionHandler(SearchTermException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleSearchTermException(SearchTermException ex) {
        logger.error("Invalid search term provided", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

    @ExceptionHandler(SearchOperationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleSearchOperationException(SearchOperationException ex) {
        logger.error("Invalid search operation provided", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

}

