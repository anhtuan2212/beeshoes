package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.TheLoai;

import java.util.List;

public interface TheLoaiService {
    TheLoai save(TheLoai theLoai);
    List<TheLoai> getAll();
    boolean delete(Long id);
}
