package com.poly.BeeShoes.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {
    @GetMapping({"/shop","/shop/"})
    public String shop(){
        return "customer/pages/shop/shop";
    }
    @GetMapping({"/shop-detail","/shop-detail/"})
    public String shopDetail(){
        return "customer/pages/shop/shop-details";
    }
    @GetMapping({"/shopping-cart","/shopping-cart/"})
    public String shoppingCart(){
        return "customer/pages/shop/shopping-cart";
    }
}
