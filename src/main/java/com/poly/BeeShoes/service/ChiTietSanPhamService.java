package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.KichCo;
import com.poly.BeeShoes.model.MauSac;
import com.poly.BeeShoes.model.SanPham;

import java.util.List;

public interface ChiTietSanPhamService {
    ChiTietSanPham save(ChiTietSanPham chiTietSanPham);
    ChiTietSanPham getById(Long id);
//    ChiTietSanPham getBySizeAndColor(KichCo kichCo, MauSac mauSac);
    List<ChiTietSanPham> getAllBySanPham(SanPham samPham);
    boolean delete(Long id);
    boolean existsByMaSanPham(String ma);
    String generateDetailCode();
}
