package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ThuongHieu;
import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.repository.ThuongHieuRepository;
import com.poly.BeeShoes.repository.VoucherResponsitory;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherImpl implements VoucherService {
    private final VoucherResponsitory voucherResponsitory;


    @Override
    public Voucher save(Voucher voucher) {
        return voucherResponsitory.save(voucher);
    }

    @Override
    public List<Voucher> getAll() {
        return voucherResponsitory.findAll();
    }

    @Override
    public Voucher update(Long id, Voucher voucher) {
        Optional<Voucher> optional=voucherResponsitory.findById(id);
        return optional.map(o->
        {
            o.setTrangThai(voucher.isTrangThai());

            return voucherResponsitory.save(voucher);
        }).orElse(null);
    }

    @Override
    public Voucher delete(Long id) {
        Optional<Voucher> optional=voucherResponsitory.findById(id);
        return optional.map(o->{
            voucherResponsitory.delete(o);
            return o;
        }).orElse(null);
    }
}
