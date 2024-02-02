package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    HoaDon hoaDon;

    @OneToOne
    @JoinColumn(name = "id_hinh_thuc_thanh_toan")
    HinhThucThanhToan hinhThucThanhToan;
}
