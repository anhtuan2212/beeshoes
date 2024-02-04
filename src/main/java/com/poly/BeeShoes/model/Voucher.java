package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.FieldDefaults;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "vouchers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String ma;
    String ten;
    Timestamp ngayBatDau;
    Timestamp ngayKetThuc;
    @Transient
    LocalDateTime startDate;
    @Transient
    LocalDateTime endDate;
    double giaTriTienMat;
    int giaTriPhanTram;
    double giaTriToiDa;
    String dieuKien;
    int soLuong;
    String moTa;
    String loaiVoucher;
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
