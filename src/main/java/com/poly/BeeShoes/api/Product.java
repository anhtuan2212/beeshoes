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
import java.util.ArrayList;
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
    private final KichCoService kichCoService;
    private final ThuongHieuService thuongHieuService;
    private final AnhService anhService;
    private final MuiGiayService muiGiayService;
    private final CoGiayService coGiayService;
    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final TagsService tagsService;
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
        System.out.println(ctspRequest.getTags());
        List<ProductDetailVersion> productdetail = gs.fromJson(ctspRequest.getProduct_details(), listType);
        if (ctspRequest.getTenSanPham().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "NameProNull").body(null);
        }
        if (productdetail.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "OptionNull").body(null);
        }
        for (int i = 0; i < productdetail.size(); i++) {
            if (productdetail.get(i).getKichCo().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "SizeNull").body(null);
            }
            if (productdetail.get(i).getMaMauSac().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "ColorNull").body(null);
            }
            if (productdetail.get(i).getGiaBan().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "GiaBanNull").body(null);
            }
            if (productdetail.get(i).getGiaGoc().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "GiaGocNull").body(null);
            }
            if (productdetail.get(i).getSoLuong() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "QuantityNull").body(null);
            }
            if (productdetail.get(i).getImg() == null || productdetail.get(i).getImg() == "/assets/cms/img/400x400/img2.jpg") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "IMGNull").body(null);
            }
            if (ctspRequest.getMoTa().length() > 2000) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "MaxLenghtMota").body(null);
            }

        }
        SanPham sp = sanPhamService.getById(ctspRequest.getSanPham());
        sp.setTen(ctspRequest.getTenSanPham());

        List<String> lstTags = ctspRequest.getTags();
        List<Tags> lst = new ArrayList<>();

        if (lstTags != null) {
            for (String tag : lstTags) {
                String name = LibService.convertNameTags(tag);
                Tags tags = tagsService.getByTen(name);
                if (tags == null) {
                    tags = new Tags();
                    tags.setTen(name);
                    tags.setTrangThai(true);
                    tags.setNgayTao(Timestamp.from(Instant.now()));
                    tags.setNgaySua(Timestamp.from(Instant.now()));
                    tags = tagsService.save(tags);
                }
                lst.add(tags);
            }
        }
        sp.setTags(lst);
        sp = sanPhamService.save(sp);
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
        sp.setTrangThai(true);
        sp.setSale(ctspRequest.isSales());
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
            if (anhC != null) {
                anh = anhC;
            } else {
                anh.setUrl(productdetail.get(i).getImg());
                anh.setSanPham(sanPham);
                anh.setNgayTao(Timestamp.from(Instant.now()));
                anh.setTrangThai(true);
            }
            if (i == 0) {
                if (sanPham.getMainImage() == null) {
                    anh.setMain(true);
                }
            } else {
                if (anh.isMain() != true) {
                    anh.setMain(false);
                }
            }
            if (anh.getId() == null) {
                anh = anhService.save(anh);
            }
            System.out.println(anh);
            System.out.println(i);
            ctsp.setAnh(anh);
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
            ctsp.setSoLuongTon(productdetail.get(i).getSoLuong());
            ctsp.setTrangThai(1);
            ctsp.setNgayTao(Timestamp.from(Instant.now()));
            ctsp.setMauSac(ms);
            chiTietSanPhamService.save(ctsp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/xoa-chi-tiet-san-pham")
    public ResponseEntity<SanPham> DeleteCtsp(@RequestParam("id") String id, @RequestParam("color") String color, @RequestParam("size") String size) {
        if (id.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "IdNull").body(null);
        }
        ChiTietSanPham chiTietSanPham = null;
        if (!LibService.containsAlphabetic(id)) {
            chiTietSanPham = chiTietSanPhamService.getById(Long.parseLong(id));
        }
        if (LibService.containsAlphabetic(id)) {
            MauSac mauSac = mauSacService.getMauSacByMa(color);
            KichCo kichCo = kichCoService.getByTen(size);
            chiTietSanPham = chiTietSanPhamService.getBySizeAndColor(kichCo, mauSac);
            if (chiTietSanPham == null) {
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        }
        int num = chiTietSanPham.getSanPham().getChiTietSanPham().size();
        if (num == 1) {
            chiTietSanPham.getSanPham().setTrangThai(false);
            sanPhamService.save(chiTietSanPham.getSanPham());
        }
        boolean st = chiTietSanPhamService.delete(chiTietSanPham.getId());
        if (st) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "NotExits").body(null);
        }
    }
}
