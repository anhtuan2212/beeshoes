package com.poly.BeeShoes.service;

import com.poly.BeeShoes.constant.TrangThaiHoaDon;
import com.poly.BeeShoes.model.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface HoaDonService {
    List<int[]> cntInvoiceInHourByCreatedDate(String date);

    List<HoaDon> getAllHoaDon();

    List<HoaDon> getAllByDate(Date date);

    List<HoaDon> getAllByDateRange(Timestamp from, Timestamp to);

    List<HoaDon> getByCustomerIdAndInvoiceCodeAndStatus(Long customerId, String invoiceCode, String status);

    List<HoaDon> getByKhachHang(KhachHang khachHang);

    List<HoaDon> getAllHoaDonByTrangThai(String trangThaiHoaDon);

    HoaDon getHoaDonById(Long id);

    Long count();

    Long countHoaDonCuaKhachHang(Long id);

    HoaDon taoMoiHoaDonKhiBanTaiQuay(User user);

    boolean capNhatTrangThaiHoaDon(User user, HoaDon hoaDon, String trangThaiHoaDon);

    boolean capNhatTrangThaiTatCaHoaDon(User user, List<HoaDon> danhSachHoaDon, String trangThaiHoaDon);

    HoaDon getHoaDonByMa(String ma);

    HoaDon save(HoaDon hoaDon);

    String generateInvoiceCode();
}
