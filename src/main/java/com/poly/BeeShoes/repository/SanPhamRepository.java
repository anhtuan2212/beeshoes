package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,Long> {
    @Query("SELECT sp FROM SanPham sp WHERE EXISTS (SELECT 1 FROM sp.chiTietSanPham ctsp)")
    Page<SanPham> getAllByChiTietSanPhamExists(Pageable pageable);

    SanPham getFirstByTen(String name);
    boolean existsByTen(String ten);
}
