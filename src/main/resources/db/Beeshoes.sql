DROP DATABASE IF EXISTS lightbee;
CREATE DATABASE IF NOT EXISTS lightbee;
USE lightbee;
CREATE TABLE user (-- test
                      id bigint auto_increment,
                      email varchar(255) NOT NULL,
                      password varchar(255) NOT NULL,
                      avatar varchar(255) NULL,
                      id_khach_hang bigint,
                      id_nhan_vien bigint,
                      role VARCHAR(10),
                      primary key (id),
                      verify_at timestamp,
                      ngay_tao timestamp,
                      ngay_sua timestamp,
                      nguoi_tao bigint,
                      nguoi_sua bigint,
                      trang_thai tinyint check (trang_thai between 0 and 9)
) engine=INNODB;
-- CHỨC VỤ
CREATE TABLE chuc_vu (
                         id bigint auto_increment,
                         ten_chuc_vu nvarchar(255) NOT NULL,
                         ngay_tao timestamp,
                         ngay_sua timestamp,
                         nguoi_tao bigint,
                         nguoi_sua bigint,
                         trang_thai BIT DEFAULT 1, -- default = 1 = true
                         primary key (id)
) engine=INNODB;
-- NHÂN VIÊN
CREATE TABLE nhan_vien (
                           id bigint auto_increment,
                           ma_nhan_vien varchar(50) UNIQUE NOT NULL,
                           ho nvarchar(50) NOT NULL,
                           ten_dem nvarchar(50),
                           ten nvarchar(50) NOT NULL,
                           gioi_tinh BIT,
                           ngay_sinh DATE,
                           dia_chi nvarchar(255),
                           sdt varchar(15),
                           cccd varchar(15),
                           id_chuc_vu bigint,
                           ngay_tao timestamp,
                           ngay_sua timestamp,
                           nguoi_tao bigint,
                           nguoi_sua bigint,
                           trang_thai BIT DEFAULT 1, -- default = 1 = true
                           primary key (id),
                           FOREIGN KEY (`id_chuc_vu`) REFERENCES `chuc_vu`(id)
);
-- HẠNG KHÁCH HÀNG
CREATE TABLE hang_khach_hang(
                                id bigint auto_increment,
                                ten_hang nvarchar(50),
                                ma_hang varchar(10),
                                diem_toi_thieu int,
                                primary key(id)
);
-- KHÁCH HÀNG
CREATE TABLE khach_hang(
                           id bigint auto_increment,
                           ma_khach_hang varchar(10) UNIQUE NOT NULL,
                           ho nvarchar(20),
                           ten_dem nvarchar(20),
                           ten nvarchar(20),
                           gioi_tinh bit,
                           ngay_sinh timestamp,
                           sdt varchar(10),
                           diem int,
                           dia_chi_mac_dinh bigint,
                           id_hang_khach_hang bigint,
                           ngay_tao timestamp,
                           ngay_sua timestamp,
                           nguoi_tao bigint,
                           nguoi_sua bigint,
                           status bit default 1,
                           primary key(id),
                           foreign key(id_hang_khach_hang) references hang_khach_hang(id)
);

-- SET KHÓA NGOẠI CHO KH VÀ USER
ALTER TABLE user
    ADD CONSTRAINT fk_kh_user
        FOREIGN KEY (id_khach_hang)
            REFERENCES khach_hang (id);
-- SET KHÓA NGOẠI CHO NV VÀ USER
ALTER TABLE user
    ADD CONSTRAINT fk_NV_user
        FOREIGN KEY (id_nhan_vien)
            REFERENCES nhan_vien (id);
