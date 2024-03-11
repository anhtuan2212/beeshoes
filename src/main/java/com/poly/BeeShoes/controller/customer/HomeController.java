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
public class HomeController {
    private final SanPhamService sanPhamService;
    @GetMapping({"","/","/index","/light-bee/"})
    public String indexLightBee(Model model){
        List<SanPham> lst1 = sanPhamService.findTop4OrderByNgayTaoDesc();
        List<SanPham> lst2 = sanPhamService.findTop4GiamGia();
        int size = Math.min(lst2.size(), 4);
        List<SanPham> firstFourItems = lst2.subList(0, size);
        model.addAttribute("newproduct",lst1);
        model.addAttribute("saleproduct",firstFourItems);
        return "customer/index";
    }
}
