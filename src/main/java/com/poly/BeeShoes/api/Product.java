package com.poly.BeeShoes.api;

import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.model.TheLoai;
import com.poly.BeeShoes.service.SanPhamService;
import com.poly.BeeShoes.service.TheLoaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Product {
    private final SanPhamService sanPhamService;
    private final TheLoaiService theLoaiService;

    @PostMapping("/add-product")
    public ResponseEntity<SanPham> themSanPham(@RequestParam("ten") String ten) {
        SanPham sanPham = new SanPham();
        sanPham.setNgayTao(Timestamp.from(Instant.now()));
        sanPham.setTrangThai(true);
        sanPham.setTen(ten);
        SanPham sp = sanPhamService.save(sanPham);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status","oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status","error").body(sp);
    }
    @PostMapping("/add-category")
    public ResponseEntity<TheLoai> themTheLoai(@RequestParam("ten") String ten) {
        TheLoai theLoai = new TheLoai();
        theLoai.setNgayTao(Timestamp.from(Instant.now()));
        theLoai.setNgaySua(Timestamp.from(Instant.now()));
        theLoai.setTrangThai(true);
        theLoai.setTenTheLoai(ten);
        TheLoai sp = theLoaiService.save(theLoai);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status","oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status","error").body(sp);
    }
}
