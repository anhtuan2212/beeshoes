package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.service.KhachHangService;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cms")
@RequiredArgsConstructor
public class CashierController {
    private final KhachHangService khachHangService;
    private final SanPhamService sanPhamService;
    @GetMapping("/cashier")
    public String index (Model model){
        model.addAttribute("customer",khachHangService.getAll());
        model.addAttribute("listsanpham",sanPhamService.getAllApi());
        return "cashier/index";
    }
}
