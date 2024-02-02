package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ThuongHieu;
import com.poly.BeeShoes.repository.ThuongHieuRepository;
import com.poly.BeeShoes.service.ThuongHieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    public boolean existsByTen(String ten) {
        return thuongHieuRepository.existsByTen(ten);
    }

    @Override
    public ThuongHieu getByTen(String ten) {
        return thuongHieuRepository.getFirstByTen(ten);
    }

    @Override
    public List<ThuongHieu> getAll() {
        return thuongHieuRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
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
