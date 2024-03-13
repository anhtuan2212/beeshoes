package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.KhachHangService;
import com.poly.BeeShoes.service.UserService;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
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
        model.addAttribute("show", 0);
        if (code != null) {
            HoaDon hd = hoaDonService.getHoaDonByMa(code);
            model.addAttribute("hoadon", hd);
            model.addAttribute("show", 1);
        }
        return "customer/pages/oder-tracking/oder-tracking";
    }
}
