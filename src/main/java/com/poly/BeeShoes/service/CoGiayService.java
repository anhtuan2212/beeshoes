package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.CoGiay;

import java.util.List;

public interface CoGiayService {
    CoGiay save(CoGiay coGiay);
    List<CoGiay> getAll();
    boolean delete(Long id);
}
