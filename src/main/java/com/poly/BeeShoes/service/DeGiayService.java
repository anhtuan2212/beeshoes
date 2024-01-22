package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.DeGiay;
import java.util.List;

public interface DeGiayService {
    DeGiay save(DeGiay deGiay);
    DeGiay getById(Long id);
    List<DeGiay> getAll();
    boolean delete(Long id);
}
