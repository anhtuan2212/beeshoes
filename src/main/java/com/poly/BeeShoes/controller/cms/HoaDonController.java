package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.HoaDonChiTiet;
import com.poly.BeeShoes.service.HoaDonChiTietService;
import com.poly.BeeShoes.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cms")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;
    private final HoaDonChiTietService hoaDonChiTietService;

    @GetMapping("/hoa-don")
    public String hoaDonPage(Model model) {
        List<HoaDon> hoaDonList = hoaDonService.getAllHoaDon();
        Long count = hoaDonService.count();
        model.addAttribute("hoaDonList", hoaDonList);
        model.addAttribute("count", count);
        return "cms/pages/oders/orders";
    }

    @GetMapping("/hoa-don/{idHoaDon}/chi-tiet")
    public String hoaDonChiTietPage(
            @PathVariable("idHoaDon") Long id,
            Model model
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id).get();
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.getHoaDonChiTietCuaHoaDonById(id);
        double tongTien = 0;
        for(HoaDonChiTiet hdct : hoaDonChiTietList) {
            tongTien = hdct.getChiTietSanPham().getGiaBan().doubleValue() + tongTien;
        }
        model.addAttribute("hoaDonChiTietList", hoaDonChiTietList);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("tongTien", tongTien);
        return "cms/pages/oders/order-details";
    }

    @GetMapping("/check-out")
    public String checkout() {
        return "cms/pages/oders/checkout";
    }

}
