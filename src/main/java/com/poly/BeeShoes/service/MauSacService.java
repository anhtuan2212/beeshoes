package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.MauSac;

import java.util.List;

public interface MauSacService {
    MauSac save(MauSac mauSac);
    MauSac getById(Long id);
    MauSac getMauSacByMa(String ma);
    MauSac getByTen(String ten);
    List<MauSac> getAll();
    boolean delete(Long id);
    boolean existsByMaMauSac(String ma);

    boolean existsByTen(String ten,Long id);
}
