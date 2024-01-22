package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.model.NhanVien;

import java.util.List;

public interface KhachHangService {
    List<KhachHang> getAll();

    void delete(Long id);

    KhachHang detail(Long id);

    KhachHang add(KhachHang khachHang);

    KhachHang update(KhachHang khachHang, Long id);
    KhachHang getByMa(String ma);

    boolean existByMa(String ma);

    String generateCustomerCode();
}
