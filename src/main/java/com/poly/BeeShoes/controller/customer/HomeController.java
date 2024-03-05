package com.poly.BeeShoes.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"","/","/index","/light-bee/"})
    public String indexLightBee(){
        return "customer/index";
    }
}
