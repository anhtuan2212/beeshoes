package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.MuiGiay;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.model.TheLoai;
import com.poly.BeeShoes.model.ThuongHieu;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,Long> {
    @Query("SELECT sp FROM SanPham sp WHERE EXISTS (SELECT 1 FROM sp.chiTietSanPham ctsp) AND sp.trangThai=true")
    Page<SanPham> getAllByChiTietSanPhamExists(Pageable pageable);
    @Query("SELECT s FROM SanPham s LEFT JOIN FETCH s.chiTietSanPham ctsp LEFT JOIN FETCH ctsp.mauSac ms ORDER BY ms.maMauSac")
    SanPham findByIdWithSortedChiTietSanPham(Long id);
    SanPham getFirstByTen(String name);
    boolean existsByTheLoai(TheLoai theLoai);
    boolean existsByThuongHieu(ThuongHieu thuongHieu);
    boolean existsByTen(String ten);
}
