package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
     id binary(16) NOT NULL,
     email varchar(255) NOT NULL,
     password varchar(255) NOT NULL,
     id_khach_hang binary(16),
     id_nhan_vien binary(16),
     role tinyint check (role between 0 and 3),
     is_staff BIT,
     primary key (id),
     ngay_tao timestamp,
     ngay_sua timestamp,
     nguoi_tao binary(16),
     nguoi_sua binary(16),
     trang_thai tinyint check (trang_thai between 0 and 9)
 */
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email;
    String password;

    @OneToOne
    @JoinColumn(name = "id_khach_hang")
    KhachHang khachHang;

    @OneToOne
    @JoinColumn(name = "id_nhan_vien")
    NhanVien nhanVien;

    @Enumerated(EnumType.STRING)
    Role role;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToOne
    @JoinColumn(name = "nguoi_tao", referencedColumnName = "id")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua", referencedColumnName = "id")
    User nguoiSua;

    boolean trangThai;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
