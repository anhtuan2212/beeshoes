package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.service.HinhThucThanhToanService;
import com.poly.BeeShoes.service.HoaDonChiTietService;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.LichSuHoaDonService;
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
    private final LichSuHoaDonService lichSuHoaDonService;
    private final HinhThucThanhToanService hinhThucThanhToanService;
    @GetMapping("/hoa-don")
    public String hoaDonPage(Model model) {
        List<HinhThucThanhToan> hinhThucThanhToanList = hinhThucThanhToanService.getAll();
        List<HoaDon> hoaDonChoXacNhanList = hoaDonService.getAllHoaDonByTrangThai(TrangThaiHoaDon.ChoXacNhan);
        List<HoaDon> hoaDonChoGiaoList = hoaDonService.getAllHoaDonByTrangThai(TrangThaiHoaDon.ChoGiao);
        List<HoaDon> hoaDonDangGiaoList = hoaDonService.getAllHoaDonByTrangThai(TrangThaiHoaDon.DangGiao);
        List<HoaDon> hoaDonThanhCongList = hoaDonService.getAllHoaDonByTrangThai(TrangThaiHoaDon.ThanhCong);
        List<HoaDon> hoaDonList = hoaDonService.getAllHoaDon();
        Long count = hoaDonService.count();
        model.addAttribute("hoaDonList", hoaDonList);
        model.addAttribute("hoaDonChoXacNhanList", hoaDonChoXacNhanList);
        model.addAttribute("hoaDonChoGiaoList", hoaDonChoGiaoList);
        model.addAttribute("hoaDonDangGiaoList", hoaDonDangGiaoList);
        model.addAttribute("hoaDonThanhCongList", hoaDonThanhCongList);
        model.addAttribute("hinhThucThanhToanList", hinhThucThanhToanList);
        model.addAttribute("count", count);
        return "cms/pages/orders/orders";
    }

    @GetMapping("/hoa-don/{idHoaDon}/chi-tiet")
    public String hoaDonChiTietPage(
            @PathVariable("idHoaDon") Long id,
            Model model
    ) {

        List<LichSuHoaDon> lichSuHoaDonList = lichSuHoaDonService.getAllLichSuHoaDonByIdHoaDon(id);
        HoaDon hoaDon = hoaDonService.getHoaDonById(id).get();
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.getHoaDonChiTietCuaHoaDonById(id);
        Long countHoaDonCuaKhachHang = hoaDonService.countHoaDonCuaKhachHang(hoaDon.getKhachHang().getId());

        double tongTien = 0;
        for(HoaDonChiTiet hdct : hoaDonChiTietList) {
            tongTien = (hdct.getChiTietSanPham().getGiaBan().doubleValue() * hdct.getSoLuong()) + tongTien;
        }
        model.addAttribute("lichSuHoaDonList", lichSuHoaDonList);
        model.addAttribute("hoaDonChiTietList", hoaDonChiTietList);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("countHoaDonCuaKhachHang", countHoaDonCuaKhachHang);
        return "cms/pages/orders/order-details";
    }

    @GetMapping("/check-out")
    public String checkout() {
        return "cms/pages/orders/checkout";
    }

}
