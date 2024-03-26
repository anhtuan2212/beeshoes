package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    @Override
    Optional<ThanhToan> findById(Long id);
}
