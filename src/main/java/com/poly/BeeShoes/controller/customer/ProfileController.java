package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.request.ProfileRequest;
import com.poly.BeeShoes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final HoaDonService hoaDonService;
    private final VoucherService voucherService;
    private final KhachHangService khachHangService;


    @GetMapping("/user-profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.getByUsername(userDetails.getUsername());
            List<HoaDon> hd = hoaDonService.getByKhachHang(user.getKhachHang());
            List<Voucher> vouchers = voucherService.getAllByTrangThai(2);
            hd.forEach((hoaDon -> {
                vouchers.removeIf(v -> v.equals(hoaDon.getVoucher()));
            }));
            model.addAttribute("user", user);
            model.addAttribute("lsthoadon", hd);
            model.addAttribute("lstvouchers", vouchers);
            return "customer/pages/profile/user-profile";
        } else {
//            User user = userService.getByUsername("lutan2212@gmail.com");
//            List<HoaDon> hd = hoaDonService.getByKhachHang(user.getKhachHang());
//            List<Voucher> vouchers = voucherService.getAllByTrangThai(2);
//            hd.forEach((hoaDon -> {
//                vouchers.removeIf(v -> v.equals(hoaDon.getVoucher()));
//            }));
//            model.addAttribute("user", user);
//            model.addAttribute("lsthoadon", hd);
//            model.addAttribute("lstvouchers", vouchers);
//            return "customer/pages/profile/user-profile";
            return "redirect:/";
        }
    }

    @PostMapping("/api/update-profile")
    public ResponseEntity update(@ModelAttribute ProfileRequest request) {
        System.out.println(request.toString());
        if (request.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Empty id.").build();
        }
        if (request.getHoTen().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Please enter your full name.").build();
        }
        if (request.getAvatar().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Please enter your avatar.").build();
        }
        if (request.getEmail().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Please enter your email.").build();
        }
        if (request.getSdt().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Please enter your phone.").build();
        }

        User user = userService.findByKhachHang_Id(request.getId());
        KhachHang khachHang = khachHangService.detail(request.getId());
        boolean emailExists = userService.existsByEmail(request.getEmail());
        boolean phoneNumberExists = khachHangService.existsBySdt(request.getSdt());

        if(user.getEmail().equalsIgnoreCase(request.getEmail())){
            emailExists = false;
        }
        if(khachHang.getSdt().equalsIgnoreCase(request.getSdt())){
            phoneNumberExists = false;
        }
        if (emailExists){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Exists by email.").build();
        }
        if (phoneNumberExists){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("error", "Exists by phone number.").build();
        }
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        user.setNgaySua(Timestamp.from(Instant.now()));
        userService.update(user);
        khachHang.setNgaySinh(request.getNgaySinh());
        khachHang.setGioiTinh(request.isGioiTinh());
        khachHang.setHoTen(request.getHoTen());
        khachHang.setNgaySua(Timestamp.from(Instant.now()));
        khachHangService.update(khachHang);
        return ResponseEntity.ok().build();
    }
}
