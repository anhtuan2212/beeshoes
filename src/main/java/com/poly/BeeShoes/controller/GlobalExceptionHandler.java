package com.poly.BeeShoes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public String handleIllegalArgumentException(HttpServletRequest request) {
        return "redirect:/error";
    }
    @ExceptionHandler(value = {BadRequestException.class})
    public String handleBadRequestException(HttpServletRequest request) {
        return "redirect:/error";
    }
}
