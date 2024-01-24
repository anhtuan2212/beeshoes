package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface VoucherService {
    Voucher save(Voucher voucher);
    List<Voucher> getAll();

    Voucher update(Long id, Voucher voucher);
    Voucher delete(Long id);
}
