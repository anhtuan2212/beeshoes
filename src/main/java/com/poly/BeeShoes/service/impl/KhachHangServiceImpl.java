package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.utility.ConvertUtility;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.repository.KhachHangRepository;
import com.poly.BeeShoes.repository.UserRepository;
import com.poly.BeeShoes.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final UserRepository userRepository;
    @Override
    public KhachHang getByMa(String ma) {
        return khachHangRepository.findByMaKhachHang(ma)
                .orElseThrow(() -> new UsernameNotFoundException("username does not exist"));
    }

    @Override
    public boolean existByMa(String ma) {
        return khachHangRepository.existsByMaKhachHang(ma);
    }

    @Override
    public KhachHang createNew(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    @Override
    public KhachHang update(KhachHang khachHang) {
        KhachHang updatedKhachHang = new KhachHang();
        updatedKhachHang.setMaKhachHang(khachHang.getTen());
        updatedKhachHang.setTenDem(khachHang.getTenDem());
        updatedKhachHang.setHo(khachHang.getHo());
        updatedKhachHang.setDiem(khachHang.getDiem());
        updatedKhachHang.setStatus(khachHang.isStatus());
        updatedKhachHang.setGioiTinh(khachHang.isGioiTinh());
        updatedKhachHang.setNgaySinh(khachHang.getNgaySinh());
        updatedKhachHang.setNgaySua(ConvertUtility.DateToTimestamp(new Date()));
        updatedKhachHang.setSdt(khachHang.getSdt());
        return khachHangRepository.save(updatedKhachHang);
    }

    @Override
    public String generateCustomerCode() {
        long count = khachHangRepository.count();
        int numberOfDigits = (int) Math.log10(count + 1) + 1;
        int numberOfZeros = Math.max(0, 5 - numberOfDigits);
        String customerCode;
        do {
            customerCode = String.format("KH%0" + (numberOfDigits + numberOfZeros) + "d", count + 1);
            count++;
        } while (khachHangRepository.existsByMaKhachHang(customerCode));

        return customerCode;
    }
}
