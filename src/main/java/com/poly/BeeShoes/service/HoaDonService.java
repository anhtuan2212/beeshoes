package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.NhanVien;
import com.poly.BeeShoes.model.TrangThaiHoaDon;
import com.poly.BeeShoes.model.User;

import java.util.List;
import java.util.Optional;

public interface HoaDonService {

    List<HoaDon> getAllHoaDon();

    Optional<HoaDon> getHoaDonById(Long id);

    Long count();

    Long countHoaDonCuaKhachHang(Long id);

    HoaDon taoMoiHoaDonKhiBanTaiQuay(User user);

    boolean capNhatTrangThaiHoaDon(User user, HoaDon hoaDon, TrangThaiHoaDon trangThaiHoaDon);

    boolean capNhatTrangThaiTatCaHoaDon(User user, List<HoaDon> danhSachHoaDon, TrangThaiHoaDon trangThaiHoaDon);

}
