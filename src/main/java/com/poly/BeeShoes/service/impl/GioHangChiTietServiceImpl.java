package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.GioHangChiTiet;
import com.poly.BeeShoes.repository.GioHangChiTietRepository;
import com.poly.BeeShoes.service.GioHangChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GioHangChiTietServiceImpl implements GioHangChiTietService {
    private final GioHangChiTietRepository gioHangChiTietRepository;
    @Override
    public GioHangChiTiet save(GioHangChiTiet gioHangChiTiet) {
        return gioHangChiTietRepository.save(gioHangChiTiet);
    }

    @Override
    public boolean delete(Long id) {
        if (gioHangChiTietRepository.existsById(id)){
            gioHangChiTietRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
