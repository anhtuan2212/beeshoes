package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.NhanVien;
import com.poly.BeeShoes.service.ChucVuService;
import com.poly.BeeShoes.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class NhanVienController {
    private final NhanVienService nhanVienService;
    private final ChucVuService chucVuService;

    @GetMapping("/nhanVien")
    public String nhanVien(Model model) {
        List<NhanVien> nv = nhanVienService.getAll();
        model.addAttribute("listCV", chucVuService.getAll());
        model.addAttribute("listNV", nv);
        model.addAttribute("nhanVien", new NhanVien());
        return "cms/pages/users/nhanVien";
    }

    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        model.addAttribute("nhanVien", new NhanVien());
        model.addAttribute("listCV", chucVuService.getAll());
        return "cms/pages/users/add-nhanVien";
    }

    @PostMapping("/add-nhanVien")
    public String addNV(@ModelAttribute("nhanVien") NhanVien nhanVien, Model model) {
        model.addAttribute("nhanVien", nhanVien);
        nhanVienService.generateEmployeeCode();
        nhanVienService.add(nhanVien);
        return "redirect:/cms/nhanVien";
    }

    @GetMapping("/view-detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model){
        NhanVien nhanVien = nhanVienService.detail(id);
        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("listCV",chucVuService.getAll());
        return "cms/pages/users/detail-nhanVien";
    }

    @PostMapping("/update-nhanVien/{id}")
    public String updateNV(@PathVariable Long id, Model model,
                           @ModelAttribute("nhanVien") NhanVien nhanVien){
        nhanVienService.update(nhanVien, id);
        return "redirect:/cms/nhanVien";
    }

    @GetMapping("/delete/{id}")
    public String deleteNV(@PathVariable Long id) {
        nhanVienService.delete(id);
        return "redirect:/cms/nhanVien";
    }
}
