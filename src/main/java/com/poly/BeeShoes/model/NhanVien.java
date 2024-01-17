package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
     id binary(16) NOT NULL,
     ma_nhan_vien varchar(50) UNIQUE NOT NULL,
     ho nvarchar(50) NOT NULL,
     ten_dem nvarchar(50),
     ten nvarchar(50) NOT NULL,
     gioi_tinh BIT,
     ngay_sinh DATE,
     dia_chi nvarchar(255),
     sdt varchar(15),
     cccd varchar(15),
     id_chuc_vu binary(16),
     ngay_tao timestamp,
     ngay_sua timestamp,
     nguoi_tao binary(16),
     nguoi_sua binary(16),
     trang_thai BIT DEFAULT 1, -- default = 1 = true
 */
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String maNhanVien;
    String ho;
    String tenDem;
    String ten;
    boolean gioiTinh;
    Date ngaySinh;
    String diaChi;
    String sdt;
    String cccd;

    @OneToOne
    @JoinColumn(name = "id_chuc_vu")
    ChucVu chucVu;

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
