package com.poly.BeeShoes.request;

import com.poly.BeeShoes.model.KhachHang;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
@Getter
@Setter
@ToString
public class KhachHangRequest {
    private Long id;
    private String maKhachHang;
    private String ho;
    private String tenDem;
    private String ten;
    private boolean gioiTinh;
    private Date ngaySinh;
    private String sdt;
    private boolean trangThai;
    String soNha;
    String phuongXa;
    String quanHuyen;
    String tinhThanhPho;
}
