package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo,Long> {
}