package com.poly.BeeShoes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class CTSPRequest {
    public CTSPRequest(String imgSelected, Long sanPham, Long theLoai, Long thuongHieu, Long chatLieu, Long deGiay, Long coGiay, Long muiGiay, String giaNhap, String giaGoc, String moTa, boolean sales, boolean trangThai, String product_details) {
        this.imgSelected = imgSelected;
        this.sanPham = sanPham;
        this.theLoai = theLoai;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.deGiay = deGiay;
        this.coGiay = coGiay;
        this.muiGiay = muiGiay;
        this.giaNhap = giaNhap;
        this.giaGoc = giaGoc;
        this.moTa = moTa;
        this.sales = sales;
        this.trangThai = trangThai;
        this.product_details = product_details;
    }

    @NotBlank(message = "Ảnh không được trống !")
    @JsonIgnore
    String imgSelected;

    @NotNull(message = "Sản Phẩm không được để trống !")
    Long sanPham;

    @NotNull(message = "Thể Loại không được để trống !")
    Long theLoai;

    @NotNull(message = "Thương Hiệu không được để trống !")
    Long thuongHieu;

    @NotNull(message = "Chất Liệu không được để trống !")
    Long chatLieu;

    @NotNull(message = "Đế Giày không được để trống !")
    Long deGiay;

    @NotNull(message = "Cổ Giày không được để trống !")
    Long coGiay;

    @NotNull(message = "Mũi Giày không được để trống !")
    Long muiGiay;

    @NotBlank(message = "Giá Nhập không được trống !")
    String giaNhap;
    @NotBlank(message = "Giá Gốc được trống !")
    String giaGoc;
    @NotBlank(message = "Mô Tả được trống !")
    String moTa;

    @NotNull(message = "Sale không được để trống !")
    boolean sales;

    @NotNull(message = "Trạng Thái không được để trống !")
    boolean trangThai;

    @NotBlank(message = "Chi Tiết Phiên Bản không được trống !")
    @JsonIgnore
    String product_details;

    @Override
    public String toString() {
        return "CTSPRequest{" +
                "imgSelected='" + imgSelected + '\'' +
                ", sanPham=" + sanPham +
                ", theLoai=" + theLoai +
                ", thuongHieu=" + thuongHieu +
                ", chatLieu=" + chatLieu +
                ", deGiay=" + deGiay +
                ", coGiay=" + coGiay +
                ", muiGiay=" + muiGiay +
                ", giaNhap='" + giaNhap + '\'' +
                ", giaGoc='" + giaGoc + '\'' +
                ", moTa='" + moTa + '\'' +
                ", sales=" + sales +
                ", trangThai=" + trangThai +
                ", product_details='" + product_details + '\'' +
                '}';
    }
}
