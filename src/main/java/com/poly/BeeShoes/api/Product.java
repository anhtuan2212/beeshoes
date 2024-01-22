package com.poly.BeeShoes.api;

import com.google.gson.Gson;
import com.poly.BeeShoes.dto.CTSPRequest;
import com.poly.BeeShoes.dto.ProductDetailVersion;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Product {
    private final TheLoaiService theLoaiService;
    private final ChatLieuService chatLieuService;
    private final DeGiayService deGiayService;
    private final MauSacService mauSacService;
    private final ThuongHieuService thuongHieuService;
    private final KichCoService kichCoService;
    private final AnhService anhService;
    private final MuiGiayService muiGiayService;
    private final CoGiayService coGiayService;
    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;

    @PostMapping("/them-san-pham")
    public ResponseEntity<SanPham> themSanPham(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        SanPham sanPham = new SanPham();
        sanPham.setNgayTao(Timestamp.from(Instant.now()));
        sanPham.setTrangThai(false);
        sanPham.setTen(ten);
        SanPham sp = sanPhamService.save(sanPham);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-the-loai")
    public ResponseEntity<TheLoai> themTheLoai(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        TheLoai theLoai = new TheLoai();
        theLoai.setNgayTao(Timestamp.from(Instant.now()));
        theLoai.setNgaySua(Timestamp.from(Instant.now()));
        theLoai.setTrangThai(true);
        theLoai.setTen(ten);
        TheLoai sp = theLoaiService.save(theLoai);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-thuong-hieu")
    public ResponseEntity<ThuongHieu> themThuongHieu(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        ThuongHieu th = new ThuongHieu();
        th.setNgayTao(Timestamp.from(Instant.now()));
        th.setNgaySua(Timestamp.from(Instant.now()));
        th.setTrangThai(true);
        th.setTen(ten);
        ThuongHieu sp = thuongHieuService.save(th);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-chat-lieu")
    public ResponseEntity<ChatLieu> themChatLieu(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        ChatLieu th = new ChatLieu();
        th.setNgayTao(Timestamp.from(Instant.now()));
        th.setNgaySua(Timestamp.from(Instant.now()));
        th.setTrangThai(true);
        th.setTen(ten);
        ChatLieu sp = chatLieuService.save(th);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-de-giay")
    public ResponseEntity<DeGiay> themDeGiay(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        DeGiay dg = new DeGiay();
        dg.setNgayTao(Timestamp.from(Instant.now()));
        dg.setNgaySua(Timestamp.from(Instant.now()));
        dg.setTrangThai(true);
        dg.setTen(ten);
        DeGiay sp = deGiayService.save(dg);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-co-giay")
    public ResponseEntity<CoGiay> themCoGiay(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        CoGiay cg = new CoGiay();
        cg.setNgayTao(Timestamp.from(Instant.now()));
        cg.setNgaySua(Timestamp.from(Instant.now()));
        cg.setTrangThai(true);
        cg.setTen(ten);
        CoGiay sp = coGiayService.save(cg);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-mui-giay")
    public ResponseEntity<MuiGiay> themMuiGiay(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        MuiGiay cg = new MuiGiay();
        cg.setNgayTao(Timestamp.from(Instant.now()));
        cg.setNgaySua(Timestamp.from(Instant.now()));
        cg.setTrangThai(true);
        cg.setTen(ten);
        MuiGiay sp = muiGiayService.save(cg);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-kich-co")
    public ResponseEntity<KichCo> kichCo(@RequestParam("ten") String ten) {
        if (kichCoService.exitsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "true").body(null);
        }
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "error").body(null);
        }
        KichCo cg = new KichCo();
        cg.setNgayTao(Timestamp.from(Instant.now()));
        cg.setNgaySua(Timestamp.from(Instant.now()));
        cg.setTrangThai(true);
        cg.setTen(ten);
        KichCo sp = kichCoService.save(cg);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "false").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-mau-sac")
    public ResponseEntity<MauSac> themMauSac(@RequestParam("ten_mau") String ten, @RequestParam("ma_mau") String ma) {
        boolean st = mauSacService.existsByMaMauSac(ma);
        if (st) {
            return ResponseEntity.status(HttpStatus.OK).header("status-cus", "true").body(null);
        }
        MauSac ms = new MauSac();
        ms.setNgayTao(Timestamp.from(Instant.now()));
        ms.setNgaySua(Timestamp.from(Instant.now()));
        ms.setTrangThai(true);
        ms.setTen(ten);
        ms.setMaMauSac(ma);
        MauSac sp = mauSacService.save(ms);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status-cus", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/chi-tiet-san-pham")
    public ResponseEntity<SanPham> chiTietSanPham(@ModelAttribute CTSPRequest ctspRequest) {
        Gson gs = new Gson();
        String[] array = gs.fromJson(ctspRequest.getImgSelected(), String[].class);
        Type listType = new TypeToken<List<ProductDetailVersion>>() {
        }.getType();
        List<ProductDetailVersion> productdetail = gs.fromJson(ctspRequest.getProduct_details(), listType);

        SanPham sp = sanPhamService.getById(ctspRequest.getSanPham());
        ChatLieu cl = chatLieuService.getById(ctspRequest.getChatLieu());
        ThuongHieu th = thuongHieuService.getById(ctspRequest.getThuongHieu());
        TheLoai tl = theLoaiService.getById(ctspRequest.getTheLoai());
        DeGiay dg = deGiayService.getById(ctspRequest.getDeGiay());
        CoGiay cg = coGiayService.getById(ctspRequest.getCoGiay());
        MuiGiay mg = muiGiayService.getById(ctspRequest.getMuiGiay());
        sp.setThuongHieu(th);
        sp.setTrangThai(ctspRequest.isTrangThai());
        sp.setTheLoai(tl);
        sp.setMoTa(ctspRequest.getMoTa());
        SanPham sanPham = sanPhamService.save(sp);
        boolean st = anhService.saveAnhSanPham(sanPham, array);
        if (st) {
            for (ProductDetailVersion pro : productdetail) {
                if (pro.getId() != 0) {
                    ChiTietSanPham ctsp = chiTietSanPhamService.getById(pro.getId());
                    KichCo kc = kichCoService.getByTen(pro.getKichCo());
                    ctsp.setChatLieu(cl);
                    ctsp.setDeGiay(dg);
                    ctsp.setCoGiay(cg);
                    ctsp.setMuiGiay(mg);
                    ctsp.setSale(ctspRequest.isSales());
                    ctsp.setTrangThai(1);
                    ctsp.setSanPham(sp);
                    ctsp.setKichCo(kc);
                    ctsp.setGiaGoc(LibService.convertStringToBigDecimal(ctspRequest.getGiaGoc()));
                    ctsp.setGiaNhap(LibService.convertStringToBigDecimal(ctspRequest.getGiaNhap()));
                    ctsp.setGiaBan(LibService.convertStringToBigDecimal(pro.getGiaBan()));
                    ctsp.setMaSanPham(chiTietSanPhamService.generateDetailCode());
                    ctsp.setSoLuongNhap(pro.getSoLuong());
                    ctsp.setSoLuongTon(pro.getSoLuong());
                    ctsp.setTrangThai(1);
                    ctsp.setNgayTao(Timestamp.from(Instant.now()));
                    ctsp.setMauSac(mauSacService.getMauSacByMa(pro.getMauSac()));
                    chiTietSanPhamService.save(ctsp);
                    System.out.println(ctspRequest.toString());
                } else {
                    KichCo kc = kichCoService.getByTen(pro.getKichCo());
//                    ChiTietSanPham ct = chiTietSanPhamService.getBySizeAndColor(kc, mauSacService.getMauSacByMa(pro.getMauSac()));
                    ChiTietSanPham ctsp = new ChiTietSanPham();
//                    if (ct != null) {
//                        ctsp = ct;
//                    }
                    ctsp.setChatLieu(cl);
                    ctsp.setDeGiay(dg);
                    ctsp.setCoGiay(cg);
                    ctsp.setMuiGiay(mg);
                    ctsp.setSale(ctspRequest.isSales());
                    ctsp.setTrangThai(1);
                    ctsp.setSanPham(sp);
                    ctsp.setKichCo(kc);
                    ctsp.setGiaGoc(LibService.convertStringToBigDecimal(ctspRequest.getGiaGoc()));
                    ctsp.setGiaNhap(LibService.convertStringToBigDecimal(ctspRequest.getGiaNhap()));
                    ctsp.setGiaBan(LibService.convertStringToBigDecimal(pro.getGiaBan()));
                    ctsp.setMaSanPham(chiTietSanPhamService.generateDetailCode());
                    ctsp.setSoLuongNhap(pro.getSoLuong());
                    ctsp.setSoLuongTon(pro.getSoLuong());
                    ctsp.setTrangThai(1);
                    ctsp.setNgayTao(Timestamp.from(Instant.now()));
                    ctsp.setMauSac(mauSacService.getMauSacByMa(pro.getMauSac()));
                    chiTietSanPhamService.save(ctsp);
                }
            }
//            System.out.println(ctspRequest.toString());
//            SanPham spres = new SanPham();
//            spres.setId(sanPham.getId());
            return ResponseEntity.status(HttpStatus.OK).header("status", "1").body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
