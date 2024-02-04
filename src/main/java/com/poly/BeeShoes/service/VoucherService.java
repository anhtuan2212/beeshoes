package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface VoucherService {
    Voucher save(Voucher voucher);
    List<Voucher> getAll();
     Voucher detail(Long id);
    Voucher update(Long id, Voucher voucher);
    Voucher delete(Long id);
    Page<Voucher> getAllpage(Integer page);
    Page <Voucher> SearchVoucher(String key,Integer page);
//    String generateVoucherCode();
//    Voucher getByMa(String ma);
//
//    boolean existByMa(String ma);
}
