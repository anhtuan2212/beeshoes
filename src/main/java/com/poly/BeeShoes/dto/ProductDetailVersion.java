package com.poly.BeeShoes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailVersion {
    public ProductDetailVersion(String mauSac, String kichCo, String giaBan, int soLuong) {
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
    }

    public ProductDetailVersion(Long id, String mauSac, String kichCo, String giaBan, int soLuong) {
        this.id = id;
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
    }

    Long id;
    String mauSac;
    String kichCo;
    String giaBan;
    int soLuong;

    @Override
    public String toString() {
        return "ProductDetailVersion{" +
                "id=" + id +
                ", mauSac='" + mauSac + '\'' +
                ", kichCo='" + kichCo + '\'' +
                ", giaBan='" + giaBan + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}
