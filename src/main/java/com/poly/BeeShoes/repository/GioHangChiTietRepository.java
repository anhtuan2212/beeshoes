package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Long> {
    GioHangChiTiet findByChiTietSanPham(ChiTietSanPham chiTietSanPham);
    void deleteByGioHang_Id(Long id);
}
