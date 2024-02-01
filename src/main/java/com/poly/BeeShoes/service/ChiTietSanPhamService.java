package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.*;

import java.util.List;

public interface ChiTietSanPhamService {
    ChiTietSanPham save(ChiTietSanPham chiTietSanPham);
    ChiTietSanPham getById(Long id);
    ChiTietSanPham getBySizeAndColorAndProduct(KichCo kichCo, MauSac mauSac, SanPham sanPham);
    List<ChiTietSanPham> getAllBySanPham(SanPham samPham);
    boolean delete(Long id);
    boolean existsByChatLieu(ChatLieu chatLieu);
    boolean existsByMaSanPham(String ma);
    boolean existsBySanPham(SanPham sanPham);
    String generateDetailCode();
}
