package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.HinhThucThanhToan;
import com.poly.BeeShoes.repository.HinhThucThanhToanRepository;
import com.poly.BeeShoes.service.HinhThucThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HinhThucThanhToanServiceImpl implements HinhThucThanhToanService {

    private final HinhThucThanhToanRepository hinhThucThanhToanRepository;

    @Override
    public List<HinhThucThanhToan> getAll() {
        return hinhThucThanhToanRepository.findAll();
    }
}
