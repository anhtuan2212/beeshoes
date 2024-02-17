package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherResponsitory extends JpaRepository<Voucher,Long> {
  @Query("select c  from Voucher c  where c.ma  like %?1% or c.ten like %?1%" +
          "or c.loaiVoucher like %?1% or c.dieuKien  like %?1% ")
    List<Voucher> searchVC(String key);

//  Optional<Voucher> findByMavoucher(String maVoucher);
//  boolean existsByMaVoucher(String maVoucher);



}
