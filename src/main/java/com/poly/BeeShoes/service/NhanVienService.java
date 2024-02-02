package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.NhanVien;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> getAll();

    void delete(Long id);

    NhanVien detail(Long id);

    NhanVien add(NhanVien nhanVien);

    NhanVien update(NhanVien nhanVien, Long id);

    NhanVien getByMa(String ma);

    boolean existByMa(String ma);

    String generateEmployeeCode();
}
