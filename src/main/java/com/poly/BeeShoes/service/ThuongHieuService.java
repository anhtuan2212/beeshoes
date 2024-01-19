package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ThuongHieu;

import java.util.List;

public interface ThuongHieuService {
    ThuongHieu save(ThuongHieu thuongHieu);
    ThuongHieu getById(Long id);
    List<ThuongHieu> getAll();
    boolean delete(Long id);
}
