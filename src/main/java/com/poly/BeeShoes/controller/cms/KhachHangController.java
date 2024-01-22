package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.DiaChi;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.model.NhanVien;
import com.poly.BeeShoes.service.DiaChiService;
import com.poly.BeeShoes.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class KhachHangController {
    private final KhachHangService khachHangService;
    private final DiaChiService diaChiService;

    @GetMapping("/khachHang")
    public String khachHang(Model model){
        List<KhachHang> kh = khachHangService.getAll();
        List<DiaChi> dc = diaChiService.getAll();
        model.addAttribute("listKH", kh);
        model.addAttribute("listDC", dc);
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("diaChi", new DiaChi());
        return "cms/pages/users/khachHang";
    }

    @GetMapping("/deleteKH/{id}")
    public String deleteKH(@PathVariable Long id) {
        khachHangService.delete(id);
        return "redirect:/cms/khachHang";
    }

    @GetMapping("/view-addKH")
    public String viewAdd(Model model) {
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("diaChi", new DiaChi());
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listDC", diaChiService.getAll());
        return "cms/pages/users/add-khachHang";
    }
    @PostMapping("/add-diaChi")
    public String addDiaChi(@ModelAttribute("diaChi") DiaChi diaChi, Model model) {
        model.addAttribute("diaChi", diaChi);
        diaChiService.add(diaChi);
        return "redirect:/cms/khachHang";
    }

    @PostMapping("/add-khachHang")
    public void addKH(@ModelAttribute("khachHang") KhachHang khachHang, Model model) {
        model.addAttribute("khachHang", khachHang);
        khachHangService.generateCustomerCode();
        khachHangService.add(khachHang);
//        return "redirect:/cms/nhanVien";
    }

    @GetMapping("/khachHang-detail")
    public String khachHangDetail() {
        return "cms/pages/users/customer-detail";
    }
}
