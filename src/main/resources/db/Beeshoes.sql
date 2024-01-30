DROP DATABASE IF EXISTS lightbee;
CREATE DATABASE lightbee;
USE lightbee;

-- bỏ table thông tin thanh toán vì nếu call api từ sanbox của vnpay thì sẽ được cấp thông tin thẻ.

-- Sản Phẩm (nguoi_tao, nguoi_sua) //done
CREATE TABLE san_pham
(
    id bigint auto_increment,
    id_thuong_hieu bigint,
    id_the_loai bigint,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    mo_ta nvarchar(999),
    primary key(id)
)engine=INNODB;

-- Ảnh (id_san_pham, nguoi_tao, nguoi_sua) //done
CREATE TABLE anh
(
    id bigint auto_increment,
    id_san_pham bigint,
    url nvarchar(256),
    main bit default 0,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Thương Hiệu (nguoi_tao, nguoi_sua) //done
CREATE TABLE thuong_hieu
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Đế Giày (nguoi_tao, nguoi_sua) //done
CREATE TABLE de_giay
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Màu Sắc (nguoi_tao, nguoi_sua) //done
CREATE TABLE mau_sac
(
    id bigint auto_increment,
    ten nvarchar(256),
    ma_mau_sac varchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Kích Cỡ (nguoi_tao, nguoi_sua) //done
CREATE TABLE kich_co
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Thể Loại (nguoi_tao, nguoi_sua) //done
CREATE TABLE the_loai
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Chất Liệu (nguoi_tao, nguoi_sua) //done
CREATE TABLE chat_lieu
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Cỡ Giày (nguoi_tao, nguoi_sua) //done
CREATE TABLE co_giay
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Mũi Giày (nguoi_tao, nguoi_sua) //done
CREATE TABLE mui_giay
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Chi Tiết Sản Phẩm (id_san_pham, id_thuong_hieu, id_mau_sac,
-- id_de_giay, id_kich_co, id_chat_lieu, id_the_loai, id_co_giay, id_mui_giay, nguoi_tao, nguoi_sua) //done
CREATE TABLE chi_tiet_san_pham
(
    id bigint auto_increment,
    id_san_pham bigint,
    id_mau_sac bigint,
    id_de_giay bigint,
    id_kich_co bigint,
    id_chat_lieu bigint,
    id_co_giay bigint,
    id_anh bigint,
    id_mui_giay bigint,
    ma_san_pham varchar(256),
    ma_van_chuyen varchar(256),
    gia_nhap decimal(11, 2),
    gia_goc decimal(11, 2),
    gia_ban decimal(11, 2),
    is_sale bit default 0, -- 0 = false
    giam_phan_tram int default 0,
    giam_tien decimal(11, 2),
    so_luong_ton int,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Chức Vụ (nguoi_tao, nguoi_sua) //done
CREATE TABLE chuc_vu
(
    id bigint auto_increment,
    ten nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Nhân Viên (id_chuc_vu, nguoi_tao, nguoi_sua) //done
CREATE TABLE nhan_vien
(
    id bigint auto_increment,
    ma_nhan_vien varchar(256),
    id_chuc_vu bigint,
    ten nvarchar(256),
    ten_dem nvarchar(256),
    ho nvarchar(256),
    gioi_tinh bit,
    ngay_sinh date,
    dia_chi nvarchar(256),
    sdt varchar(10),
    cccd varchar(12),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Địa Chỉ (id_khach_hang, nguoi_tao, nguoi_sua) //done
CREATE TABLE dia_chi
(
    id bigint auto_increment,
    id_khach_hang bigint,
    so_nha nvarchar(256),
    phuong_xa nvarchar(256),
    quan_huyen nvarchar(256),
    tinh_thanh_pho nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Khách Hàng (id_hang_khach_hang, dia_chi_mac_dinh, nguoi_tao, nguoi_sua) //done
CREATE TABLE khach_hang
(
    id bigint auto_increment,
    ma_khach_hang varchar(256),
    ten nvarchar(256),
    ten_dem nvarchar(256),
    ho nvarchar(256),
    gioi_tinh bit,
    ngay_sinh date,
    sdt varchar(10),
    diem int,
    id_hang_khach_hang bigint,
    dia_chi_mac_dinh bigint,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Hạng Khách Hàng
CREATE TABLE hang_khach_hang
(
    id bigint auto_increment,
    ten nvarchar(256),
    diem_toi_thieu int,
    primary key(id)
)engine=INNODB;

-- User (id_khach_hang, id_nhan_vien, nguoi_tao, nguoi_sua) //done
CREATE TABLE user
(
    id bigint auto_increment,
    email varchar(256),
    password varchar(999),
    role nvarchar(256),
    id_khach_hang bigint,
    id_nhan_vien bigint,
    verify_at timestamp,
    avatar varchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Đơn Vị Vận Chuyển (nguoi_tao, nguoi_sua) //done
CREATE TABLE don_vi_van_chuyen
(
    id bigint auto_increment,
    ma_don_vi varchar(256),
    ten_don_vi nvarchar(256),
    dia_chi nvarchar(256),
    api json,
    token_api varchar(999),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Voucher (id_hang_khach_hang, nguoi_tao, nguoi_sua) //done
CREATE TABLE vouchers -- bỏ field loai_voucher
(
    id bigint auto_increment,
    ma varchar(256),
    ten nvarchar(256),
    gia_tri_tien_mat decimal(11, 2),
    gia_tri_phan_tram int,
    gia_tri_toi_da decimal(11, 2),
    id_hang_khach_hang bigint,
    dieu_kien nvarchar(256),
    so_luong int,
    ngay_bat_dau timestamp,
    ngay_ket_thuc timestamp,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    mo_ta nvarchar(256),
    primary key(id)
)engine=INNODB;

-- Vouchers cho khách hàng (id_khach_hang, id_vou_cher, nguoi_tao, nguoi_sua) //done
CREATE TABLE voucher_khach_hang
(
    id bigint auto_increment,
    id_khach_hang bigint,
    id_voucher bigint,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Hóa Đơn (id_nhan_vien, id_khach_hang, id_voucher, id_don_vi_van_chuyen, id_thanh_toan, nguoi_tao, nguoi_sua) //done
CREATE TABLE hoa_don -- bỏ field deleted
(
    id bigint auto_increment,
    ma_hoa_don varchar(256),
    loai_hoa_don bit,
    id_nhan_vien bigint,
    id_khach_hang bigint,
    id_voucher bigint,
    id_don_vi_van_chuyen bigint,
    tong_tien decimal(11, 2),
    thuc_thu decimal(11, 2),
    phi_ship decimal(11, 2),
    ngay_xac_nhan timestamp,
    ngay_ship timestamp,
    ngay_nhan timestamp,
    sdt_nhan varchar(10),
    dia_chi_nhan nvarchar(256),
    id_thanh_toan bigint,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Hóa Đơn Chi Tiết (id_hoa_don, id_chi_tiet_san_pham) //done
CREATE TABLE hoa_don_chi_tiet
(
    id bigint auto_increment,
    id_hoa_don bigint,
    id_chi_tiet_san_pham bigint,
    so_luong int,
    primary key(id)
)engine=INNODB;

-- Lịch Sử Hóa Đơn (id_hoa_don, nguoi_thuc_hien) //done
CREATE TABLE lich_su_hoa_don
(
    id bigint auto_increment,
    id_hoa_don bigint,
    hanh_dong nvarchar(256),
    thoi_gian timestamp,
    nguoi_thuc_hien bigint,
    primary key(id)
)engine=INNODB;

-- Hình Thức Thanh Toán (nguoi_tao, nguoi_sua) //done
CREATE TABLE hinh_thuc_thanh_toan
(
    id bigint auto_increment,
    ma_giao_dich varchar(256),
    hinh_thuc bit, -- 2 hinh thuc thanh toan khi nhan hang + thanh toan online (vnpay or momo)
    mo_ta nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Thanh Toán (id_hoa_don, id_hinh_thuc_thanh_toan, nguoi_tao, nguoi_sua) //done
CREATE TABLE thanh_toan
(
    id bigint auto_increment,
    id_hoa_don bigint,
    id_hinh_thuc_thanh_toan bigint,
    mo_ta nvarchar(256),
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Giỏ Hàng (id_khach_hang, nguoi_tao, nguoi_sua) //done
CREATE TABLE gio_hang
(
    id bigint auto_increment,
    id_khach_hang bigint,
    ngay_tao timestamp,
    ngay_sua timestamp,
    nguoi_tao bigint,
    nguoi_sua bigint,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Giỏ Hàng Chi Tiết (id_gio_hang, id_chi_tiet_san_pham)
CREATE TABLE gio_hang_chi_tiet
(
    id bigint auto_increment,
    id_gio_hang bigint,
    id_chi_tiet_san_pham bigint,
    so_luong int,
    ngay_tao timestamp,
    ngay_sua timestamp,
    trang_thai bit default 1,
    primary key(id)
)engine=INNODB;

-- Ràng Buộc, Liên Kết
-- Sản Phẩm (nguoi_tao, nguoi_sua)
ALTER TABLE san_pham ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE san_pham ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);
ALTER TABLE san_pham ADD FOREIGN KEY(id_thuong_hieu) REFERENCES thuong_hieu(id);
ALTER TABLE san_pham ADD FOREIGN KEY(id_the_loai) REFERENCES the_loai(id);

-- Ảnh (id_san_pham, nguoi_tao, nguoi_sua)
ALTER TABLE anh ADD FOREIGN KEY(id_san_pham) REFERENCES san_pham(id);
ALTER TABLE anh ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE anh ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Thương Hiệu (nguoi_tao, nguoi_sua)
ALTER TABLE thuong_hieu ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE thuong_hieu ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Đế Giày (nguoi_tao, nguoi_sua)
ALTER TABLE de_giay ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE de_giay ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Màu Sắc (nguoi_tao, nguoi_sua)
ALTER TABLE mau_sac ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE mau_sac ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Kích Cỡ (nguoi_tao, nguoi_sua)
ALTER TABLE kich_co ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE kich_co ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Thể Loại (nguoi_tao, nguoi_sua)
ALTER TABLE the_loai ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE the_loai ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Chất Liệu (nguoi_tao, nguoi_sua)
ALTER TABLE chat_lieu ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE chat_lieu ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Cỡ Giày (nguoi_tao, nguoi_sua)
ALTER TABLE co_giay ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE co_giay ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Mũi Giày (nguoi_tao, nguoi_sua)
ALTER TABLE mui_giay ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE mui_giay ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Chi Tiết Sản Phẩm (id_san_pham, id_thuong_hieu, id_mau_sac,
-- id_de_giay, id_kich_co, id_chat_lieu, id_the_loai, id_co_giay, id_mui_giay, nguoi_tao, nguoi_sua)
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_san_pham) REFERENCES san_pham(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_mau_sac) REFERENCES mau_sac(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_anh) REFERENCES anh(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_de_giay) REFERENCES de_giay(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_kich_co) REFERENCES kich_co(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_chat_lieu) REFERENCES chat_lieu(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_co_giay) REFERENCES co_giay(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(id_mui_giay) REFERENCES mui_giay(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE chi_tiet_san_pham ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Chức Vụ (nguoi_tao, nguoi_sua)
ALTER TABLE chuc_vu ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE chuc_vu ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Nhân Viên (id_chuc_vu, nguoi_tao, nguoi_sua)
ALTER TABLE nhan_vien ADD FOREIGN KEY(id_chuc_vu) REFERENCES chuc_vu(id);
ALTER TABLE nhan_vien ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE nhan_vien ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Địa Chỉ (id_khach_hang, nguoi_tao, nguoi_sua
ALTER TABLE dia_chi ADD FOREIGN KEY(id_khach_hang) REFERENCES khach_hang(id);
ALTER TABLE dia_chi ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE dia_chi ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Khách Hàng (id_hang_khach_hang, dia_chi_mac_dinh, nguoi_tao, nguoi_sua)
ALTER TABLE khach_hang ADD FOREIGN KEY(id_hang_khach_hang) REFERENCES hang_khach_hang(id);
ALTER TABLE khach_hang ADD FOREIGN KEY(dia_chi_mac_dinh) REFERENCES dia_chi(id);
ALTER TABLE khach_hang ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE khach_hang ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Users (id_khach_hang, id_nhan_vien, nguoi_tao, nguoi_sua)
ALTER TABLE users ADD FOREIGN KEY(id_khach_hang) REFERENCES khach_hang(id);
ALTER TABLE users ADD FOREIGN KEY(id_nhan_vien) REFERENCES nhan_vien(id);
ALTER TABLE users ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE users ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Đơn Vị Vận Chuyển (nguoi_tao, nguoi_sua)
ALTER TABLE don_vi_van_chuyen ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE don_vi_van_chuyen ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Voucher (id_hang_khach_hang, nguoi_tao, nguoi_sua)
ALTER TABLE vouchers ADD FOREIGN KEY(id_hang_khach_hang) REFERENCES nhan_vien(id);
ALTER TABLE vouchers ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE vouchers ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Vouchers cho khách hàng (id_khach_hang, id_vou_cher, nguoi_tao, nguoi_sua)
ALTER TABLE voucher_khach_hang ADD FOREIGN KEY(id_khach_hang) REFERENCES khach_hang(id);
ALTER TABLE voucher_khach_hang ADD FOREIGN KEY(id_voucher) REFERENCES vouchers(id);
ALTER TABLE voucher_khach_hang ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE voucher_khach_hang ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Hóa Đơn (id_nhan_vien, id_khach_hang, id_voucher, id_don_vi_van_chuyen, id_thanh_toan, nguoi_tao, nguoi_sua)
ALTER TABLE hoa_don ADD FOREIGN KEY(id_nhan_vien) REFERENCES nhan_vien(id);
ALTER TABLE hoa_don ADD FOREIGN KEY(id_khach_hang) REFERENCES khach_hang(id);
ALTER TABLE hoa_don ADD FOREIGN KEY(id_voucher) REFERENCES vouchers(id);
ALTER TABLE hoa_don ADD FOREIGN KEY(id_don_vi_van_chuyen) REFERENCES don_vi_van_chuyen(id);
ALTER TABLE hoa_don ADD FOREIGN KEY(id_thanh_toan) REFERENCES thanh_toan(id);
ALTER TABLE hoa_don ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE hoa_don ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Hóa Đơn Chi Tiết (id_hoa_don, id_chi_tiet_san_pham)
ALTER TABLE hoa_don_chi_tiet ADD FOREIGN KEY(id_hoa_don) REFERENCES hoa_don(id);
ALTER TABLE hoa_don_chi_tiet ADD FOREIGN KEY(id_chi_tiet_san_pham) REFERENCES chi_tiet_san_pham(id);

-- Lịch Sử Hóa Đơn (id_hoa_don, nguoi_thuc_hien)
ALTER TABLE lich_su_hoa_don ADD FOREIGN KEY(id_hoa_don) REFERENCES hoa_don(id);
ALTER TABLE lich_su_hoa_don ADD FOREIGN KEY(nguoi_thuc_hien) REFERENCES users(id);

-- Hình Thức Thanh Toán (nguoi_tao, nguoi_sua)
ALTER TABLE hinh_thuc_thanh_toan ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE hinh_thuc_thanh_toan ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Thanh Toán (id_hoa_don, id_hinh_thuc_thanh_toan, nguoi_tao, nguoi_sua)
ALTER TABLE thanh_toan ADD FOREIGN KEY(id_hoa_don) REFERENCES hoa_don(id);
ALTER TABLE thanh_toan ADD FOREIGN KEY(id_hinh_thuc_thanh_toan) REFERENCES hinh_thuc_thanh_toan(id);
ALTER TABLE thanh_toan ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE thanh_toan ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Giỏ Hàng (id_khach_hang, nguoi_tao, nguoi_sua)
ALTER TABLE gio_hang ADD FOREIGN KEY(id_khach_hang) REFERENCES khach_hang(id);
ALTER TABLE gio_hang ADD FOREIGN KEY(nguoi_tao) REFERENCES users(id);
ALTER TABLE gio_hang ADD FOREIGN KEY(nguoi_sua) REFERENCES users(id);

-- Giỏ Hàng Chi Tiết (id_gio_hang, id_chi_tiet_san_pham)
ALTER TABLE gio_hang_chi_tiet ADD FOREIGN KEY(id_gio_hang) REFERENCES gio_hang(id);
ALTER TABLE gio_hang_chi_tiet ADD FOREIGN KEY(id_chi_tiet_san_pham) REFERENCES chi_tiet_san_pham(id);