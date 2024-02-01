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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

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

    @PostMapping("/them-the-loai")
    public ResponseEntity<TheLoai> themTheLoai(@RequestParam("ten") String ten, @RequestParam(value = "id", required = false) Long id) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (theLoaiService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
        }
        TheLoai theLoai = new TheLoai();
        if (id != null) {
            theLoai = theLoaiService.getById(id);
            theLoai.setNgaySua(Timestamp.from(Instant.now()));
        } else {
            theLoai.setNgayTao(Timestamp.from(Instant.now()));
            theLoai.setNgaySua(Timestamp.from(Instant.now()));
        }
        theLoai.setTrangThai(true);
        theLoai.setTen(ten);
        TheLoai sp = theLoaiService.save(theLoai);
        if (sp.getNguoiTao() != null) {
            sp.setCreate(sp.getNguoiTao().getNhanVien().getMaNhanVien());
        } else {
            sp.setCreate("N/A");
        }
        if (sp.getNguoiSua() != null) {
            sp.setUpdate(sp.getNguoiSua().getNhanVien().getMaNhanVien());
        } else {
            sp.setUpdate("N/A");
        }
        sp.setNguoiTao(null);
        sp.setNguoiSua(null);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(null);
    }

    @DeleteMapping("/xoa-the-loai")
    public ResponseEntity xoaTheLoai(@RequestParam(value = "id") Long id) {
        boolean st = false;
        TheLoai tl = theLoaiService.getById(id);
        if (sanPhamService.exitsByTheLoai(tl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "constraint").body(null);
        }
        if (id != null) {
            st = theLoaiService.delete(id);
        }
        if (st) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "success").body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(null);
    }

    @DeleteMapping("/xoa-chat-lieu")
    public ResponseEntity xoaChatLieu(@RequestParam(value = "id") Long id) {
        boolean st = false;
        ChatLieu cl = chatLieuService.getById(id);
        if (chiTietSanPhamService.existsByChatLieu(cl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "constraint").body(null);
        }
        if (id != null) {
            st = chatLieuService.delete(id);
        }
        if (st) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "success").body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(null);
    }

    @DeleteMapping("/xoa-thuong-hieu")
    public ResponseEntity xoaThuongHieu(@RequestParam(value = "id") Long id) {
        boolean st = false;
        ThuongHieu cl = thuongHieuService.getById(id);
        if (sanPhamService.exitsByThuongHieu(cl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "constraint").body(null);
        }
        if (id != null) {
            st = thuongHieuService.delete(id);
        }
        if (st) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "success").body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(null);
    }

    @PostMapping("/them-thuong-hieu")
    public ResponseEntity<ThuongHieu> themThuongHieu(@RequestParam("ten") String ten, @RequestParam(value = "id", required = false) Long id) {
        ThuongHieu th = new ThuongHieu();
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (thuongHieuService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
        }
        if (id != null) {
            th = thuongHieuService.getById(id);
            th.setNgaySua(Timestamp.from(Instant.now()));
        } else {
            th.setNgaySua(Timestamp.from(Instant.now()));
            th.setNgayTao(Timestamp.from(Instant.now()));
        }
        th.setTrangThai(true);
        th.setTen(ten);
        ThuongHieu sp = thuongHieuService.save(th);
        sp.setNguoiTao(null);
        sp.setNgaySua(null);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-chat-lieu")
    public ResponseEntity<ChatLieu> themChatLieu(@RequestParam("ten") String ten, @RequestParam(value = "id", required = false) Long id) {
        ChatLieu th = new ChatLieu();
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (chatLieuService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
        }
        if (id != null) {
            th = chatLieuService.getById(id);
            th.setNgaySua(Timestamp.from(Instant.now()));
        } else {
            th.setNgaySua(Timestamp.from(Instant.now()));
            th.setNgayTao(Timestamp.from(Instant.now()));
        }
        th.setTrangThai(true);
        th.setTen(ten);
        ChatLieu sp = chatLieuService.save(th);
        if (sp.getNguoiTao() != null) {
            sp.setCreate(sp.getNguoiTao().getNhanVien().getMaNhanVien());
        } else {
            sp.setCreate("N/A");
        }
        if (sp.getNguoiSua() != null) {
            sp.setUpdate(sp.getNguoiSua().getNhanVien().getMaNhanVien());
        } else {
            sp.setUpdate("N/A");
        }
        sp.setNguoiTao(null);
        sp.setNguoiSua(null);
        if (sp.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "oke").body(sp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("status", "error").body(sp);
    }

    @PostMapping("/them-de-giay")
    public ResponseEntity<DeGiay> themDeGiay(@RequestParam("ten") String ten) {
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (deGiayService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (coGiayService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (muiGiayService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
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
        if (Integer.parseInt(ten) < 20 || Integer.parseInt(ten) > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameSize").body(null);
        }
        if (ten.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "nameNull").body(null);
        }
        if (kichCoService.exitsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.OK).header("status", "existsByTen").body(null);
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
        if (mauSacService.existsByMaMauSac(ma)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByMa").body(null);
        }
        if (mauSacService.existsByTen(ten)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "existsByTen").body(null);
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
