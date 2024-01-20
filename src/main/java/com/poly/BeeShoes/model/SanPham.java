package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String ten;
    String moTa;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiSua;

    boolean trangThai;

    @JoinColumn(name = "id_thuong_hieu")
    @ManyToOne
    ThuongHieu thuongHieu;

    @JoinColumn(name = "id_the_loai")
    @ManyToOne
    TheLoai theLoai;

    @OneToMany(mappedBy = "sanPham")
    List<Anh> anh;

    @OneToMany(mappedBy = "sanPham")
    List<ChiTietSanPham> chiTietSanPham;

    @Transient
    int soLuong;

    @Transient
    BigDecimal giaNhap;

    @Transient
    List<MauSac> mauSac;
}
