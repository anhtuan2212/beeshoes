package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChiTietSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String maSanPham;

    @JoinColumn(name = "id_thuong_hieu")
    @ManyToOne
    ThuongHieu thuongHieu;

    @JoinColumn(name = "id_san_pham")
    @ManyToOne
    SanPham sanPham;

    @JoinColumn(name = "id_mau_sac")
    @ManyToOne
    MauSac mauSac;

    @JoinColumn(name = "id_de_giay")
    @ManyToOne
    DeGiay deGiay;

    @JoinColumn(name = "id_kich_co")
    @ManyToOne
    KichCo kichCo;

    @JoinColumn(name = "id_chat_lieu")
    @ManyToOne
    ChatLieu chatLieu;

    @JoinColumn(name = "id_the_loai")
    @ManyToOne
    TheLoai theLoai;

    @JoinColumn(name = "id_co_giay")
    @ManyToOne
    CoGiay coGiay;

    @JoinColumn(name = "id_mui_giay")
    @ManyToOne
    MuiGiay muiGiay;

    int giamPhanTram;

    BigDecimal giamTien;
    BigDecimal giaNhap;
    BigDecimal giaGoc;
    BigDecimal giaBan;
    int soLuongNhap;
    int soLuongTon;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiSua;

    int trangThai;

}
