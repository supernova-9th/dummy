package com.supernova.dummy.core.handler;

import com.supernova.dummy.core.exception.DummyException;
import com.supernova.dummy.core.wrapper.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DummyException.class)
    public ResultResponse handleWantedException(DummyException ex) {
        return new ResultResponse<>(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(x -> sb.append(x).append("\n"));
        errors.values().forEach(x -> sb.append(x).append("\n"));
        return new ResultResponse<>(HttpStatus.BAD_REQUEST, sb.toString().trim());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultResponse unhandledException(Exception e, HttpServletRequest request) {
        log.error("error occur {}", e);
        return new ResultResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
