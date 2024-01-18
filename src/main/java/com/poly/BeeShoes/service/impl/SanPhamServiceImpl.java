package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.repository.SanPhamRepository;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
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
    public List<SanPham> getAll() {
        return sanPhamRepository.findAll();
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
