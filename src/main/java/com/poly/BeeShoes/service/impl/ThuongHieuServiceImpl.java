package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ThuongHieu;
import com.poly.BeeShoes.repository.ThuongHieuRepository;
import com.poly.BeeShoes.service.ThuongHieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThuongHieuServiceImpl implements ThuongHieuService {
    private final ThuongHieuRepository thuongHieuRepository;
    @Override
    public ThuongHieu save(ThuongHieu thuongHieu) {
        return thuongHieuRepository.save(thuongHieu);
    }

    @Override
    public ThuongHieu getById(Long id) {
        return thuongHieuRepository.findById(id).get();
    }

    @Override
    public List<ThuongHieu> getAll() {
        return thuongHieuRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        ThuongHieu th = thuongHieuRepository.findById(id).get();
        if (th.getId()!=null){
            thuongHieuRepository.deleteById(th.getId());
            return true;
        }
        return false;
    }
}
