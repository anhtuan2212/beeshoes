package com.poly.BeeShoes.api;

import com.google.gson.Gson;
import com.poly.BeeShoes.dto.ProductDetailVersion;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    private final ThuongHieuService thuongHieuService;
    private final KichCoService kichCoService;
    private final AnhService anhService;
    private final MuiGiayService muiGiayService;
    private final CoGiayService coGiayService;
    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;

    @PostMapping("/them-san-pham")
    public ResponseEntity<SanPham> themSanPham(@RequestParam("ten") String ten) {
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
    public ResponseEntity<SanPham> chiTietSanPham(@RequestParam("imgSelected") String imgSelected,
                                                  @RequestParam("sanPham") Long sanPham,
                                                  @RequestParam("theLoai") Long theLoai,
                                                  @RequestParam("thuongHieu") Long thuongHieu,
                                                  @RequestParam("chatLieu") Long chatLieu,
                                                  @RequestParam("deGiay") Long deGiay,
                                                  @RequestParam("coGiay") Long coGiay,
                                                  @RequestParam("muiGiay") Long muiGiay,
                                                  @RequestParam("giaNhap") String giaNhap,
                                                  @RequestParam("giaGoc") String giaGoc,
                                                  @RequestParam("moTa") String moTa,
                                                  @RequestParam("sales") boolean sales,
                                                  @RequestParam("trangThai") boolean trangThai,
                                                  @RequestParam("product_details") String product_details
    ) {

        List<String> lst = LibService.checkDataProduct(sanPham, theLoai, thuongHieu, chatLieu, deGiay, coGiay, muiGiay, giaNhap, giaGoc);

        Gson gs = new Gson();
        String[] array = gs.fromJson(imgSelected, String[].class);
        Type listType = new TypeToken<List<ProductDetailVersion>>() {
        }.getType();
        List<ProductDetailVersion> productdetail = gs.fromJson(product_details, listType);

        List<String> lst2 = LibService.checkDataProductDetail(lst, productdetail,array);

        if (lst2 == null) {

            SanPham sp = sanPhamService.getById(sanPham);
            ChatLieu cl = chatLieuService.getById(chatLieu);
            ThuongHieu th = thuongHieuService.getById(thuongHieu);
            TheLoai tl = theLoaiService.getById(theLoai);
            DeGiay dg = deGiayService.getById(deGiay);
            CoGiay cg = coGiayService.getById(coGiay);
            MuiGiay mg = muiGiayService.getById(muiGiay);
            sp.setThuongHieu(th);
            sp.setTrangThai(trangThai);
            sp.setTheLoai(tl);
            sp.setMoTa(moTa);
            sanPhamService.save(sp);
            List<Anh> a = sp.getAnh();
                if (sp.getAnh().size() == 4) {
                    for (int i = 1; i < 4 - a.size(); i++) {
                        Anh anh = new Anh();
                        anh.setUrl(array[i]);
                        anh.setSanPham(sp);
                        if (i == 0) {
                            anh.setMain(true);
                        }
                        anh.setNgayTao(Timestamp.from(Instant.now()));
                        anhService.save(anh);
                    }
                }

                for (ProductDetailVersion pro : productdetail) {
                    ChiTietSanPham ctsp = new ChiTietSanPham();
                    KichCo kc = kichCoService.getByTen(pro.getKichCo());
                    ctsp.setChatLieu(cl);
                    ctsp.setDeGiay(dg);
                    ctsp.setCoGiay(cg);
                    ctsp.setMuiGiay(mg);
                    ctsp.setSale(sales);
                    ctsp.setTrangThai(trangThai == true ? 1 : 0);
                    ctsp.setSanPham(sp);
                    ctsp.setKichCo(kc);
                    ctsp.setGiaGoc(LibService.convertStringToBigDecimal(giaGoc));
                    ctsp.setGiaNhap(LibService.convertStringToBigDecimal(giaNhap));
                    ctsp.setGiaBan(LibService.convertStringToBigDecimal(pro.getGiaBan()));
                    ctsp.setMaSanPham(chiTietSanPhamService.generateDetailCode());
                    ctsp.setSoLuongNhap(pro.getSoLuong());
                    ctsp.setSoLuongTon(pro.getSoLuong());
                    ctsp.setTrangThai(1);
                    ctsp.setNgayTao(Timestamp.from(Instant.now()));
                    ctsp.setMauSac(mauSacService.getMauSacByMa(pro.getMauSac()));
                    chiTietSanPhamService.save(ctsp);
                }
            return ResponseEntity.status(HttpStatus.OK).body(sp);
        }else {
            String res = gs.toJson(lst2);
            return ResponseEntity.status(HttpStatus.OK).header("mess",res).body(null);
        }
    }

}
