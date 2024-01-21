package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SanPhamService {
    SanPham save(SanPham sanPham);
    SanPham getById(Long id);
    List<SanPham> getAll();
    Page<SanPham> getAllShop(Pageable pageable);
    boolean delete(Long id);
}
