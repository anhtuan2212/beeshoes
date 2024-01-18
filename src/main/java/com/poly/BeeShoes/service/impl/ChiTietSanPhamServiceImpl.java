package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.repository.ChiTietSanPhamRepository;
import com.poly.BeeShoes.service.ChiTietSanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChiTietSanPhamServiceImpl implements ChiTietSanPhamService {
    private final ChiTietSanPhamRepository ctspRepository;
    @Override
    public ChiTietSanPham save(ChiTietSanPham chiTietSanPham) {
        return ctspRepository.save(chiTietSanPham);
    }

    @Override
    public List<ChiTietSanPham> getAllBySanPham(SanPham samPham) {
        return ctspRepository.getAllBySanPham(samPham);
    }

    @Override
    public boolean delete(Long id) {
        ChiTietSanPham ctsp = ctspRepository.findById(id).get();
        if (ctsp.getId()!=null){
            ctspRepository.deleteById(ctsp.getId());
            return true;
        }
        return false;
    }
}
