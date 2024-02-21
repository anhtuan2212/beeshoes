package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.GioHang;
import com.poly.BeeShoes.model.KhachHang;

public interface GioHangService {
    GioHang save(GioHang gioHang);
    boolean delete(Long id);
    GioHang getByKhachHang(KhachHang khachHang);
    GioHang getById(Long id);
}
