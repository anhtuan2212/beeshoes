package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.QuanTri;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.service.QuanTriService;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms/quan-tri")
public class QuanTriController {
    private final QuanTriService quanTriService;
    private final SanPhamService sanPhamService;

    @GetMapping("")
    public String quanTri(Model model) {
        QuanTri qtri = quanTriService.getById(1L);
        model.addAttribute("quanTri", qtri);
        model.addAttribute("listSP", sanPhamService.findByTrangThaiEquals(true));
        return "cms/pages/quantri/quan-tri";
    }

    @PostMapping("/update")
    public String update(Model model,@ModelAttribute("quanTri") QuanTri quanTri){
        System.out.println(quanTri.toString());
        QuanTri updatedQuanTri = quanTriService.getById(1L);
        updatedQuanTri.setBanner1(quanTri.getBanner1());
        updatedQuanTri.setTitle1(quanTri.getTitle1());
        updatedQuanTri.setMo_ta1(quanTri.getMo_ta1());
        updatedQuanTri.setBanner2(quanTri.getBanner2());
        updatedQuanTri.setTitle2(quanTri.getTitle2());
        updatedQuanTri.setMo_ta2(quanTri.getMo_ta2());
        updatedQuanTri.setSan_pham1(quanTri.getSan_pham1());
        updatedQuanTri.setTitle_sp1(quanTri.getTitle_sp1());
        updatedQuanTri.setSan_pham2(quanTri.getSan_pham2());
        updatedQuanTri.setTitle_sp2(quanTri.getTitle_sp2());
        updatedQuanTri.setSan_pham3(quanTri.getSan_pham3());
        updatedQuanTri.setTitle_sp3(quanTri.getTitle_sp3());
        updatedQuanTri.setSan_pham_sale(quanTri.getSan_pham_sale());
        updatedQuanTri.setTitle_sp_sale(quanTri.getTitle_sp_sale());
        updatedQuanTri.setThoi_gian_sale(quanTri.getThoi_gian_sale());
        quanTriService.update(updatedQuanTri, 1L);
        return "redirect:/cms/quan-tri";
    }
}
