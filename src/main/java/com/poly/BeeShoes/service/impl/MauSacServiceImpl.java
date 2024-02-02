package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.MauSac;
import com.poly.BeeShoes.repository.MauSacRepository;
import com.poly.BeeShoes.service.MauSacService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MauSacServiceImpl implements MauSacService {
    private final MauSacRepository mauSacRepository;
    @Override
    public MauSac save(MauSac mauSac) {
        return mauSacRepository.save(mauSac);
    }

    @Override
    public MauSac getById(Long id) {
        return mauSacRepository.getById(id);
    }

    @Override
    public MauSac getMauSacByMa(String ma) {
        return mauSacRepository.getMauSacByMaMauSac(ma);
    }

    @Override
    public MauSac getByTen(String ten) {
        return mauSacRepository.getFirstByTen(ten);
    }

    @Override
    public List<MauSac> getAll() {
        return mauSacRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        MauSac ms = mauSacRepository.findById(id).get();
        if (ms.getId()!=null){
            mauSacRepository.deleteById(ms.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByMaMauSac(String ma) {
        return mauSacRepository.existsByMaMauSac(ma);
    }

    @Override
    public boolean existsByTen(String ten) {
        return mauSacRepository.existsByTen(ten);
    }
}
