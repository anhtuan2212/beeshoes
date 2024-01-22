package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.DeGiay;
import com.poly.BeeShoes.repository.DeGiayRepository;
import com.poly.BeeShoes.service.DeGiayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeGiayServiceImpl implements DeGiayService {
    private final DeGiayRepository deGiayRepository;
    @Override
    public DeGiay save(DeGiay deGiay) {
        return deGiayRepository.save(deGiay);
    }

    @Override
    public DeGiay getById(Long id) {
        return deGiayRepository.findById(id).get();
    }

    @Override
    public List<DeGiay> getAll() {
        return deGiayRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
    }

    @Override
    public boolean delete(Long id) {
        DeGiay dg = deGiayRepository.findById(id).get();
        if (dg.getId()!=null){
            deGiayRepository.deleteById(dg.getId());
            return true;
        }
        return false;
    }
}
