package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(NotFoundException e) {
        log.debug("Произошла ошибка NotFoundException. message: {}, Trace: {}", e.getMessage(), e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dublicateValidationException(DublicateException e) {
        log.debug("Произошла ошибка DublicateException. message: {}, Trace: {}", e.getMessage(), e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }
}