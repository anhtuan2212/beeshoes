package com.poly.BeeShoes.api;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.TrangThaiHoaDon;
import com.poly.BeeShoes.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonRestController {

    private final HoaDonService hoaDonService;

    @PutMapping("/xac-nhan")
    public ResponseEntity<String> xacNhanDon(
            @RequestBody List<String> maHoaDonList
    ) {
        maHoaDonList.forEach(ma -> {
            HoaDon hoaDon = hoaDonService.getHoaDonByMa(ma);
            if(hoaDon.getTrangThai().equals(TrangThaiHoaDon.ChoXacNhan)) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ChoGiao);
            } else if(hoaDon.getTrangThai().equals(TrangThaiHoaDon.ChoGiao)) {
                hoaDon.setTrangThai(TrangThaiHoaDon.DangGiao);
            } else if(hoaDon.getTrangThai().equals(TrangThaiHoaDon.DangGiao)) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ThanhCong);
            }
            hoaDonService.save(hoaDon);
        });
        return new ResponseEntity<>("updatedHoaDon", HttpStatus.OK);
    }

}
