package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HinhThucThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String maGiaoDich;
    String hinhThuc;
    String moTa;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "nguoi_tao")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua")
    User nguoiSua;

    boolean trangThai;

    @ManyToMany(mappedBy = "hinhThucThanhToans")
    private List<HoaDon> hoaDon;
}
