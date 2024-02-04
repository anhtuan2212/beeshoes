package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TheLoaiRepository extends JpaRepository<TheLoai,Long> {

    boolean existsByTen(String ten);


    TheLoai getFirstByTen(String ten);
    TheLoai getById(Long id);
}
