package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.MauSac;

import java.util.List;

public interface MauSacService {
    MauSac save(MauSac mauSac);
    MauSac getMauSacByMa(String ma);
    List<MauSac> getAll();
    boolean delete(Long id);
    boolean existsByMaMauSac(String ma);

}
