package com.plzhans.assignment.api.controller.common;

import com.plzhans.assignment.api.service.common.datatype.ErrorCode;
import com.plzhans.assignment.api.service.common.datatype.ErrorResponseDto;
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

    /**
     * Handle exception error response dto.
     *
     * @param ex the ex
     * @return the error response dto
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorResponseDto handleException(MethodArgumentNotValidException ex) {
        log.error(String.format("MethodArgumentNotValidException. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.InvalidParams);
    }

    /**
     * Handle exception error response dto.
     *
     * @param ex the ex
     * @return the error response dto
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnexpectedTypeException.class)
    public ErrorResponseDto handleException(UnexpectedTypeException ex) {
        log.error(String.format("UnexpectedTypeException. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.InvalidParams);
    }

    /**
     * Handle exception error response dto.
     *
     * @param ex the ex
     * @return the error response dto
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorResponseDto handleException(Exception ex) {
        log.error(String.format("Exception. message=%s", ex.getMessage()), ex);
        return new ErrorResponseDto(ErrorCode.Error);
    }

}
