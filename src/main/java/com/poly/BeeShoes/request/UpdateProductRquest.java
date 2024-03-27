package com.poly.BeeShoes.request;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateProductRquest {
    Long id;
    Long id_hdct;
    String maSanPham;
    String mauSac;
    String ten;
    String tenMau;
    String KichCo;
    String anh;
    Integer giaGoc;
    Integer giaBan;
    int soLuongTon;
    boolean isSale;
    Integer soLuong;
    Timestamp times;
    String message;
    BigDecimal tongTien;
    BigDecimal giamGia;
    BigDecimal thucThu;
    Integer phiShip;
}
