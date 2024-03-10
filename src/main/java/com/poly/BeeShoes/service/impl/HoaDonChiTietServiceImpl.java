package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.HoaDonChiTiet;
import com.poly.BeeShoes.repository.HoaDonChiTietRepository;
import com.poly.BeeShoes.service.HoaDonChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {

    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    @Override
    public List<HoaDonChiTiet> getHoaDonChiTietCuaHoaDonById(Long id) {
        return hoaDonChiTietRepository.findByHoaDon_Id(id);
    }

    @Override
    public List<HoaDonChiTiet> getChiTietSanPhamCuaHoaDonByIdHoaDon(Long id) {
        return hoaDonChiTietRepository.findByHoaDon_Id(id);
    }

    @Override
    public HoaDonChiTiet save(HoaDonChiTiet hoaDonChiTiet) {
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }
}
