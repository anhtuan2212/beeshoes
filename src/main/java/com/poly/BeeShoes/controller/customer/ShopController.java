package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {
    private final SanPhamService sanPhamService;
    public Authentication getUserAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
    @GetMapping({"/shop","/shop/"})
    public String shop(Model model){
        Authentication auth = getUserAuth();
        System.out.println(auth.toString());

        Pageable pageable = PageRequest.of(0,12);
        Page<SanPham> spx = sanPhamService.getAllShop(pageable);
        model.addAttribute("lstsanpham",spx);
        return "customer/pages/shop/shop";
    }
    @GetMapping({"/shop-detail","/shop-detail/"})
    public String shopDetail(@RequestParam("product")Long id,Model model){
        SanPham sanPham = sanPhamService.getById(id);
        if (sanPham==null || !sanPham.isTrangThai()){
            return "redirect:/shop";
        }
        List<SanPham> lst = sanPhamService.findTop4ByTheLoaiOrderByNgayTaoDesc(sanPham.getTheLoai());
        model.addAttribute("sanPham",sanPham);
        model.addAttribute("lstRelatedProduct",lst);
        return "customer/pages/shop/shop-details";
    }
    @GetMapping({"/shopping-cart","/shopping-cart/"})
    public String shoppingCart(){
        return "customer/pages/shop/shopping-cart";
    }
}
