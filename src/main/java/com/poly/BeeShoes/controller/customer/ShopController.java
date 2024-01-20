package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {
    private final SanPhamService sanPhamService;
    @GetMapping({"/shop","/shop/"})
    public String shop(Model model){
        List<SanPham> sp = sanPhamService.getAll();
        model.addAttribute("lstsanpham",sp);
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
