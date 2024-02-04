package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    BigDecimal giaBan;
    @Transient
    boolean sale;
    @Transient
    List<MauSac> mauSac;

    public boolean isSales() {
        if (chiTietSanPham == null || chiTietSanPham.isEmpty()) {
            return false;
        }
        // Sử dụng Java Stream để kiểm tra xem có đối tượng nào có isSale là true và trạng thái là 1 hay không
        return chiTietSanPham.stream()
                .anyMatch(ctsp -> ctsp.isSale() && ctsp.getTrangThai() == 1);
    }
    public List<KichCo> getDistinctKichCoList() {
        if (chiTietSanPham == null || chiTietSanPham.isEmpty()) {
            return List.of();
        }
        // Sử dụng Java Stream để lọc các kích cỡ không trùng lặp
        return chiTietSanPham.stream()
                .map(ChiTietSanPham::getKichCo)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<ChiTietSanPham> getSortedChiTietSanPhamByMauSac() {
        if (chiTietSanPham == null || chiTietSanPham.isEmpty()) {
            return Collections.emptyList();
        }
        // Sắp xếp danh sách chi tiết sản phẩm theo mã màu sắc
        chiTietSanPham.sort(Comparator.comparing(ctsp -> ctsp.getMauSac().getMaMauSac()));

        return chiTietSanPham;
    }

}
