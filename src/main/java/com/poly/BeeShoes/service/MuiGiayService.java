package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.MauSac;
import com.poly.BeeShoes.model.MuiGiay;

import java.util.List;

public interface MuiGiayService {
    MuiGiay save(MuiGiay muiGiay);
    MuiGiay getById(Long id);
    List<MuiGiay> getAll();
    boolean delete(Long id);

    boolean existsByTen(String ten);
}
