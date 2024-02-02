package com.poly.BeeShoes.api;

import com.google.gson.Gson;
import com.poly.BeeShoes.request.CTSPRequest;
import com.poly.BeeShoes.request.ProductDetailVersion;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    Gson gs = new Gson();

    @PostMapping("/them-san-pham")
    public ResponseEntity<SanPham> themSanPham(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (sanPhamService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
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



    @PostMapping("/chi-tiet-san-pham")
    public ResponseEntity<SanPham> chiTietSanPham(@ModelAttribute CTSPRequest ctspRequest) {

        Type listType = new TypeToken<List<ProductDetailVersion>>() {
        }.getType();
        List<ProductDetailVersion> productdetail = gs.fromJson(ctspRequest.getProduct_details(), listType);
        if (productdetail.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Option product can't null").body(null);
        }
        for (int i = 0; i < productdetail.size(); i++) {
            if (productdetail.get(i).getKichCo().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Size in option product can't null").body(null);
            }
            if (productdetail.get(i).getMaMauSac().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Color in option product can't null").body(null);
            }
            if (productdetail.get(i).getGiaBan().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Price in option product can't null").body(null);
            }
            if (productdetail.get(i).getSoLuong() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Quantity in option product can't null").body(null);
            }
            if (productdetail.get(i).getImg() == null||productdetail.get(i).getImg() == "/assets/cms/img/400x400/img2.jpg") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "IMG in option product can't null").body(null);
            }

        }
        SanPham sp = sanPhamService.getById(ctspRequest.getSanPham());
        ChatLieu cl = chatLieuService.getById(ctspRequest.getChatLieu());
        ThuongHieu th = thuongHieuService.getById(ctspRequest.getThuongHieu());
        TheLoai tl = theLoaiService.getById(ctspRequest.getTheLoai());
        DeGiay dg = deGiayService.getById(ctspRequest.getDeGiay());
        CoGiay cg = coGiayService.getById(ctspRequest.getCoGiay());
        MuiGiay mg = muiGiayService.getById(ctspRequest.getMuiGiay());
        if (sp == null || cl == null || th == null || tl == null || dg == null || cg == null || mg == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        sp.setThuongHieu(th);
        sp.setTrangThai(ctspRequest.isTrangThai());
        sp.setTheLoai(tl);
        sp.setMoTa(ctspRequest.getMoTa());
        SanPham sanPham = sanPhamService.save(sp);
        for (int i = 0; i < productdetail.size(); i++) {
            System.out.println(i);
            KichCo kc = kichCoService.getByTen(productdetail.get(i).getKichCo());
            MauSac ms = mauSacService.getMauSacByMa(productdetail.get(i).getMaMauSac());
            ChiTietSanPham ct = chiTietSanPhamService.getBySizeAndColorAndProduct(kc, ms, sanPham);
            ChiTietSanPham ctsp = new ChiTietSanPham();
            if (ct != null) {
                ctsp = ct;
            }
            Anh anhC = anhService.getAnhByURL(productdetail.get(i).getImg());
            Anh anh = new Anh();
            if (anhC!=null){
                anh=anhC;
            }else{
                anh.setUrl(productdetail.get(i).getImg());
                anh.setSanPham(sanPham);
                anh.setNgayTao(Timestamp.from(Instant.now()));
                anh.setTrangThai(true);
            }
            if (i==1){
                anh.setMain(true);
            }else{
                anh.setMain(false);
            }
            Anh a = anhService.save(anh);
            ctsp.setAnh(a);
            ctsp.setChatLieu(cl);
            ctsp.setDeGiay(dg);
            ctsp.setCoGiay(cg);
            ctsp.setMuiGiay(mg);
            ctsp.setSale(ctspRequest.isSales());
            ctsp.setTrangThai(1);
            ctsp.setSanPham(sp);
            ctsp.setKichCo(kc);
            ctsp.setGiaGoc(LibService.convertStringToBigDecimal(productdetail.get(i).getGiaGoc()));
            ctsp.setGiaNhap(LibService.convertStringToBigDecimal("0"));
            ctsp.setGiaBan(LibService.convertStringToBigDecimal(productdetail.get(i).getGiaBan()));
            ctsp.setMaSanPham(chiTietSanPhamService.generateDetailCode());
            ctsp.setSoLuongNhap(productdetail.get(i).getSoLuong());//tạm bỏ qua
            ctsp.setSoLuongTon(productdetail.get(i).getSoLuong());
            ctsp.setTrangThai(1);
            ctsp.setNgayTao(Timestamp.from(Instant.now()));
            ctsp.setMauSac(ms);
            chiTietSanPhamService.save(ctsp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
