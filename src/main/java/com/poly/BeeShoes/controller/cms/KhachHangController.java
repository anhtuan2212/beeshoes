package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.dto.DiaChiDto;
import com.poly.BeeShoes.model.DiaChi;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.request.KhachHangRequest;
import com.poly.BeeShoes.service.DiaChiService;
import com.poly.BeeShoes.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return "redirect:/cms/khach-hang";
    }

    @GetMapping("/detail/{id}")
    public String khachHangDetail(@PathVariable Long id, Model model) {
        KhachHang khachHang = khachHangService.detail(id);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("listDC", khachHang.getDiaChi());
        return "cms/pages/users/detail-khachHang";
    }

    @PostMapping("/update/add-diachi")
    public ResponseEntity addDiaChi(
            @ModelAttribute("diaChiDto") DiaChiDto diaChiDto
    ) {
        if(diaChiDto.getSoNhaDto().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status","soNhaNull").body(null);
        }
        if(diaChiDto.getPhuongXaDto().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status","tinhTPNull").body(null);
        }
        if(diaChiDto.getQuanHuyenDto().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status","quanHuyenNull").body(null);
        }
        if(diaChiDto.getTinhThanhPhoDto().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status","phuongXaNull").body(null);
        }
        KhachHang khachHang = khachHangService.detail(diaChiDto.getIdKH());

        if(khachHang.getDiaChi().size() > 4){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status","maxAddress").body(null);
        }
        DiaChi diaChi = new DiaChi();
        diaChi.setSoNha(diaChiDto.getSoNhaDto());
        diaChi.setPhuongXa(diaChiDto.getPhuongXaDto());
        diaChi.setQuanHuyen(diaChiDto.getQuanHuyenDto());
        diaChi.setTinhThanhPho(diaChiDto.getTinhThanhPhoDto());
        diaChi.setKhachHang(khachHangService.detail(diaChiDto.getIdKH()));
        DiaChi diaChi1 = diaChiService.add(diaChi);
        diaChi1.setKhachHang(null);

        if (diaChi1.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(diaChi1);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(diaChi1);
    }

    @PostMapping("/set-default-address")
    public ResponseEntity setAddess(@RequestParam("idDiaChi") Long idDiaChi,
                                    @RequestParam("idKhachHang") Long idKhachHang){
        DiaChi diaChi = diaChiService.detail(idDiaChi);
        KhachHang khachHang = khachHangService.detail(idKhachHang);
        khachHang.setDiaChiMacDinh(diaChi);
        khachHangService.add(khachHang);
        return ResponseEntity.status(HttpStatus.OK).body(null);
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
