package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.CoGiay;

import java.util.List;

public interface CoGiayService {
    CoGiay save(CoGiay coGiay);
    CoGiay getById(Long id);
    List<CoGiay> getAll();
    boolean delete(Long id);
    boolean existsByTen(String ten);
}
