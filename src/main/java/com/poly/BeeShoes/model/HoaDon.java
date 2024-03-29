package com.poly.BeeShoes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "id_nhan_vien")
    NhanVien nhanVien;

    @OneToOne
    @JoinColumn(name = "id_khach_hang")
    KhachHang khachHang;

    @ManyToMany
    @JoinTable(name = "thanh_toan",
            joinColumns = @JoinColumn(name = "id_hoa_don"),
            inverseJoinColumns = @JoinColumn(name = "id_hinh_thuc_thanh_toan"))
    private List<HinhThucThanhToan> hinhThucThanhToans;

    String maHoaDon;

    String maVanChuyen;
    boolean loaiHoaDon;

    @OneToOne
    @JoinColumn(name = "id_voucher")
    Voucher voucher;

    @OneToOne
    @JoinColumn(name = "id_thanh_toan")
    ThanhToan thanhToan;

    @OneToOne
    @JoinColumn(name = "id_don_vi_van_chuyen")
    DonViVanChuyen donViVanChuyen;

    String diaChiNhan;
    String sdtNhan;
    String tenNguoiNhan;
    String emailNguoiNhan;

    BigDecimal tongTien;
    BigDecimal giamGia;
    BigDecimal thucThu;
    BigDecimal phiShip;
    BigDecimal soTienCanThanhToan;
    BigDecimal soTienDaThanhToan;
    Date ngayXacNhan;
    Date ngayShip;
    Date ngayNhan;
    Timestamp ngayTao;
    Timestamp ngaySua;

    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTiet> hoaDonChiTiets = new ArrayList<>();

    @OneToMany(mappedBy = "hoaDon")
    private List<LichSuHoaDon> lichSuHoaDons = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "nguoi_tao")
    User nguoiTao;

    @OneToOne
    @JoinColumn(name = "nguoi_sua")
    User nguoiSua;

    @Enumerated(EnumType.STRING)
    TrangThaiHoaDon trangThai;

    @OneToOne
    @JoinColumn(name = "hinh_thuc_thanh_toan")
    HinhThucThanhToan hinhThucThanhToan;
}
