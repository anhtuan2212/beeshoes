package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu,Long> {
    boolean existsByTen(String ten);
    ThuongHieu getFirstByTen(String ten);
}
