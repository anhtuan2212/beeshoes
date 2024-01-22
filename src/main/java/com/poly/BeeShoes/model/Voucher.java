package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table (name = "voucher")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
          @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;
          private String ma;
          private String ten;
          private String loaiVoucher;
          private Date ngayBatDau;
    private Date ngayKetThuc;
    private BigDecimal giaTriTienMat;
    private byte giaTriPhanTram;
    private BigDecimal giaTriToiDa;
    private String hangKhachHang;
    private String dieuKien;
    private Integer soLuong;
    private String moTa;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiSua;
    boolean trangThai;
}
