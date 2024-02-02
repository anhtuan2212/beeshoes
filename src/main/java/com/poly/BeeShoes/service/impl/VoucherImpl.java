package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ThuongHieu;
import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.repository.ThuongHieuRepository;
import com.poly.BeeShoes.repository.VoucherResponsitory;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Voucher detail(Long id) {
        Voucher voucher=voucherResponsitory.findById(id).get();
        return voucher;
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

    @Override
    public Page<Voucher> getAllpage(Integer page) {
        Pageable page1= PageRequest.of(page-1,5);

        return voucherResponsitory.findAll(page1);
    }

    @Override
    public Page<Voucher> SearchVoucher(String key, Integer page) {
       List list=voucherResponsitory.searchVC(key);
       Pageable pageable=PageRequest.of(page-1,5);
       Integer start= (int)pageable.getOffset();
       Integer end=(int)((pageable.getOffset() + pageable.getPageSize()) >list.size()
               ?list.size() : pageable.getOffset() +pageable.getPageSize());
       list=list.subList(start,end);
        return new PageImpl<Voucher>(list,pageable,voucherResponsitory.searchVC(key).size());
    }


//    @Override
//    public String generateVoucherCode() {
//        long count = voucherResponsitory.count();
//        int numberOfDigits = (int) Math.log10(count + 1) + 1;
//        int numberOfZeros = Math.max(0, 5 - numberOfDigits);
//        String ma;
//        do {
//            ma = String.format("VC%0" + (numberOfDigits + numberOfZeros) + "d", count + 1);
//            count++;
//        } while (voucherResponsitory.existsByMaVoucher(ma));
//
//        return ma;
//    }
//
//    @Override
//    public Voucher getByMa(String ma) {
//        return voucherResponsitory.findByMavoucher(ma)
//                .orElseThrow(() -> new UsernameNotFoundException("username does not exist"));
//    }
//
//    @Override
//    public boolean existByMa(String ma) {
//        return voucherResponsitory.existsByMaVoucher(ma);
//    }
}
