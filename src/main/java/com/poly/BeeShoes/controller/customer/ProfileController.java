package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.LichSuHoaDon;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.LichSuHoaDonService;
import com.poly.BeeShoes.service.UserService;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final HoaDonService hoaDonService;
    private final VoucherService voucherService;
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
            model.addAttribute("user",user);
            model.addAttribute("lsthoadon",hd);
            model.addAttribute("lstvouchers",vouchers);
            return "customer/pages/profile/user-profile";
        } else {
//            User user = userService.getByUsername("lutan2212@gmail.com");
//            List<HoaDon> hd = hoaDonService.getByKhachHang(user.getKhachHang());
//            List<Voucher> vouchers = voucherService.getAllByTrangThai(2);
//            hd.forEach((hoaDon -> {
//                vouchers.removeIf(v -> v.equals(hoaDon.getVoucher()));
//            }));
//            model.addAttribute("user",user);
//            model.addAttribute("lsthoadon",hd);
//            model.addAttribute("lstvouchers",vouchers);
//            return "customer/pages/profile/user-profile";
            return "redirect:/";
        }
    }
}
