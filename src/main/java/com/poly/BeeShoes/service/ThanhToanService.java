package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ThanhToan;

public interface ThanhToanService {
    ThanhToan getById(Long id);
    ThanhToan save(ThanhToan thanhToan);
}
