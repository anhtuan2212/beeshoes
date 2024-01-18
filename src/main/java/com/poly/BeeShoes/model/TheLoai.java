package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TheLoai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String tenTheLoai;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "id")
    User nguoiSua;
    boolean trangThai;
}
