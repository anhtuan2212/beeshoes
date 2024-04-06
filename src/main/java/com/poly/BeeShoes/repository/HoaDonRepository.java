package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.constant.TrangThaiHoaDon;
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

    List<HoaDon> findAllByKhachHang(KhachHang khachHang, Sort sort);

    List<HoaDon> findByTrangThai(String trangThaiHoaDon, Sort sort);

    List<HoaDon> findByNgayTaoBetweenAndTrangThai(Timestamp startDate, Timestamp endDate, String trangThaiHoaDon);

//    List<HoaDon> findByTrangThai(String trangThai);


    Long countByKhachHangId(Long id);

    List<HoaDon> findByNgayTaoBetween(Date startDate, Date endDate);

    boolean existsByMaHoaDon(String maHoaDon);

    HoaDon findByMaHoaDon(String maHoaDon);

    @Query(
            value = "SELECT hour_of_day, COALESCE(number_of_invoices, 0) AS number_of_invoices " +
                    "FROM (" +
                    "   SELECT HOUR(ngay_tao) AS hour_of_day, COUNT(*) AS number_of_invoices " +
                    "   FROM hoa_don " +
                    "   WHERE DATE(ngay_tao) = ?1 " +
                    "   GROUP BY HOUR(ngay_tao) " +
                    "   UNION " +
                    "   SELECT hour_of_day, NULL AS number_of_invoices " +
                    "   FROM (SELECT 0 AS hour_of_day " +
                    "         UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 " +
                    "         UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 " +
                    "         UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 " +
                    "         UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 " +
                    "         UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 " +
                    "         UNION SELECT 21 UNION SELECT 22 UNION SELECT 23) AS all_hours " +
                    "   WHERE all_hours.hour_of_day NOT IN (SELECT HOUR(ngay_tao) FROM hoa_don WHERE DATE(ngay_tao) = ?1)" +
                    ") AS result " +
                    "ORDER BY hour_of_day",
            nativeQuery = true
    )
    List<Object[]> getAllCountCreatedByCreatDate(String date);
    @Query(
            value = "SELECT hour_of_day, COALESCE(number_of_invoices, 0) AS number_of_invoices " +
                    "FROM (" +
                    "   SELECT HOUR(ngay_tao) AS hour_of_day, COUNT(*) AS number_of_invoices " +
                    "   FROM hoa_don " +
                    "   WHERE DATE(ngay_tao) = ?1 AND loai_hoa_don = ?2 " + // Thêm điều kiện loại hóa đơn
                    "   GROUP BY HOUR(ngay_tao) " +
                    "   UNION " +
                    "   SELECT hour_of_day, NULL AS number_of_invoices " +
                    "   FROM (SELECT 0 AS hour_of_day " +
                    "         UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 " +
                    "         UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 " +
                    "         UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 " +
                    "         UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 " +
                    "         UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 " +
                    "         UNION SELECT 21 UNION SELECT 22 UNION SELECT 23) AS all_hours " +
                    "   WHERE all_hours.hour_of_day NOT IN (SELECT HOUR(ngay_tao) FROM hoa_don WHERE DATE(ngay_tao) = ?1 AND loai_hoa_don = ?2)" +
                    ") AS result " +
                    "ORDER BY hour_of_day",
            nativeQuery = true
    )
    List<Object[]> getCountCreatedByCreatDateAndTypeHD(String date,boolean loaiHoaDon);
}
