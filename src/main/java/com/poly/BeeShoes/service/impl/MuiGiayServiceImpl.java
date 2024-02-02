package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.MuiGiay;
import com.poly.BeeShoes.repository.MuiGiayRepository;
import com.poly.BeeShoes.service.MuiGiayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MuiGiayServiceImpl implements MuiGiayService {
    private final MuiGiayRepository muiGiayRepository;
    @Override
    public MuiGiay save(MuiGiay muiGiay) {
        return muiGiayRepository.save(muiGiay);
    }

    @Override
    public MuiGiay getById(Long id) {
        return  muiGiayRepository.findById(id).get();
    }

    @Override
    public MuiGiay getByTen(String ten) {
        return muiGiayRepository.getByTen(ten);
    }

    @Override
    public List<MuiGiay> getAll() {
        return muiGiayRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
    }

    @Override
    public boolean delete(Long id) {
        MuiGiay mg = muiGiayRepository.findById(id).get();
        if (mg.getId()!=null){
            muiGiayRepository.deleteById(mg.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByTen(String ten) {
        return muiGiayRepository.existsByTen(ten);
    }
}
