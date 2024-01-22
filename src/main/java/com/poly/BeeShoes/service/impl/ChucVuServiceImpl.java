package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ChucVu;
import com.poly.BeeShoes.repository.ChucVuReponsitory;
import com.poly.BeeShoes.service.ChucVuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChucVuServiceImpl implements ChucVuService {
    private final ChucVuReponsitory chucVuReponsitory;

    @Override
    public List<ChucVu> getAll() {
        return chucVuReponsitory.findAll();
    }

    @Override
    public void delete(Long id) {
        chucVuReponsitory.deleteById(id);
    }

    @Override
    public ChucVu detail(Long id) {
        ChucVu chucVu = chucVuReponsitory.findById(id).get();
        return chucVu;
    }

    @Override
    public ChucVu add(ChucVu chucVu) {
        chucVuReponsitory.save(chucVu);
        return null;
    }

    @Override
    public ChucVu update(ChucVu chucVu, Long id) {
        chucVuReponsitory.save(chucVu);
        return null;
    }
}
