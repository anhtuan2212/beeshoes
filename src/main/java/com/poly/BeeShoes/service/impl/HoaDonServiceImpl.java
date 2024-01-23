package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.TrangThaiHoaDon;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.repository.HoaDonRepository;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.utility.ConvertUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepository;

    @Override
    public List<HoaDon> getAllHoaDon() {
        return hoaDonRepository.findAll();
    }

    @Override
    public Optional<HoaDon> getHoaDonById(Long id) {
        return hoaDonRepository.findById(id);
    }

    @Override
    public Long count() {
        return hoaDonRepository.count();
    }

    @Override
    public Long countHoaDonCuaKhachHang(Long id) {
        return hoaDonRepository.countByKhachHangId(id);
    }

    @Override
    public HoaDon taoMoiHoaDonKhiBanTaiQuay(User user) {
        HoaDon createdHoaDon = new HoaDon();
        createdHoaDon.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
        createdHoaDon.setNguoiTao(user);
        return hoaDonRepository.save(createdHoaDon);
    }

    @Override
    public boolean capNhatTrangThaiHoaDon(User user, HoaDon hoaDon, TrangThaiHoaDon trangThaiHoaDon) {
        try {
            hoaDon.setTrangThai(trangThaiHoaDon);
            hoaDon.setNgaySua(ConvertUtility.DateToTimestamp(new Date()));
            hoaDon.setNguoiSua(user);
            hoaDonRepository.save(hoaDon);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean capNhatTrangThaiTatCaHoaDon(User user, List<HoaDon> danhSachHoaDon, TrangThaiHoaDon trangThaiHoaDon) {
        try {
            danhSachHoaDon.forEach(hoaDon -> {
                hoaDon.setTrangThai(trangThaiHoaDon);
                hoaDon.setNgaySua(ConvertUtility.DateToTimestamp(new Date()));
                hoaDon.setNguoiSua(user);
            });
            hoaDonRepository.saveAll(danhSachHoaDon);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
