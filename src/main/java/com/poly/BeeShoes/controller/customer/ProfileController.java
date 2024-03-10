package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.LichSuHoaDon;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.LichSuHoaDonService;
import com.poly.BeeShoes.service.UserService;
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
    private final LichSuHoaDonService lichSuHoaDonService;
    @GetMapping("/user-profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.getByUsername(userDetails.getUsername());
            List<HoaDon> hd = hoaDonService.getByKhachHang(user.getKhachHang());
            model.addAttribute("user",user);
            model.addAttribute("lsthoadon",hd);
            System.out.println("User is authenticated. Email: " + user.getEmail());
            return "customer/pages/profile/user-profile";
        } else {
            return "redirect:/";
        }
    }
}
