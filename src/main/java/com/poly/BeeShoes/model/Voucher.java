package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Setter
@Getter
@Table(name = "vouchers")
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
    LocalDateTime startDate1;
    @Transient
    LocalDateTime endDate1;
    BigDecimal giaTriTienMat;
    Integer giaTriPhanTram;
    BigDecimal giaTriToiDa;
    BigDecimal giaTriToiThieu;
    Integer  soLuong;
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
    Integer trangThai;


}
