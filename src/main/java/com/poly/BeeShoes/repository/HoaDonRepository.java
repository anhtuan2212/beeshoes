package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    List<HoaDon> findByNguoiTao_Id(Long userId);

    List<HoaDon> findByTrangThai(String trangThai);

    Long countByKhachHangId(Long id);

    List<HoaDon> findByNgayTaoBetween(Date startDate, Date endDate);

    boolean existsByMaHoaDon(String maHoaDon);
}
