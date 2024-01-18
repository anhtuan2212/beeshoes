package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ThuongHieu;

import java.util.List;

public interface ThuongHieuService {
    ThuongHieu save(ThuongHieu thuongHieu);
    List<ThuongHieu> getAll();
    boolean delete(Long id);
}
