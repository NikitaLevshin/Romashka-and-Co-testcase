package com.romashkaco.testcase.exceptions;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        log.info("Получена ошибка валидации 400: {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Ошибка валидации")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException e) {
        log.info("Получена ошибка валидации 400: {}", Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ApiError.builder()
                .message(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                .reason("Ошибка валидации")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        log.info("Получена ошибка 404: {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Контент не найден")
                .status(HttpStatus.NOT_FOUND.toString())
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
