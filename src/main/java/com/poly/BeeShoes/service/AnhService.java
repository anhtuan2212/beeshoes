package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.Anh;
import com.poly.BeeShoes.model.SanPham;

import java.util.List;

public interface AnhService {
    Anh save(Anh anh);
    List<Anh> getAllBySanPham(SanPham sanPham);
    boolean delete(Long id);
}
