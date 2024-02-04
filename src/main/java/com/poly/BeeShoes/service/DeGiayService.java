package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.DeGiay;
import java.util.List;

public interface DeGiayService {
    DeGiay save(DeGiay deGiay);
    DeGiay getById(Long id);
    DeGiay getByTen(String ten);
    List<DeGiay> getAll();
    boolean delete(Long id);

    boolean existsByTen(String ten);
}
