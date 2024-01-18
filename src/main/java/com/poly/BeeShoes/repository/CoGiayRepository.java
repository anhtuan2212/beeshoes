package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.CoGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoGiayRepository extends JpaRepository<CoGiay,Long> {
}
