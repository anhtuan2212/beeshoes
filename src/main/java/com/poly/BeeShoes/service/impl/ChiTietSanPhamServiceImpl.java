package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.KichCo;
import com.poly.BeeShoes.model.MauSac;
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
    public ChiTietSanPham getById(Long id) {
        return ctspRepository.findById(id).get();
    }

    @Override
    public ChiTietSanPham getBySizeAndColorAndProduct(KichCo kichCo, MauSac mauSac,SanPham sanPham) {
        return ctspRepository.getFirstByMauSacAndKichCoAndSanPham(mauSac,kichCo,sanPham);
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

    @Override
    public boolean existsByMaSanPham(String ma) {
        return false;
    }

    @Override
    public String generateDetailCode() {
        long count = ctspRepository.count();
        int numberOfDigits = (int) Math.log10(count + 1) + 1;
        int numberOfZeros = Math.max(0, 6 - numberOfDigits);
        String productCode;
        do {
            productCode = String.format("SP%0" + (numberOfDigits + numberOfZeros) + "d", count + 1);
            count++;
        } while (ctspRepository.existsByMaSanPham(productCode));

        return productCode;
    }
}
