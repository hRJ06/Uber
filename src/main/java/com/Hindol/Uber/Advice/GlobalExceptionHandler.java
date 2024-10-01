package com.Hindol.Uber.Advice;

import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Exception.RuntimeConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<APIError> buildErrorResponseEntity(APIError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        APIError apiError = APIError.builder().httpStatus(HttpStatus.NOT_FOUND).message(ex.getMessage()).build();
        log.error("RESOURCE NOT FOUND EXCEPTION : {}", apiError);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<APIError> handleRuntimeConflictException(RuntimeConflictException ex) {
        APIError apiError = APIError.builder().httpStatus(HttpStatus.CONFLICT).message(ex.getMessage()).build();
        log.error("RUNTIME CONFLICT EXCEPTION : {}", apiError);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleInputValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        APIError apiError = APIError.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Input Validation error").subErrors(errors).build();
        log.error("INPUT VALIDATION EXCEPTION : {}", apiError);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIError> handleAuthenticationException(AuthenticationException authenticationException) {
        APIError apiError = APIError.builder().httpStatus(HttpStatus.UNAUTHORIZED).message(authenticationException.getLocalizedMessage()).build();
        log.error("AUTHENTICATION ERROR : {}", apiError);
        return new ResponseEntity<APIError>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleInternalServerError(Exception ex) {
        APIError apiError = APIError.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(ex.getMessage()).build();
        log.error("INTERNAL SERVER ERROR : {}", apiError);
        return buildErrorResponseEntity(apiError);
    }
}