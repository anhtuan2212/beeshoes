package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.MuiGiay;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.model.TheLoai;
import com.poly.BeeShoes.model.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SanPhamService {
    List<String> getListKichCo(Long id);
    SanPham save(SanPham sanPham);
    SanPham getById(Long id);
    SanPham getByTen(String name);
    boolean existsByTen(String name);
    List<SanPham> getAll();
    List<SanPham> getAllApi();
    boolean exitsByTheLoai(TheLoai theLoai);
    boolean exitsByThuongHieu(ThuongHieu th);
    Page<SanPham> getAllShop(Pageable pageable);
    boolean delete(Long id);
    List<SanPham> getSanPhamEmtyCTSP();
    List<SanPham> findTop4ByTheLoaiOrderByNgayTaoDesc(TheLoai theLoai);
    Map<String, Map<String, Long>> getKichCoCountByMauSac(Long id);
    Integer getCount();
}
