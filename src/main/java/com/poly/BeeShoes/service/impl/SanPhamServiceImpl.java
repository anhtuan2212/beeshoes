package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.repository.SanPhamRepository;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SanPhamServiceImpl implements SanPhamService {
    private final SanPhamRepository sanPhamRepository;


    @Override
    public List<String> getListKichCo(Long id) {
        SanPham sp = sanPhamRepository.findById(id).orElse(null);
        List<String> lst = new ArrayList<>();
        if (sp != null) {
            for (ChiTietSanPham ctsp : sp.getChiTietSanPham()) {
                String kichCoTen = ctsp.getKichCo().getTen();
                if (!lst.contains(kichCoTen)) {
                    lst.add(kichCoTen);
                }
            }
        }
        return lst;
    }

    @Override
    public SanPham save(SanPham sanPham) {
        sanPham.setNgaySua(Timestamp.from(Instant.now()));
        return sanPhamRepository.save(sanPham);
    }
    @Override
    public SanPham getById(Long id) {
        Optional<SanPham> optionalSanPham = sanPhamRepository.findById(id);
        if (optionalSanPham.isPresent()) {
            SanPham sanPham = optionalSanPham.get();
            List<ChiTietSanPham> sortedChiTietSanPham = sanPham.getSortedChiTietSanPhamByMauSac();
            sanPham.setChiTietSanPham(sortedChiTietSanPham);
            return sanPham;
        } else {
            return null;
        }
    }
    @Override
    public Map<String, Map<String, Long>> getKichCoCountByMauSac(Long sanPhamId) {
        SanPham sanPham = getById(sanPhamId);
        Map<String, Map<String, Long>> result = new HashMap<>();

        if (sanPham != null && sanPham.getChiTietSanPham() != null) {
            for (ChiTietSanPham chiTietSanPham : sanPham.getChiTietSanPham()) {
                if (chiTietSanPham.getMauSac() != null && chiTietSanPham.getKichCo() != null) {
                    String mauSac = chiTietSanPham.getMauSac().getMaMauSac();
                    String kichCo = chiTietSanPham.getKichCo().getTen();
                    result.computeIfAbsent(mauSac, k -> new HashMap<>());
                    result.get(mauSac).merge(kichCo, 1L, Long::sum);
                }
            }
        }

        return result;
    }

    @Override
    public SanPham getByTen(String name) {
        return sanPhamRepository.getFirstByTen(name);
    }

    @Override
    public boolean existsByTen(String name) {
        return sanPhamRepository.existsByTen(name);
    }

    @Override
    public List<SanPham> getAll() {
        List<SanPham> sp = sanPhamRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
        for (int i = 0; i < sp.size(); i++) {
            SanPham s = sp.get(i);
            List<ChiTietSanPham> ctsp = s.getChiTietSanPham();
            int num = 0;
            BigDecimal gn =null;
            List<MauSac> lst = new ArrayList<>();
            for (int j = 0; j < ctsp.size(); j++) {
                ChiTietSanPham ct = ctsp.get(j);
                num += ct.getSoLuongTon();
                gn=ct.getGiaNhap();
                if (!lst.contains(ct.getMauSac())){
                    lst.add(ct.getMauSac());
                }
            }
            s.setMauSac(lst);
            s.setGiaNhap(gn);
            s.setSoLuong(num);
        }
        return sp;
    }

    @Override
    public boolean exitsByTheLoai(TheLoai theLoai) {
        return sanPhamRepository.existsByTheLoai(theLoai);
    }

    @Override
    public boolean exitsByThuongHieu(ThuongHieu th) {
        return sanPhamRepository.existsByThuongHieu(th);
    }

    @Override
    public Page<SanPham> getAllShop(Pageable pageable) {
        Page<SanPham> sp = sanPhamRepository.getAllByChiTietSanPhamExists(pageable);
        List<SanPham> spx = sp.getContent();
        if (!spx.isEmpty()){
            for (int i = 0; i < spx.size(); i++) {
                SanPham s = spx.get(i);
                List<ChiTietSanPham> ctsp = s.getChiTietSanPham();
                int num = 0;
                BigDecimal gn =null;
                boolean sale =false;
                List<MauSac> lst = new ArrayList<>();
                for (int j = 0; j < ctsp.size(); j++) {
                    ChiTietSanPham ct = ctsp.get(j);
                    if (ct.isSale()){
                        sale=true;
                    }
                    num += ct.getSoLuongTon();
                    gn=ct.getGiaNhap();
                    if (!lst.contains(ct.getMauSac())){
                        lst.add(ct.getMauSac());
                    }
                }
                sp.getContent().get(i).setSale(sale);
                sp.getContent().get(i).setMauSac(lst);
                sp.getContent().get(i).setGiaNhap(gn);
                sp.getContent().get(i).setSoLuong(num);
            }
        }

        return sp;
    }

    @Override
    public boolean delete(Long id) {
        SanPham sp = sanPhamRepository.findById(id).get();
        if (sp.getId()!=null){
            sanPhamRepository.deleteById(sp.getId());
            return true;
        }
        return false;
    }
}
