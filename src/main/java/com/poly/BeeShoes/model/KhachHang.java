package com.poly.BeeShoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
    id binary(16) NOT NULL,
     ma_khach_hang varchar(10) UNIQUE NOT NULL,
     ho nvarchar(20),
     ten_dem nvarchar(20),
     ten nvarchar(20),
     gioi_tinh bit,
     ngay_sinh timestamp,
     sdt varchar(10),
     diem int,
     dia_chi_mac_dinh binary(16),
     id_hang_khach_hang binary(16),
     ngay_tao timestamp,
     ngay_sua timestamp,
     nguoi_tao binary(16),
     nguoi_sua binary(16),
     status bit default 1,
 */
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String maKhachHang;
    private   String ho;
    private  String tenDem;
    private  String ten;
    boolean gioiTinh;
    private  Date ngaySinh;
    private  String sdt;
    @JsonIgnore
    @OneToMany(mappedBy = "khachHang", fetch = FetchType.EAGER)
    List<DiaChi> diaChi;

    @OneToOne
    @JoinColumn(name = "id_hang_khach_hang")
    HangKhachHang hangKhachHang;
    @OneToOne
    @JoinColumn(name = "dia_chi_mac_dinh")
    DiaChi diaChiMacDinh;
    int diem;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "nguoi_tao")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua")
    User nguoiSua;

    boolean trangThai;
}
