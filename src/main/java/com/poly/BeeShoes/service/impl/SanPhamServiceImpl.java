package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.MauSac;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.repository.SanPhamRepository;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamServiceImpl implements SanPhamService {
    private final SanPhamRepository sanPhamRepository;
    @Override
    public SanPham save(SanPham sanPham) {
        sanPham.setNgaySua(Timestamp.from(Instant.now()));
        return sanPhamRepository.save(sanPham);
    }

    @Override
    public SanPham getById(Long id) {
        return sanPhamRepository.findById(id).get();
    }

    @Override
    public List<SanPham> getAll() {
        List<SanPham> sp = sanPhamRepository.findAll();
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
    public Page<SanPham> getAllShop(Pageable pageable) {
        Page<SanPham> sp = sanPhamRepository.getAllByChiTietSanPhamExists(pageable);
        List<SanPham> spx = sp.getContent();
        if (!spx.isEmpty()){
            for (int i = 0; i < spx.size(); i++) {
                SanPham s = spx.get(i);
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
