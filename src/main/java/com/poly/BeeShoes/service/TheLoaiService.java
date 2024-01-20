package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.TheLoai;

import java.util.List;

public interface TheLoaiService {
    TheLoai save(TheLoai theLoai);
    TheLoai getById(Long id);
    List<TheLoai> getAll();
    boolean delete(Long id);
}