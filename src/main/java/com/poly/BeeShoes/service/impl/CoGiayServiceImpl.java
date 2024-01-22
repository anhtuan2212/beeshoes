package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.CoGiay;
import com.poly.BeeShoes.repository.CoGiayRepository;
import com.poly.BeeShoes.service.CoGiayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoGiayServiceImpl implements CoGiayService {
    private final CoGiayRepository coGiayRepository;
    @Override
    public CoGiay save(CoGiay coGiay) {
        return coGiayRepository.save(coGiay);
    }

    @Override
    public CoGiay getById(Long id) {
        return coGiayRepository.findById(id).get();
    }

    @Override
    public List<CoGiay> getAll() {
        return coGiayRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
    }

    @Override
    public boolean delete(Long id) {
        CoGiay cg = coGiayRepository.findById(id).get();
        if (cg.getId()!=null){
            coGiayRepository.deleteById(cg.getId());
            return true;
        }
        return false;
    }
}
