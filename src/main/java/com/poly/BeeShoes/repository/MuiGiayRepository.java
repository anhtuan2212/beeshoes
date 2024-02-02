package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.MuiGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuiGiayRepository extends JpaRepository<MuiGiay,Long> {
    boolean existsByTen(String ten);
}
