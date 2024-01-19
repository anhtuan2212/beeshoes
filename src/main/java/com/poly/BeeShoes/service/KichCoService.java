package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.KichCo;

import java.util.List;

public interface KichCoService {
    KichCo save(KichCo kichCo);
    KichCo getById(Long id);
    List<KichCo> getAll();
    boolean delete(Long id);
}
