package com.bank.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(
                exception.getMessage()
        );

        errorResponse.setStatus(
                HttpStatus.NOT_FOUND.value()
        );

        errorResponse.setTimestamp(
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(
                exception.getMessage()
        );

        errorResponse.setStatus(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        errorResponse.setTimestamp(
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {

        String errorMessage =
                exception
                        .getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        ErrorResponse errorResponse =
                new ErrorResponse();

        errorResponse.setMessage(
                errorMessage
        );

        errorResponse.setStatus(
                HttpStatus.BAD_REQUEST.value()
        );

        errorResponse.setTimestamp(
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(
            InsufficientBalanceException.class
    )
    public ResponseEntity<ErrorResponse>
    handleInsufficientBalanceException(
            InsufficientBalanceException exception
    ) {

        ErrorResponse errorResponse =
                new ErrorResponse();

        errorResponse.setMessage(
                exception.getMessage()
        );

        errorResponse.setStatus(
                HttpStatus.BAD_REQUEST.value()
        );

        errorResponse.setTimestamp(
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }
}