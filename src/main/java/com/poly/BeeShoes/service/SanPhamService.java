package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.SanPham;

import java.util.List;

public interface SanPhamService {
    SanPham save(SanPham sanPham);
    List<SanPham> getAll();
    boolean delete(Long id);
}
