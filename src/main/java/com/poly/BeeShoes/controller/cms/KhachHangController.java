package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.dto.DiaChiDto;
import com.poly.BeeShoes.model.DiaChi;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.request.KhachHangRequest;
import com.poly.BeeShoes.service.DiaChiService;
import com.poly.BeeShoes.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms/khach-hang")
public class KhachHangController {
    private final KhachHangService khachHangService;
    private final DiaChiService diaChiService;

    @GetMapping("")
    public String khachHang(Model model) {
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
        return "redirect:/cms/khach-hang";
    }

    @GetMapping("/view-addKH")
    public String viewAdd(Model model) {
        model.addAttribute("khachHang", new KhachHangRequest());
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listDC", diaChiService.getAll());
        return "cms/pages/users/add-khachHang";
    }

    @PostMapping("/add")
    public String addKH(@ModelAttribute("khachHang") KhachHangRequest khachHang, Model model) {
        model.addAttribute("khachHang", khachHang);
        KhachHang kh1 = new KhachHang();
        kh1.setHoTen(khachHang.getHoTen());
        kh1.setGioiTinh(khachHang.isGioiTinh());
        kh1.setNgaySinh(khachHang.getNgaySinh());
        kh1.setSdt(khachHang.getSdt());
        kh1.setNgayTao(Timestamp.from(Instant.now()));
        kh1.setMaKhachHang(khachHangService.generateCustomerCode());
        kh1.setTrangThai(khachHang.isTrangThai());
        KhachHang kh = khachHangService.add(kh1);
        DiaChi dc = new DiaChi();
        dc.setSoNha(khachHang.getSoNha());
        dc.setPhuongXa(khachHang.getPhuongXa());
        dc.setQuanHuyen(khachHang.getQuanHuyen());
        dc.setTinhThanhPho(khachHang.getTinhThanhPho());
        dc.setNgayTao(Timestamp.from(Instant.now()));
        dc.setKhachHang(kh);
        DiaChi diaChi = diaChiService.add(dc);
        kh.setDiaChiMacDinh(diaChi);
        khachHangService.add(kh);
        System.out.println(kh.toString());
        System.out.println(khachHang.toString());
        return "redirect:/cms/khach-hang";
    }

    @GetMapping("/detail/{id}")
    public String khachHangDetail(@PathVariable Long id, Model model) {
        KhachHang khachHang = khachHangService.detail(id);
        List<DiaChi> diaChiList = diaChiService.getByIdKhachHang(id);
        model.addAttribute("diaChiList", diaChiList);
        model.addAttribute("diaChiDto", new DiaChiDto());
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("listDC", khachHang.getDiaChi());
        return "cms/pages/users/detail-khachHang";
    }

    @PostMapping("/update/{id}/add-diachi")
    public String addDiaChi(
            @PathVariable("id") Long id,
            @ModelAttribute("diaChiDto") DiaChiDto diaChiDto
    ) {
        System.out.println(diaChiDto.toString());
        DiaChi diaChi = new DiaChi();
        diaChi.setSoNha(diaChiDto.getSoNhaDto());
        diaChi.setPhuongXa(diaChiDto.getPhuongXaDto());
        diaChi.setQuanHuyen(diaChiDto.getQuanHuyenDto());
        diaChi.setTinhThanhPho(diaChiDto.getTinhThanhPhoDto());
        diaChi.setKhachHang(khachHangService.detail(id));
        diaChiService.add(diaChi);
        return "redirect:/cms/khach-hang";
    }

    @PostMapping("/update/{id}")
    public String updateKH(
            @PathVariable Long id, Model model,
            @ModelAttribute("khachHang") KhachHang khachHang
    ) {
        KhachHang kh1 = khachHangService.detail(id);
        kh1.setHoTen(khachHang.getHoTen());
        kh1.setGioiTinh(khachHang.isGioiTinh());
        kh1.setNgaySinh(khachHang.getNgaySinh());
        kh1.setSdt(khachHang.getSdt());
        kh1.setTrangThai(khachHang.isTrangThai());
        KhachHang kh = khachHangService.update(kh1, id);

//        DiaChi dc = diaChiService.detail(kh.getDiaChiMacDinh().getId());
//        dc.setSoNha(khachHang.getDiaChiMacDinh().getSoNha());
//        dc.setPhuongXa(khachHang.getDiaChiMacDinh().getPhuongXa());
//        dc.setQuanHuyen(khachHang.getDiaChiMacDinh().getQuanHuyen());
//        dc.setTinhThanhPho(khachHang.getDiaChiMacDinh().getTinhThanhPho());
//        DiaChi diaChi = diaChiService.update(dc, kh1.getDiaChiMacDinh().getId());
//        kh.setDiaChiMacDinh(dc);
        khachHangService.update(kh,id);

        return "redirect:/cms/khach-hang";
    }

}
