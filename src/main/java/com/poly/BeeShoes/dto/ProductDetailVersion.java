package com.poly.BeeShoes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailVersion {
    public ProductDetailVersion(String mauSac, Long kichCo, String giaBan, int soLuong) {
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
    }

    String mauSac;
    Long kichCo;
    String giaBan;
    int soLuong;

    @Override
    public String toString() {
        return "ProductDetailVersion{" +
                "mauSac='" + mauSac + '\'' +
                ", kichCo=" + kichCo +
                ", giaBan='" + giaBan + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}
