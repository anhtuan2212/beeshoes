package com.poly.BeeShoes.request;

import com.poly.BeeShoes.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class VoucherRequest {
    Long id;
    String ma;
    String ten;
    LocalDateTime ngayBatDau;
    LocalDateTime ngayKetThuc;
    BigDecimal giaTriTienMat;
    Integer giaTriPhanTram;
    BigDecimal giaTriToiDa;
    BigDecimal giaTriToiThieu;
    Integer  soLuong;
    String moTa;
    String loaiVoucher;

}
