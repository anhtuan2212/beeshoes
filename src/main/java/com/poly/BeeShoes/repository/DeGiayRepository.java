package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.DeGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeGiayRepository extends JpaRepository<DeGiay,Long> {
    boolean existsByTen(String ten);
}
