package com.poly.BeeShoes.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailVersion {
    public ProductDetailVersion(String img, String mauSac, String kichCo, String giaBan, int soLuong) {
        this.img = img;
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
    }

    public ProductDetailVersion(Long id, String img, String mauSac, String kichCo, String giaBan, int soLuong) {
        this.id = id;
        this.img = img;
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
    }

    Long id;
    String img;
    String mauSac;
    String kichCo;
    String giaBan;
    int soLuong;

    @Override
    public String toString() {
        return "ProductDetailVersion{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", mauSac='" + mauSac + '\'' +
                ", kichCo='" + kichCo + '\'' +
                ", giaBan='" + giaBan + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}
