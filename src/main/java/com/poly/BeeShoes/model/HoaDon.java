package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "id_nhan_vien")
    NhanVien nhanVien;

    @OneToOne
    @JoinColumn(name = "id_khach_hang")
    KhachHang khachHang;
    String maHoaDon;

    @OneToOne
    @JoinColumn(name = "id_voucher")
    Voucher voucher;

    @OneToOne
    @JoinColumn(name = "id_thanh_toan")
    ThanhToan thanhToan;

    @OneToOne
    @JoinColumn(name = "id_don_vi_van_chuyen")
    DonViVanChuyen donViVanChuyen;

    String diaChiNhan;
    String sdtNhan;

    BigDecimal tongTien;
    BigDecimal thucThu;
    BigDecimal phiShip;
    Date ngayXacNhan;
    Date ngayShip;
    Date ngayNhan;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "nguoi_tao")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua")
    User nguoiSua;

    @Enumerated(EnumType.STRING)
    TrangThaiHoaDon trangThai;
}