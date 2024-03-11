package com.poly.BeeShoes.api;

import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.request.AddToCartRquest;
import com.poly.BeeShoes.request.GioHangRequest;
import com.poly.BeeShoes.request.chiTietSanPhamApiRquest;
import com.poly.BeeShoes.service.ChiTietSanPhamService;
import com.poly.BeeShoes.service.GioHangChiTietService;
import com.poly.BeeShoes.service.GioHangService;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShopAPI {
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final GioHangService gioHangService;
    private final GioHangChiTietService gioHangChiTietService;
    private final VoucherService voucherService;


    public Authentication getUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @PostMapping("/add-product-to-cart")
    public ResponseEntity<List<GioHangRequest>> addProductToCart(@ModelAttribute AddToCartRquest request) {
        if (request.getProduct() == null || request.getQuantity() == null) {
            return ResponseEntity.badRequest().header("status", "Invalid request").build();
        }
        ChiTietSanPham ctsp = chiTietSanPhamService.getById(request.getProduct());
        if (ctsp == null) {
            return ResponseEntity.notFound().header("status", "Product not found").build();
        }
        Authentication auth = getUserAuth();
        Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            GioHang gh = gioHangService.getByKhachHang(user.getKhachHang());
            if (gh == null) {
                gh = new GioHang();
                gh.setKhachHang(user.getKhachHang());
                gh = gioHangService.save(gh);
            }

            GioHangChiTiet ghct = gioHangChiTietService.getByCTSP(ctsp);
            if (ghct == null) {
                ghct = new GioHangChiTiet();
                ghct.setChiTietSanPham(ctsp);
                ghct.setSoLuong(request.getQuantity());
                ghct.setGioHang(gh);
            } else {
                if (request.isUpdate()) {
                    ghct.setSoLuong(request.getQuantity());
                } else {
                    ghct.setSoLuong(ghct.getSoLuong() + request.getQuantity());
                }
            }
            ghct = gioHangChiTietService.save(ghct);
            List<GioHangChiTiet> lst = gh.getGioHangChiTiets();
            lst.add(ghct);
            gh.setGioHangChiTiets(lst);
            gh = gioHangService.save(gh);

            List<GioHangChiTiet> lst_ghct = gh.getGioHangChiTiets();
            List<GioHangRequest> lst_res = new ArrayList<>();
            for (int i = 0; i < lst_ghct.size(); i++) {
                GioHangRequest ghResponse = new GioHangRequest();
                ghResponse.setSoLuong(lst_ghct.get(i).getSoLuong());
                ghResponse.setId(lst_ghct.get(i).getId());
                chiTietSanPhamApiRquest ctsp_res = new chiTietSanPhamApiRquest();
                ctsp_res.setId(lst_ghct.get(i).getChiTietSanPham().getId());
                ctsp_res.setTen(lst_ghct.get(i).getChiTietSanPham().getSanPham().getTen());
                ctsp_res.setAnh(lst_ghct.get(i).getChiTietSanPham().getAnh().getUrl());
                ctsp_res.setKichCo(lst_ghct.get(i).getChiTietSanPham().getKichCo().getTen());
                ctsp_res.setMauSac(lst_ghct.get(i).getChiTietSanPham().getMauSac().getTen());
                ctsp_res.setMaSanPham(lst_ghct.get(i).getChiTietSanPham().getMaSanPham());
                ctsp_res.setSoLuongTon(lst_ghct.get(i).getChiTietSanPham().getSoLuongTon());
                ctsp_res.setGiaBan(lst_ghct.get(i).getChiTietSanPham().getGiaBan().intValue());
                ctsp_res.setGiaGoc(lst_ghct.get(i).getChiTietSanPham().getGiaGoc().intValue());
                ghResponse.setChitietSanPham(ctsp_res);
                lst_res.add(ghResponse);
            }
            return ResponseEntity.ok().body(lst_res);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @GetMapping("/get-all-voucher")
    public ResponseEntity<List<Voucher>> getAllVoucher() {
        List<Voucher> lst = voucherService.getAllByTrangThai(2);
        for (Voucher vc : lst) {
            vc.setNguoiSua(null);
            vc.setNguoiTao(null);
            vc.setEndDate1(vc.getNgayKetThuc().toLocalDateTime());
        }
        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("/get-product-in-cart")
    public ResponseEntity<List<GioHangRequest>> getAll() {
        Authentication auth = getUserAuth();
        Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            KhachHang khachHang = user.getKhachHang();
            GioHang gh = gioHangService.getByKhachHang(khachHang);
            if (gh == null) {
                gh = new GioHang();
                gh.setKhachHang(khachHang);
                gh.setNgayTao(Timestamp.from(Instant.now()));
                gioHangService.save(gh);
            } else {
                List<GioHangChiTiet> lst_ghct = gh.getGioHangChiTiets();
                List<GioHangRequest> lst = new ArrayList<>();
                for (int i = 0; i < lst_ghct.size(); i++) {
                    GioHangRequest ghResponse = new GioHangRequest();
                    ghResponse.setSoLuong(lst_ghct.get(i).getSoLuong());
                    ghResponse.setId(lst_ghct.get(i).getId());
                    chiTietSanPhamApiRquest ctsp = new chiTietSanPhamApiRquest();
                    ctsp.setId(lst_ghct.get(i).getChiTietSanPham().getId());
                    ctsp.setTen(lst_ghct.get(i).getChiTietSanPham().getSanPham().getTen());
                    ctsp.setAnh(lst_ghct.get(i).getChiTietSanPham().getAnh().getUrl());
                    ctsp.setKichCo(lst_ghct.get(i).getChiTietSanPham().getKichCo().getTen());
                    ctsp.setMauSac(lst_ghct.get(i).getChiTietSanPham().getMauSac().getTen());
                    ctsp.setMaSanPham(lst_ghct.get(i).getChiTietSanPham().getMaSanPham());
                    ctsp.setSoLuongTon(lst_ghct.get(i).getChiTietSanPham().getSoLuongTon());
                    ctsp.setGiaBan(lst_ghct.get(i).getChiTietSanPham().getGiaBan().intValue());
                    ctsp.setGiaGoc(lst_ghct.get(i).getChiTietSanPham().getGiaGoc().intValue());
                    ghResponse.setChitietSanPham(ctsp);
                    lst.add(ghResponse);
                }
                return ResponseEntity.ok().body(lst);
            }
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.ok().body(null);
        }

    }

    @DeleteMapping("/del-shopping-cart-detail")
    public ResponseEntity deleteProduct(@RequestParam("id") Long id) {
        if (gioHangChiTietService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update-shopping-cart-quantity")
    public ResponseEntity updateQuantity(@RequestParam("id") Long id, @RequestParam("calcul") String cal) {
        GioHangChiTiet ghct = gioHangChiTietService.getById(id);
        if (ghct != null) {
            if (cal.equalsIgnoreCase("plus")) {
                ghct.setSoLuong(ghct.getSoLuong() + 1);
            }
            if (!cal.equalsIgnoreCase("plus") && ghct.getSoLuong() > 1) {
                ghct.setSoLuong(ghct.getSoLuong() - 1);
            }
            gioHangChiTietService.save(ghct);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
