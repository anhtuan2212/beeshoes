package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac,Long> {
    boolean existsByMaMauSac(String ma);
    MauSac getMauSacByMaMauSac(String ma);
    MauSac getById(Long id);
    boolean existsByTen(String ten);

    MauSac getFirstByTen(String ten);
}
