package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac,Long> {
}
