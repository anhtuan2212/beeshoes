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

    @OneToOne
    @JoinColumn(name = "nguoi_tao")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua")
    User nguoiSua;
    int trangThai;
}
