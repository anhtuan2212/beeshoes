package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.NhanVien;
import com.poly.BeeShoes.service.ChucVuService;
import com.poly.BeeShoes.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms/nhan-vien")
public class NhanVienController {
    private final NhanVienService nhanVienService;
    private final ChucVuService chucVuService;

    @GetMapping("/")
    public String nhanVien(Model model){
        List<NhanVien> nv = nhanVienService.getAll();
        model.addAttribute("listCV", chucVuService.getAll());
        model.addAttribute("listNV", nv);
        return "cms/pages/users/nhanVien";
    }

    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        model.addAttribute("nhanVien", new NhanVien());
        model.addAttribute("listCV", chucVuService.getAll());
        return "cms/pages/users/add-nhanVien";
    }

    @PostMapping("/add")
    public String addNV(@ModelAttribute("nhanVien") NhanVien nhanVien, Model model) {
        model.addAttribute("nhanVien", nhanVien);
        nhanVien.setMaNhanVien(nhanVienService.generateEmployeeCode());
        nhanVienService.add(nhanVien);
        model.addAttribute("thongbao", "Thêm thành công!");
        return "redirect:/cms/nhanVien";
    }

    @GetMapping("/view-detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model){
        NhanVien nhanVien = nhanVienService.detail(id);
        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("listCV",chucVuService.getAll());
        return "cms/pages/users/detail-nhanVien";
    }

    @PostMapping("/update/{id}")
    public String updateNV(@PathVariable Long id, Model model,
                           @ModelAttribute("nhanVien") NhanVien nhanVien){
        nhanVienService.update(nhanVien, id);
        model.addAttribute("thongbao", "Cập nhật thành công!");
        return "redirect:/cms/nhanVien";
    }

    @GetMapping("/delete/{id}")
    public String deleteNV(@PathVariable Long id, Model model) {
        nhanVienService.delete(id);
        model.addAttribute("thongbao", "Xóa thành công!");
        return "redirect:/cms/nhanVien";
    }
}
