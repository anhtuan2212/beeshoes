package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.HinhThucThanhToan;

import java.util.List;

public interface HinhThucThanhToanService {

    List<HinhThucThanhToan> getAll();
    HinhThucThanhToan save(HinhThucThanhToan hinhThucThanhToan);
    HinhThucThanhToan getById(Long id);
    HinhThucThanhToan getByTen(String hinhThuc);
    boolean delete(Long id);
    HinhThucThanhToan getByHinhThuc(String hinhThuc);

}
