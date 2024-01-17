package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    Optional<KhachHang> findByMaKhachHang(String maKhachHang);

    boolean existsByMaKhachHang(String maKhachHang);
}
