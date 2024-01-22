package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.KichCo;
import com.poly.BeeShoes.repository.KichCoRepository;
import com.poly.BeeShoes.service.KichCoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KichCoServiceImpl implements KichCoService {
    private final KichCoRepository kichCoRepository;
    @Override
    public KichCo save(KichCo kichCo) {
        return kichCoRepository.save(kichCo);
    }

    @Override
    public KichCo getById(Long id) {
        return kichCoRepository.findById(id).get();
    }

    @Override
    public KichCo getByTen(String co) {
        return kichCoRepository.getKichCoByTen(co);
    }

    @Override
    public List<KichCo> getAll() {
        return kichCoRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
    }

    @Override
    public boolean delete(Long id) {
        KichCo co = kichCoRepository.findById(id).get();
        if (co.getId()!=null){
            kichCoRepository.deleteById(co.getId());
        }
        return false;
    }

    @Override
    public boolean exitsByTen(String ten) {
        return kichCoRepository.existsByTen(ten);
    }
}
