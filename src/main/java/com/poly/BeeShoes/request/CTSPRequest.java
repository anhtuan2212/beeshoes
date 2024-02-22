package com.poly.BeeShoes.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CTSPRequest {

    @NotNull(message = "Sản Phẩm không được để trống !")
    Long sanPham;

    String tenSanPham;
    List<String> tags;

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

    @NotBlank(message = "Mô Tả được trống !")
    String moTa;

    @NotNull(message = "Sale không được để trống !")
    boolean sales;

    @NotNull(message = "Trạng Thái không được để trống !")
    boolean trangThai;

    @NotBlank(message = "Chi Tiết Phiên Bản không được trống !")
    String product_details;

}
