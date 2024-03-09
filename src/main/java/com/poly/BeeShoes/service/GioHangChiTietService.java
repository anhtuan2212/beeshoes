package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.GioHangChiTiet;

public interface GioHangChiTietService {
    GioHangChiTiet save(GioHangChiTiet gioHangChiTiet);
    boolean delete(Long id);

    GioHangChiTiet getByCTSP(ChiTietSanPham ctsp);
    GioHangChiTiet getById(Long id);

    void deleteByGioHangId(Long id);
}
