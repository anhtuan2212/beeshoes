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
        Type listType = new TypeToken<List<ProductDetailVersion>>() {
        }.getType();
        List<ProductDetailVersion> productdetail = gs.fromJson(ctspRequest.getProduct_details(), listType);
        if (productdetail.size()==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","Option product can't null").body(null);
        }
        String mau ="";
        for (int i=0;i< productdetail.size();i++){
            if (productdetail.get(i).getKichCo().isBlank()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","Size in option product can't null").body(null);
            }
            if (productdetail.get(i).getMauSac().isBlank()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","Color in option product can't null").body(null);
            }
            if (productdetail.get(i).getGiaBan().isBlank()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","Price in option product can't null").body(null);
            }
            if (productdetail.get(i).getSoLuong()<0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","Quantity in option product can't null").body(null);
            }
            if(i==0){
                mau = productdetail.get(i).getMauSac();
                if (productdetail.get(i).getImg()==null){
                    System.out.println(productdetail.get(i).getImg());
                    System.out.println("Index ="+i);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","IMG in option product can't null").body(null);
                }
            }else {
                if (!mau.equals(productdetail.get(i).getMauSac())){
                    if (productdetail.get(i).getImg()==null){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error","IMG in option product can't null").body(null);
                    }
                    mau=productdetail.get(i).getMauSac();
                }
            }
        }
        SanPham sp = sanPhamService.getById(ctspRequest.getSanPham());
        ChatLieu cl = chatLieuService.getById(ctspRequest.getChatLieu());
        ThuongHieu th = thuongHieuService.getById(ctspRequest.getThuongHieu());
        TheLoai tl = theLoaiService.getById(ctspRequest.getTheLoai());
        DeGiay dg = deGiayService.getById(ctspRequest.getDeGiay());
        CoGiay cg = coGiayService.getById(ctspRequest.getCoGiay());
        MuiGiay mg = muiGiayService.getById(ctspRequest.getMuiGiay());
        if (sp==null || cl==null || th==null || tl==null || dg==null || cg==null || mg==null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        sp.setThuongHieu(th);
        sp.setTrangThai(ctspRequest.isTrangThai());
        sp.setTheLoai(tl);
        sp.setMoTa(ctspRequest.getMoTa());
        SanPham sanPham = sanPhamService.save(sp);
        Anh anh = null;
        for (int i=0;i<productdetail.size();i++) {
            int in = productdetail.indexOf(productdetail.get(i));
            KichCo kc = kichCoService.getByTen(productdetail.get(i).getKichCo());
            MauSac ms = mauSacService.getMauSacByMa(productdetail.get(i).getMauSac());
            ChiTietSanPham ct = chiTietSanPhamService.getBySizeAndColorAndProduct(kc, ms,sanPham);
            ChiTietSanPham ctsp = new ChiTietSanPham();
            if (ct != null) {
                ctsp = ct;
            }
            String img = productdetail.get(i).getImg();
            if (img != null) {
                List<Anh> lsta = anhService.getAllBySanPham(sanPham);
                Anh a2 = null;
                for (Anh a : lsta) {
                    if (a.getUrl().equals(img)) {
                        anh = a;
                        a2 = a;
                        break;
                    }
                }
                if (a2 != null) {
                    ctsp.setAnh(anh);
                } else {
                    Anh an = new Anh(sanPham, img, in == 0 ? true : false, Timestamp.from(Instant.now()));
                    anh = anhService.save(an);
                    ctsp.setAnh(anh);
                }
            } else {
                ctsp.setAnh(anh);
            }
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
            ctsp.setGiaBan(LibService.convertStringToBigDecimal(productdetail.get(i).getGiaBan()));
            ctsp.setMaSanPham(chiTietSanPhamService.generateDetailCode());
            ctsp.setSoLuongNhap(productdetail.get(i).getSoLuong());
            ctsp.setSoLuongTon(productdetail.get(i).getSoLuong());
            ctsp.setTrangThai(1);
            ctsp.setNgayTao(Timestamp.from(Instant.now()));
            ctsp.setMauSac(mauSacService.getMauSacByMa(productdetail.get(i).getMauSac()));
            chiTietSanPhamService.save(ctsp);
        }
        SanPham sp1 = sanPhamService.getById(sanPham.getId());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
