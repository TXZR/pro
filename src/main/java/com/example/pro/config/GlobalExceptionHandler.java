package com.example.pro.config;

import com.example.pro.dto.TywResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public TywResponse exceptionHandler(Exception e, HttpServletRequest req) {
        log.error("系统运行异常！", e);
        return TywResponse.fail(e);
    }

}
