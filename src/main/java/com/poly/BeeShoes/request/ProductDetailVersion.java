package com.poly.BeeShoes.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailVersion {
    public ProductDetailVersion(Long id, String img, String maMauSac, String tenMau, String kichCo, String giaBan, String giaGoc, int soLuong, boolean trangThai) {
        this.id = id;
        this.img = img;
        this.maMauSac = maMauSac;
        this.tenMau = tenMau;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.giaGoc = giaGoc;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }

    public ProductDetailVersion(String img, String maMauSac, String tenMau, String kichCo, String giaBan, String giaGoc, int soLuong, boolean trangThai) {
        this.img = img;
        this.maMauSac = maMauSac;
        this.tenMau = tenMau;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.giaGoc = giaGoc;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }

    Long id;
    String img;
    String maMauSac;
    String tenMau;
    String kichCo;
    String giaBan;
    String giaGoc;
    int soLuong;
    boolean trangThai;

    @Override
    public String toString() {
        return "ProductDetailVersion{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", maMauSac='" + maMauSac + '\'' +
                ", tenMau='" + tenMau + '\'' +
                ", kichCo='" + kichCo + '\'' +
                ", giaBan='" + giaBan + '\'' +
                ", giaGoc='" + giaGoc + '\'' +
                ", soLuong=" + soLuong +
                ", trangThai=" + trangThai +
                '}';
    }
}
