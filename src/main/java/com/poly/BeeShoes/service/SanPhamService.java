package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SanPhamService {
    List<String> getListKichCo(Long id);
    SanPham save(SanPham sanPham);
    SanPham getById(Long id);
    SanPham getByTen(String name);
    boolean existsByTen(String name);
    List<SanPham> getAll();
    Page<SanPham> getAllShop(Pageable pageable);
    boolean delete(Long id);

    Map<String, Map<String, Long>> getKichCoCountByMauSac(Long id);
}
