package com.poly.BeeShoes.controller.cms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cms")
public class ProductController {
    @GetMapping("/product")
    public String product() {
        return "cms/pages/products/products";
    }
    @GetMapping("/product-detail")
    public String productDetail() {
        return "cms/pages/products/product-details";
    }
    @GetMapping("/add-product")
    public String addProduct() {
        return "cms/pages/products/add-product";
    }
}
