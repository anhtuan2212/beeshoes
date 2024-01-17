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
    id bigint auto_increment,
    id_khach_hang bigint,
    duong varchar(256),
    so_nha varchar(256),
    thanh_pho varchar(256),
    tinh varchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit,
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
    String thanhPho;
    String zipCode;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "nguoi_tao")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua")
    User nguoiSua;
    int trangThai;
}
