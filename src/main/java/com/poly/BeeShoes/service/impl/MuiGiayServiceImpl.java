package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.MuiGiay;
import com.poly.BeeShoes.repository.MuiGiayRepository;
import com.poly.BeeShoes.service.MuiGiayService;
import lombok.RequiredArgsConstructor;
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
    public List<MuiGiay> getAll() {
        return muiGiayRepository.findAll();
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
}
