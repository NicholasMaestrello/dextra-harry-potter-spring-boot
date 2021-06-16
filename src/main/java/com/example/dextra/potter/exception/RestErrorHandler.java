package com.example.dextra.potter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class RestErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object processValidationError(NotFoundException ex) {
        String result = ex.getErrorMessage();
        log.error(ex.getErrorMessage());
        return ex;
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object processValidationError(InvalidOperationException ex) {
        String result = ex.getErrorMessage();
        log.error(ex.getErrorMessage());
        return ex;
    }
}
