package com.poly.BeeShoes.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error/404")
    public String handleError404() {
        return "error/404";
    }
    @RequestMapping("/error/500")
    public String handleError500() {
        return "error/500";
    }
    @RequestMapping("/error/400")
    public String handleError400() {
        return "error/400";
    }

}

