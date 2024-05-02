package com.poly.BeeShoes.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public String handleIllegalArgumentException() {
        return "redirect:/error/400";
    }
    @ExceptionHandler(value = {BadRequestException.class})
    public String handleBadRequestException() {
        return "redirect:/error/404";
    }
//    @ExceptionHandler(value = {Exception.class})
//    public String defaultErrorHandler() {
//        return "redirect:/error/404";
//    }
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(Exception ex) {
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (statusCode==500){
            return "redirect:/error/500";
        }
        if (statusCode==404){
            return "redirect:/error/404";
        }
        if (statusCode==400){
            return "redirect:/error/400";
        }
        System.out.println("Mã lỗi HTTP: " + statusCode);
        return "redirect:/error/404";
    }
}
