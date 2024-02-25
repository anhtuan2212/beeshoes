package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.GioHang;
import com.poly.BeeShoes.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Long> {
    GioHang getFirstByKhachHang(KhachHang khachHang);
}
