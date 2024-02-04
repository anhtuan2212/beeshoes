package com.poly.BeeShoes.controller.cms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.request.CTSPRequest;
import com.poly.BeeShoes.request.ProductDetailVersion;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class ViewProductController {
    private final TheLoaiService theLoaiService;
    private final ChatLieuService chatLieuService;
    private final DeGiayService deGiayService;
    private final MauSacService mauSacService;
    private final ThuongHieuService thuongHieuService;
    private final KichCoService kichCoService;
    private final MuiGiayService muiGiayService;
    private final CoGiayService coGiayService;
    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    Gson gs = new Gson();

    @GetMapping("/view-product")
    public String product(Model model, @RequestParam("id") Long id) {
        model.addAttribute("lsttheloai", theLoaiService.getAll());
        model.addAttribute("lstchatlieu", chatLieuService.getAll());
        model.addAttribute("lstthuonghieu", thuongHieuService.getAll());
        model.addAttribute("lstmausac", mauSacService.getAll());
        model.addAttribute("lstdegiay", deGiayService.getAll());
        model.addAttribute("lstmuigiay", muiGiayService.getAll());
        model.addAttribute("lstcogiay", coGiayService.getAll());
        model.addAttribute("lstsanpham", sanPhamService.getAll());
        model.addAttribute("lstkichco", kichCoService.getAll());
        if (id != null) {
            SanPham sp = sanPhamService.getById(id);
            List<String> lst = sanPhamService.getListKichCo(id);
            if (sp == null) {
                return "redirect:cms/product";
            } else {
                model.addAttribute("sanPham", sp);
            }
            model.addAttribute("kichCo", lst);
            return "cms/pages/products/view-products";
        }
        return "redirect:cms/product";

    }

    @PostMapping("/update-quick-product")
    public ResponseEntity update( @ModelAttribute CTSPRequest ctspRequest) {
        System.out.println(ctspRequest.toString());
        Type listType = new TypeToken<List<ProductDetailVersion>>() {
        }.getType();
        List<ProductDetailVersion> productdetail = gs.fromJson(ctspRequest.getProduct_details(), listType);
        for (int i = 0; i < productdetail.size(); i++) {
            if (productdetail.get(i).getGiaBan().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "GiaBanNull").body(null);
            }
            if (productdetail.get(i).getGiaGoc().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "GiaGocNull").body(null);
            }
            if (productdetail.get(i).getSoLuong() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "QuantityNull").body(null);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "ThuocTinhNull").body(null);
        }
        sp.setThuongHieu(th);
        sp.setTrangThai(ctspRequest.isTrangThai());
        sp.setSale(ctspRequest.isSales());
        sp.setTheLoai(tl);
        sp.setMoTa(ctspRequest.getMoTa());
        SanPham sanPham = sanPhamService.save(sp);
        for (int i = 0; i < productdetail.size(); i++) {
            ChiTietSanPham ct = chiTietSanPhamService.getById(productdetail.get(i).getId());
            ChiTietSanPham ctsp = new ChiTietSanPham();
            if (ct != null) {
                ctsp = ct;
            }
            ctsp.setChatLieu(cl);
            ctsp.setDeGiay(dg);
            ctsp.setCoGiay(cg);
            ctsp.setMuiGiay(mg);
            ctsp.setSale(ctspRequest.isSales());
            System.out.println(ctsp.isSale());
            ctsp.setSanPham(sanPham);
            ctsp.setGiaGoc(LibService.convertStringToBigDecimal(productdetail.get(i).getGiaGoc()));
            ctsp.setGiaNhap(LibService.convertStringToBigDecimal("0"));
            ctsp.setGiaBan(LibService.convertStringToBigDecimal(productdetail.get(i).getGiaBan()));
            ctsp.setSoLuongTon(productdetail.get(i).getSoLuong());
            ctsp.setTrangThai(productdetail.get(i).isTrangThai() == true ? 1 : 0);
            chiTietSanPhamService.save(ctsp);
        }
        return ResponseEntity.status(HttpStatus.OK).header("success", "ok").body(null);
    }
}
