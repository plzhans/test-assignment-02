package com.plzhans.assignment.api.controller.common;

import com.plzhans.assignment.api.service.common.datatype.ErrorCode;
import com.plzhans.assignment.api.service.common.datatype.ErrorResponseDto;
import com.plzhans.assignment.common.error.ClientError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;

/**
 * The type Error controller.
 */
@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ErrorResponseDto handleException(MethodArgumentNotValidException ex) {
        log.error(String.format("MethodArgumentNotValid. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.InvalidParams);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnexpectedTypeException.class)
    ErrorResponseDto handleException(UnexpectedTypeException ex) {
        log.error(String.format("UnexpectedType. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.InvalidParams);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ClientError.Exception.class)
    ErrorResponseDto handleException(ClientError.Exception ex) {
        log.error(String.format("ClientError. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.InvalidParams);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    ErrorResponseDto handleException(Exception ex) {
        log.error(String.format("Exception. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.Error);
    }

}
