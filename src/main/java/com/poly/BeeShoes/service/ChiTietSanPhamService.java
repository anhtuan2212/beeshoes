package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.SanPham;

import java.util.List;

public interface ChiTietSanPhamService {
    ChiTietSanPham save(ChiTietSanPham chiTietSanPham);
    List<ChiTietSanPham> getAllBySanPham(SanPham samPham);
    boolean delete(Long id);
}
