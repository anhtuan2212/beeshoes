package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.KhachHangService;
import com.poly.BeeShoes.service.UserService;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OderTrackingController {
    private final UserService userService;
    private final HoaDonService hoaDonService;
    private final VoucherService voucherService;
    private final KhachHangService khachHangService;

    @GetMapping("/oder-tracking")
    public String tracking(@RequestParam(name = "oder", required = false) String code, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HoaDon hoaDon = new HoaDon();
        hoaDon = null;
        User user = new User();
        user = null;
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            user = userService.getByUsername(userDetails.getUsername());
            if (code != null) {
                hoaDon = hoaDonService.getHoaDonByMa(code);
                if (hoaDon != null && hoaDon.getKhachHang() != null && user.getKhachHang() != null
                        && hoaDon.getKhachHang().getId().equals(user.getKhachHang().getId())) {
                } else {
                    hoaDon = null;
                }
            }
        } else {
            if (code != null) {
                hoaDon = hoaDonService.getHoaDonByMa(code);
                if (hoaDon != null && hoaDon.getKhachHang() != null) {
                    hoaDon = null;
                }
            }
        }

        model.addAttribute("show", code);
        model.addAttribute("hoadon", hoaDon);
        model.addAttribute("user", user);
        return "customer/pages/oder-tracking/oder-tracking";
    }
}