CREATE TABLE san_pham(
                         id bigint auto_increment,
                         ten nvarchar(256),
                         ngay_tao timestamp,
                         ngay_sua timestamp,
                         nguoi_tao bigint,
                         nguoi_sua bigint,
                         trang_thai bit default 1,
                         mo_ta nvarchar(256),
                         primary key(id),
                         foreign key(nguoi_tao) references user(id),
                         foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE anh(
                    id bigint auto_increment,
                    id_san_pham bigint,
                    url nvarchar(256),
                    main BIT DEFAULT 0,
                    ngay_tao timestamp,
                    ngay_sua timestamp,
                    nguoi_tao bigint,
                    nguoi_sua bigint,
                    trang_thai bit default 1,
                    primary key(id),
                    foreign key(id_san_pham) references san_pham(id),
                    foreign key(nguoi_tao) references user(id),
                    foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE thuong_hieu(
                            id bigint auto_increment,
                            ten nvarchar(256),
                            ngay_tao timestamp,
                            ngay_sua timestamp,
                            nguoi_tao bigint,
                            nguoi_sua bigint,
                            trang_thai bit default 1,
                            primary key(id),
                            foreign key(nguoi_tao) references user(id),
                            foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE de_giay(
                        id bigint auto_increment,
                        ten nvarchar(256),
                        ngay_tao timestamp,
                        ngay_sua timestamp,
                        nguoi_tao bigint,
                        nguoi_sua bigint,
                        trang_thai bit default 1,
                        primary key(id),
                        foreign key(nguoi_tao) references user(id),
                        foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE mau_sac(
                        id bigint auto_increment,
                        ten nvarchar(256),
                        ma_mau_sac varchar(20),
                        ngay_tao timestamp,
                        ngay_sua timestamp,
                        nguoi_tao bigint,
                        nguoi_sua bigint,
                        trang_thai bit default 1,
                        primary key(id),
                        foreign key(nguoi_tao) references user(id),
                        foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE kich_co(
                        id bigint auto_increment,
                        ten nvarchar(256),
                        ngay_tao timestamp,
                        ngay_sua timestamp,
                        nguoi_tao bigint,
                        nguoi_sua bigint,
                        trang_thai bit default 1,
                        primary key(id),
                        foreign key(nguoi_tao) references user(id),
                        foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE the_loai(
                         id bigint auto_increment,
                         ten_the_loai nvarchar(256),
                         ngay_tao timestamp,
                         ngay_sua timestamp,
                         nguoi_tao bigint,
                         nguoi_sua bigint,
                         trang_thai bit default 1,
                         primary key(id),
                         foreign key(nguoi_tao) references user(id),
                         foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE chat_lieu(
                          id bigint auto_increment,
                          ten nvarchar(256),
                          ngay_tao timestamp,
                          ngay_sua timestamp,
                          nguoi_tao bigint,
                          nguoi_sua bigint,
                          trang_thai bit default 1,
                          primary key(id),
                          foreign key(nguoi_tao) references user(id),
                          foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE co_giay(
                        id bigint auto_increment,
                        ten nvarchar(256),
                        ngay_tao timestamp,
                        ngay_sua timestamp,
                        nguoi_tao bigint,
                        nguoi_sua bigint,
                        trang_thai bit default 1,
                        primary key(id),
                        foreign key(nguoi_tao) references user(id),
                        foreign key(nguoi_sua) references user(id)
)engine=INNODB;

CREATE TABLE mui_giay(
                         id bigint auto_increment,
                         ten nvarchar(256),
                         ngay_tao timestamp,
                         ngay_sua timestamp,
                         nguoi_tao bigint,
                         nguoi_sua bigint,
                         trang_thai bit default 1,
                         primary key(id),
                         foreign key(nguoi_tao) references user(id),
                         foreign key(nguoi_sua) references user(id)
)engine=INNODB;



CREATE TABLE voucher (
                         id bigint auto_increment,
                         ma varchar(50) NOT NULL,
                         ten nvarchar(100) NOT NULL,
                         loai_voucher nvarchar(100) NOT NULL,
                         ngay_bat_dau DATETIME,
                         ngay_ket_thuc DATETIME,
                         gia_tri_tien_mat Decimal(8,0),
                         gia_tri_phan_tram tinyint check (gia_tri_phan_tram between 0 and 100),
                         gia_tri_toi_da Decimal(8,0),
                         hang_khach_hang nvarchar(100) NULL,
                         dieu_kien nvarchar(100) NOT NULL,
                         so_luong int ,
                         mo_ta nvarchar(100),
                         ngay_tao timestamp,
                         ngay_sua timestamp,
                         nguoi_tao bigint,
                         nguoi_sua bigint,
                         trang_thai BIT DEFAULT 1, -- default = 1 = true
                         primary key (id),
                         foreign key (nguoi_tao) references user(id),
                         foreign key (nguoi_sua) references user(id)
) engine=INNODB;

CREATE TABLE voucher_khach_hang (
                                    id bigint auto_increment,
                                    id_khach_hang bigint NOT NULL,
                                    id_voucher bigint NOT NULL,
                                    ngay_tao timestamp,
                                    ngay_sua timestamp,
                                    nguoi_tao bigint,
                                    nguoi_sua bigint,
                                    status BIT DEFAULT 1, -- default = 1 = true
                                    primary key (id),
                                    foreign key (id_khach_hang) references khach_hang(id),
                                    foreign key (id_voucher) references voucher(id),
                                    foreign key (nguoi_tao) references user(id),
                                    foreign key (nguoi_sua) references user(id)
) engine=INNODB;

CREATE TABLE thong_tin_thanh_toan (
                                      id bigint auto_increment,
                                      id_khach_hang bigint NOT NULL,
                                      loai_thanh_toan nvarchar(255) NOT NULL,
                                      chi_nhanh nvarchar(255) NOT NULL,
                                      ten_chu_tai_khoan nvarchar(255) NOT NULL,
                                      so_tai_khoan varchar(20) NOT NULL,
                                      han_the DATE,
                                      CCV VARCHAR(4),
                                      ten_ngan_hang nvarchar(255) NOT NULL,
                                      ngay_tao timestamp,
                                      ngay_sua timestamp,
                                      nguoi_tao bigint,
                                      nguoi_sua bigint,
                                      trang_thai tinyint check (trang_thai between 0 and 9),-- default = 1 = true
                                      foreign key (id_khach_hang) references khach_hang(id),
                                      primary key (id),
                                      foreign key (nguoi_tao) references user(id),
                                      foreign key (nguoi_sua) references user(id)
) engine=INNODB;

create table dia_chi(
                        id bigint auto_increment,
                        id_khach_hang bigint,
                        so_nha varchar(225),
                        phuong_xa nvarchar(100),
                        quan_huyen nvarchar(100),
                        thanh_pho nvarchar(100),
                        zip_code VARCHAR(50),
                        ngay_tao timestamp,
                        ngay_sua timestamp,
                        nguoi_tao bigint,
                        nguoi_sua bigint,
                        trang_thai tinyint check (trang_thai between 0 and 9),-- default = 1 = true
                        primary key(id),
                        foreign key(id_khach_hang) references khach_hang(id),
                        foreign key(nguoi_tao) references user(id),
                        foreign key(nguoi_sua) references user(id)
);
CREATE TABLE don_vi_van_chuyen (
                                   id bigint AUTO_INCREMENT,
                                   ma_don_vi varchar(50) UNIQUE NOT NULL,
                                   ten_don_vi nvarchar(255) NOT NULL,
                                   api JSON NOT NULL,
                                   token_api varchar(225),
                                   dia_chi nvarchar(255),
                                   ngay_tao timestamp,
                                   ngay_sua timestamp,
                                   nguoi_tao bigint,
                                   nguoi_sua bigint,
                                   trang_thai tinyint check (trang_thai between 0 and 9),-- default = 1 = true
                                   primary key (id),
                                   foreign key(nguoi_tao) references user(id),
                                   foreign key(nguoi_sua) references user(id)
) engine=INNODB;

CREATE TABLE hinh_thuc_thanh_toan (
                                      id bigint AUTO_INCREMENT,
                                      ten_hinh_thuc nvarchar(255) NOT NULL,
                                      mo_ta nvarchar(255) NOT NULL,
                                      ngay_tao timestamp,
                                      ngay_sua timestamp,
                                      nguoi_tao bigint,
                                      nguoi_sua bigint,
                                      trang_thai tinyint check (trang_thai between 0 and 9),-- default = 1 = true
                                      primary key (id),
                                      foreign key(nguoi_tao) references user(id),
                                      foreign key(nguoi_sua) references user(id)
) engine=INNODB;


CREATE TABLE hoa_don (
                         id bigint AUTO_INCREMENT,
                         id_nhan_vien bigint NOT NULL,
                         id_khach_hang bigint NOT NULL,
                         ma_hoa_don varchar(50) UNIQUE NOT NULL,
                         id_voucher bigint NOT NULL,
                         id_thanh_toan bigint NULL,
                         id_don_vi_van_chuyen bigint NOT NULL,
                         id_thong_tin_thanh_toan BIGINT NOT NULL,
                         tong_tien decimal(10,0),
                         thuc_thu decimal(10,0),
                         phi_ship decimal(10,0),
                         ngay_xac_nhan datetime,
                         ngay_ship datetime,
                         ngay_nhan datetime,
                         ngay_tao timestamp,
                         ngay_sua timestamp,
                         nguoi_tao bigint,
                         nguoi_sua bigint,
                         trang_thai tinyint check (trang_thai between 0 and 9),-- default = 1 = true
                         primary key (id),
                         foreign key (id_nhan_vien) references nhan_vien(id),
                         foreign key (id_thong_tin_thanh_toan) references thong_tin_thanh_toan(id),
                         foreign key (id_voucher) references voucher(id),
                         foreign key (id_khach_hang) references khach_hang(id),
                         foreign key (id_don_vi_van_chuyen) references don_vi_van_chuyen(id),
                         foreign key(nguoi_tao) references user(id),
                         foreign key(nguoi_sua) references user(id)
) engine=INNODB;

CREATE TABLE thanh_toan (
                            id bigint AUTO_INCREMENT,
                            id_hoa_don bigint NOT NULL,
                            id_hinh_thuc_thanh_toan bigint NOT NULL,
                            mo_ta nvarchar(255) NULL,
                            ngay_tao timestamp,
                            ngay_sua timestamp,
                            nguoi_tao bigint,
                            nguoi_sua BIGINT,
                            trang_thai tinyint DEFAULT 1 check (trang_thai between 0 and 9),
                            primary key (id),
                            foreign key(id_hoa_don) references hoa_don(id),
                            foreign key(id_hinh_thuc_thanh_toan) references hinh_thuc_thanh_toan(id),
                            foreign key(nguoi_tao) references user(id),
                            foreign key(nguoi_sua) references user(id)
)engine=INNODB;
ALTER TABLE hoa_don
    ADD FOREIGN KEY (id_thanh_toan) REFERENCES thanh_toan(id);

CREATE TABLE lich_su_hoa_don (
                                 id bigint AUTO_INCREMENT,
                                 id_hoa_don bigint NOT NULL,
                                 mota_hanh_dong nvarchar(256),
                                 thoi_gian timestamp,
                                 nguoi_thuc_hien bigint,
                                 nguoi_tao bigint,
                                 nguoi_sua bigint,
                                 primary key(id),
                                 foreign key (id_hoa_don) references hoa_don(id),
                                 foreign key(nguoi_thuc_hien) references user(id),
                                 foreign key(nguoi_tao) references user(id),
                                 foreign key(nguoi_sua) references user(id)
) engine=INNODB;


create table chi_tiet_san_pham(
                                  id bigint AUTO_INCREMENT,
                                  ma_san_pham  varchar(50) UNIQUE NOT NULL,
                                  id_thuong_hieu bigint NOT NULL,
                                  id_san_pham bigint NOT NULL,
                                  id_mau_sac bigint NOT NULL,
                                  id_de_giay bigint NOT NULL,
                                  id_kich_co bigint NOT NULL,
                                  id_chat_lieu bigint NOT NULL,
                                  id_the_loai bigint NOT NULL,
                                  id_co_giay bigint NOT NULL,
                                  id_mui_giay bigint NOT NULL,
                                  ma_van_chuyen nvarchar(255),
                                  giam_phan_tram tinyint check (giam_phan_tram between 0 and 100),
                                  giam_tien decimal(11, 2),
                                  gia_nhap decimal(11, 2),
                                  gia_goc decimal(11, 2),
                                  gia_ban decimal(11, 2),
                                  so_luong_nhap int,
                                  so_luong_ton int,
                                  ngay_tao timestamp,
                                  ngay_sua timestamp,
                                  nguoi_tao bigint,
                                  nguoi_sua bigint,
                                  trang_thai tinyint check (trang_thai between 0 and 9),-- default = 1 = true
                                  primary key (id),
                                  foreign key(id_thuong_hieu) references thuong_hieu(id),
                                  foreign key(id_san_pham) references san_pham(id),
                                  foreign key(id_mau_sac) references mau_sac(id),
                                  foreign key(id_de_giay) references de_giay(id),
                                  foreign key(id_kich_co) references kich_co(id),
                                  foreign key(id_chat_lieu) references chat_lieu(id),
                                  foreign key(id_the_loai) references the_loai(id),
                                  foreign key(id_chat_lieu) references chat_lieu(id),
                                  foreign key(id_mui_giay) references mui_giay(id),
                                  foreign key(nguoi_tao) references user(id),
                                  foreign key(nguoi_sua) references user(id)
) engine=INNODB;


CREATE TABLE gio_hang (
                          id bigint AUTO_INCREMENT,
                          id_khach_hang bigint NOT NULL,
                          id_chi_tiet_san_pham bigint NOT NULL,
                          ngay_tao timestamp,
                          ngay_sua timestamp,
                          nguoi_tao bigint,
                          nguoi_sua bigint,
                          primary key(id),
                          foreign key (id_khach_hang) references khach_hang(id),
                          foreign key (id_chi_tiet_san_pham) references chi_tiet_san_pham(id),
                          foreign key(nguoi_tao) references user(id),
                          foreign key(nguoi_sua) references user(id)
) engine=INNODB;

CREATE TABLE hoa_don_chi_tiet (
                                  id bigint AUTO_INCREMENT,
                                  id_hoa_don bigint NOT NULL,
                                  id_chi_tiet_san_pham  bigint NOT NULL,
                                  so_luong INT,
                                  ngay_tao timestamp,
                                  ngay_sua timestamp,
                                  nguoi_tao bigint,
                                  nguoi_sua bigint,
                                  PRIMARY KEY(id),
                                  foreign key (id_hoa_don) references hoa_don(id),
                                  foreign key (id_chi_tiet_san_pham) references chi_tiet_san_pham(id),
                                  foreign key(nguoi_tao) references user(id),
                                  foreign key(nguoi_sua) references user(id)
) engine=INNODB;

ALTER TABLE user MODIFY column role varchar(10);
ALTER TABLE user MODIFY column avatar varchar(255) null;
ALTER TABLE user MODIFY column trang_thai bit default 1;
-- SET KHÓA NGOẠI CHO KH VÀ USER
ALTER TABLE user
DROP CONSTRAINT fk_kh_user;
-- SET KHÓA NGOẠI CHO NV VÀ USER
ALTER TABLE user
DROP CONSTRAINT fk_NV_user;

-- dia_chiALTER TABLE user DROP CONSTRAINT user_chk_1;
-- ALTER TABLE user DROP CONSTRAINT user_chk_2;

-- SET KHÓA NGOẠI CHO KH VÀ USER
ALTER TABLE user ADD foreign key(id_nhan_vien) references nhan_vien(id);
ALTER TABLE user ADD foreign key(id_khach_hang) references khach_hang(id);
