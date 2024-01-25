package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.DiaChi;
import com.poly.BeeShoes.repository.DiaChiReponsitory;
import com.poly.BeeShoes.service.DiaChiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaChiServiceImpl implements DiaChiService {
    private final DiaChiReponsitory diaChiReponsitory;

    @Override
    public List<DiaChi> getAll() {
        return diaChiReponsitory.findAll();
    }

    @Override
    public void delete(Long id) {
        diaChiReponsitory.deleteById(id);
    }

    @Override
    public DiaChi detail(Long id) {
        DiaChi diaChi = diaChiReponsitory.findById(id).get();
        return diaChi;
    }

    @Override
    public DiaChi add(DiaChi diaChi) {
        diaChiReponsitory.save(diaChi);
        return null;
    }

    @Override
    public DiaChi update(DiaChi diaChi, Long id) {
        diaChiReponsitory.save(diaChi);
        return null;
    }
}