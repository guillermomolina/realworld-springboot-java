package io.github.raeperd.realworld.application.user;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvise {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException e) {
        String errorMessage = "Login failed";
        log.error(errorMessage);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("{\"errors\":{\"email or password\":[\"is invalid\"]}}");
    }    
}
