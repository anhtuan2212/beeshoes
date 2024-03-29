package com.poly.BeeShoes.repository;

import com.poly.BeeShoes.model.CoGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoGiayRepository extends JpaRepository<CoGiay,Long> {
    boolean existsByTen(String Ten);
    CoGiay getFirstByTen(String ten);
//    boolean existsByTenIgnoreCaseAndTrangThai(String tenChuanHoa, boolean b);
}
