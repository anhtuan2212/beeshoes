package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.model.TrangThaiHoaDon;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    List<HoaDon> findByNguoiTao_Id(Long userId);
    List<HoaDon> findAllByNgayTaoBetween(Timestamp from, Timestamp to);
    @Query("SELECT h FROM HoaDon h WHERE DATE(h.ngayTao) = :date")
    List<HoaDon> findAllByNgayTao(Date date);
    List<HoaDon> findByKhachHangIdAndMaHoaDonAndTrangThai(Long customerId, String invoiceCode, String status, Sort sort);
    List<HoaDon> findAllByKhachHang(KhachHang khachHang,Sort sort);

    List<HoaDon> findByTrangThai(TrangThaiHoaDon trangThaiHoaDon, Sort sort);

    List<HoaDon> findByNgayTaoBetweenAndTrangThai(Timestamp startDate, Timestamp endDate, TrangThaiHoaDon trangThaiHoaDon);

//    List<HoaDon> findByTrangThai(String trangThai);


    Long countByKhachHangId(Long id);

    List<HoaDon> findByNgayTaoBetween(Date startDate, Date endDate);

    boolean existsByMaHoaDon(String maHoaDon);

    HoaDon findByMaHoaDon(String maHoaDon);
}
