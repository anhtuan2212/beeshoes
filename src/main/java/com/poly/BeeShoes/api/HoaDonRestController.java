package com.poly.BeeShoes.api;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.TrangThaiHoaDon;
import com.poly.BeeShoes.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonRestController {

    private final HoaDonService hoaDonService;

    @PostMapping("/xac-nhan")
    public ResponseEntity<String> xacNhanDon(
            @RequestBody List<String> maHoaDonList
    ) {
        List<HoaDon> hoaDonList = new ArrayList<>();
        maHoaDonList.forEach(ma -> {
            HoaDon hoaDon = hoaDonService.getHoaDonByMa(ma);
            if(hoaDon.getTrangThai() == TrangThaiHoaDon.ChoXacNhan) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ChoGiao);
            } else if(hoaDon.getTrangThai() == TrangThaiHoaDon.ChoGiao) {
                hoaDon.setTrangThai(TrangThaiHoaDon.DangGiao);
            } else if(hoaDon.getTrangThai() == TrangThaiHoaDon.DangGiao) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ThanhCong);
            }
            HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
            hoaDonList.add(updatedHoaDon);
            System.out.println(updatedHoaDon.getMaHoaDon() + " | " + updatedHoaDon.getTrangThai().name());
        });
        return new ResponseEntity<>("Xác nhận thành công đơn hàng", HttpStatus.OK);
    }

    @PostMapping("/huy")
    public ResponseEntity<String> huyDon(
            @RequestBody List<String> maHoaDonList
    ) {
        List<HoaDon> hoaDonList = new ArrayList<>();
        maHoaDonList.forEach(ma -> {
            HoaDon hoaDon = hoaDonService.getHoaDonByMa(ma);
            hoaDon.setTrangThai(TrangThaiHoaDon.Huy);
            HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
            hoaDonList.add(updatedHoaDon);
            System.out.println(updatedHoaDon.getMaHoaDon() + " | " + updatedHoaDon.getTrangThai().name());
        });
        return new ResponseEntity<>("Hủy thành công đơn hàng", HttpStatus.OK);
    }
}
