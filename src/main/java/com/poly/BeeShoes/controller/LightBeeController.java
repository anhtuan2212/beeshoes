package com.poly.BeeShoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LightBeeController {
    @GetMapping({"/indedx","/light-bee/"})
    public String indexLightBee(){
        return "customer/index";
    }
    @GetMapping({"/checkout","/checkout/"})
    public String checkout(){
        return "customer/pages/shop/checkout";
    }
}
