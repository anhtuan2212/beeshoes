package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nhan_vien")
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
     id bigint auto_increment,
    ma_nhan_vien varchar(256),
    id_chuc_vu bigint,
    ten nvarchar(256),
    ten_dem nvarchar(256),
    ho nvarchar(256),
    gioi_tinh bit,
    ngay_sinh date,
    dia_chi nvarchar(256),
    sdt varchar(10),
    cccd varchar(12),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
 */
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String maNhanVien;
    String hoTen;
    boolean gioiTinh;
    Date ngaySinh;
    String soNha;
    String phuongXa;
    String quanHuyen;
    String tinhThanhPho;
    String sdt;
    String cccd;

    @ManyToOne
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
