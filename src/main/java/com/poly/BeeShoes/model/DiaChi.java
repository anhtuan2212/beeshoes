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
@Table(name = "dia_chi")
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
   id bigint auto_increment,
    id_khach_hang bigint,
    so_nha nvarchar(256),
    phuong_xa nvarchar(256),
    quan_huyen nvarchar(256),
    tinh_thanh_pho nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
 */
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang")
    KhachHang khachHang;

    String soNha;
    String phuongXa;
    String quanHuyen;
    String tinhThanhPho;
    Timestamp ngayTao;
    Timestamp ngaySua;

    Long nguoiTao;

    Long trangThai;
}
