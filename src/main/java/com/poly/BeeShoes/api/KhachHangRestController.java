package com.poly.BeeShoes.api;

import com.poly.BeeShoes.model.DiaChi;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
@RequiredArgsConstructor
public class KhachHangRestController {
    private final KhachHangService khachHangService;

    @GetMapping("get-address-by-customer")
    public ResponseEntity getAddress(@RequestParam("id") Long id) {
        KhachHang khachHang = khachHangService.detail(id);
        if (khachHang == null) {
            return ResponseEntity.notFound().build();
        }
        List<DiaChi> dc = khachHang.getDiaChi();
        dc.forEach(addd -> addd.setKhachHang(null));
        return ResponseEntity.ok().body(dc);
    }
}
