package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.GioHangChiTiet;

public interface GioHangChiTietService {
    GioHangChiTiet save(GioHangChiTiet gioHangChiTiet);
    boolean delete(Long id);

}
