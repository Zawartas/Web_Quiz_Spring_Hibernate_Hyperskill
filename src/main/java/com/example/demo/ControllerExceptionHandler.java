package com.example.demo;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Need at least two options in a quiz!")
    public HashMap<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Need at least two options in a quiz. [" + e.getMessage() + "]");
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Aaaargh!")
    public HashMap<String, String> handleResourceNotFoundException(ResourceNotFoundException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Aaaargh! Email")
    public HashMap<String, String> handleEmailExistsException(EmailExistsException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

}
