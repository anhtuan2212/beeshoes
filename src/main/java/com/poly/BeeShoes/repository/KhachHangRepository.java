package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    Optional<KhachHang> findByMaKhachHang(String maKhachHang);

    boolean existsByMaKhachHang(String maKhachHang);
}
