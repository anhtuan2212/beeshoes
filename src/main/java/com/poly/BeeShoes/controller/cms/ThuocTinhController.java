package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.model.TheLoai;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class ThuocTinhController {
    private final TheLoaiService theLoaiService;
//    private final ChatLieuService chatLieuService;
//    private final DeGiayService deGiayService;
//    private final MauSacService mauSacService;
//    private final ThuongHieuService thuongHieuService;
//    private final KichCoService kichCoService;
//    private final MuiGiayService muiGiayService;
//    private final CoGiayService coGiayService;
@GetMapping("/shoe-category")
public String theLoai(Model model) {
    List<TheLoai> lst = theLoaiService.getAll();
    System.out.println(lst.get(0).getNguoiTao().getNhanVien().getMaNhanVien());
    model.addAttribute("lsttheloai",lst);
    return "cms/pages/products/shoes-cate";
}
}
