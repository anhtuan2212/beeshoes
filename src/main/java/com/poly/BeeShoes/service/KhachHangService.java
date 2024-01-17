package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.KhachHang;

public interface KhachHangService {
    KhachHang getByMa(String ma);


    boolean existByMa(String ma);

    KhachHang createNew(KhachHang khachHang);

    KhachHang update(KhachHang khachHang);

    String generateCustomerCode();
}
