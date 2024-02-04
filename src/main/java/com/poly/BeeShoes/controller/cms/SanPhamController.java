package com.poly.BeeShoes.controller.cms;

import com.google.gson.Gson;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class SanPhamController {
    private final TheLoaiService theLoaiService;
    private final ChatLieuService chatLieuService;
    private final DeGiayService deGiayService;
    private final MauSacService mauSacService;
    private final ThuongHieuService thuongHieuService;
    private final KichCoService kichCoService;
    private final MuiGiayService muiGiayService;
    private final CoGiayService coGiayService;
    private final SanPhamService sanPhamService;

    @GetMapping("/product")
    public String product(Model model) {
        List<SanPham> sp = sanPhamService.getAll();
        model.addAttribute("lstsanpham", sp);
        model.addAttribute("lstkichco", kichCoService.getAll());
        model.addAttribute("lstmausac", mauSacService.getAll());
        model.addAttribute("lsttheloai", theLoaiService.getAll());
        model.addAttribute("lstthuonghieu", thuongHieuService.getAll());
        return "cms/pages/products/products";
    }

    @GetMapping("/product-detail")
    public String productDetail(@RequestParam(name = "id", required = false) Long id, Model model) {
        model.addAttribute("lsttheloai", theLoaiService.getAll());
        model.addAttribute("lstchatlieu", chatLieuService.getAll());
        model.addAttribute("lstthuonghieu", thuongHieuService.getAll());
        model.addAttribute("lstmausac", mauSacService.getAll());
        model.addAttribute("lstdegiay", deGiayService.getAll());
        model.addAttribute("lstmuigiay", muiGiayService.getAll());
        model.addAttribute("lstcogiay", coGiayService.getAll());
        model.addAttribute("lstsanpham", sanPhamService.getAll());
        model.addAttribute("lstkichco", kichCoService.getAll());
        if (id != null) {
            SanPham sp = sanPhamService.getById(id);
            List<String> lst = sanPhamService.getListKichCo(id);
            if (sp==null){
                model.addAttribute("errorSP", 1);
            }else{
                model.addAttribute("sanPham", sp);
                model.addAttribute("errorSP", 0);
            }
            model.addAttribute("kichCo", lst);
        }
        return "cms/pages/products/add-product";
    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("lsttheloai", theLoaiService.getAll());
        model.addAttribute("lstchatlieu", chatLieuService.getAll());
        model.addAttribute("lstthuonghieu", thuongHieuService.getAll());
        model.addAttribute("lstmausac", mauSacService.getAll());
        model.addAttribute("lstdegiay", deGiayService.getAll());
        model.addAttribute("lstmuigiay", muiGiayService.getAll());
        model.addAttribute("lstcogiay", coGiayService.getAll());
        model.addAttribute("lstsanpham", sanPhamService.getAll());
        model.addAttribute("lstkichco", kichCoService.getAll());
        return "cms/pages/products/add-product";
    }
}
