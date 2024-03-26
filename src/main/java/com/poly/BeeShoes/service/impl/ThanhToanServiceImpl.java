package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ThanhToan;
import com.poly.BeeShoes.repository.ThanhToanRepository;
import com.poly.BeeShoes.service.ThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThanhToanServiceImpl implements ThanhToanService {
    private final ThanhToanRepository thanhToanRepository;

    @Override
    public ThanhToan getById(Long id) {
        return thanhToanRepository.findById(id).get();
    }

    @Override
    public ThanhToan save(ThanhToan thanhToan) {
        return thanhToanRepository.save(thanhToan);
    }
}
