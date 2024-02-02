package com.poly.BeeShoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "khach_hang")
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
    id bigint auto_increment,
    ma_khach_hang varchar(256),
    ten nvarchar(256),
    ten_dem nvarchar(256),
    ho nvarchar(256),
    gioi_tinh bit,
    ngay_sinh date,
    sdt varchar(10),
    diem int,
    id_hang_khach_hang bigint,
    dia_chi_mac_dinh bigint,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
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

    Long nguoiTao;

    Long nguoiSua;

    boolean trangThai;
}
