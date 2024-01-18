package com.poly.BeeShoes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
    id binary(16) not null,
    ten_hang nvarchar(50),
    diem_toi_thieu int,

    //ALTER TABLE hang_khach_hang ADD ma_hang varchar(10)

 */
public class HangKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String tenHang;
    private  int diemToiThieu;
}
