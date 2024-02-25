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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        Voucher voucher = voucherResponsitory.findById(id).get();
        return voucher;
    }


    @Override
    public Voucher update(Long id, Voucher voucher) {
        Optional<Voucher> optional = voucherResponsitory.findById(id);
        return optional.map(o ->
        {
            voucherResponsitory.save(voucher);
            return o;
        }).orElse(null);
    }

    @Override
    public Voucher delete(Long id) {
        Optional<Voucher> optional = voucherResponsitory.findById(id);
        return optional.map(o -> {
            o.setTrangThai(3);  // Đặt trạng thái của đối tượng thành 3
            return voucherResponsitory.save(o);  // Lưu và trả về đối tượng đã được thay đổi
        }).orElse(null);
    }

    @Override
    public Page<Voucher> getAllpage(Integer page) {
        Pageable page1 = PageRequest.of(page - 1, 5);
        return voucherResponsitory.findAll(page1);
    }

    @Override
    public Page<Voucher> SearchVoucher(String key, Integer page) {
        List list = voucherResponsitory.searchVC(key);
        Pageable pageable = PageRequest.of(page - 1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size() : pageable.getOffset() + pageable.getPageSize());
        list = list.subList(start, end);
        return new PageImpl<Voucher>(list, pageable, voucherResponsitory.searchVC(key).size());
    }

//    @Override
//    public Page<Voucher> Searchlaoi(String loai, Integer page) {
//        List list=voucherResponsitory.searchloai(loai);
//        Pageable pageable=PageRequest.of(page-1,5);
//        Integer start= (int)pageable.getOffset();
//        Integer end=(int)((pageable.getOffset() + pageable.getPageSize()) >list.size()
//                ?list.size() : pageable.getOffset() +pageable.getPageSize());
//        list=list.subList(start,end);
//        return new PageImpl<Voucher>(list,pageable,voucherResponsitory.searchloai(loai).size());
//    }

//    @Override
//    public Page<Voucher> SearchVoucher1(String key1, Integer page) {
//        List list=voucherResponsitory.searchVC1(key1);
//        Pageable pageable=PageRequest.of(page-1,5);
//        Integer start= (int)pageable.getOffset();
//        Integer end=(int)((pageable.getOffset() + pageable.getPageSize()) >list.size()
//                ?list.size() : pageable.getOffset() +pageable.getPageSize());
//        list=list.subList(start,end);
//        return new PageImpl<Voucher>(list,pageable,voucherResponsitory.searchVC1(key1).size());
//    }

    @Override
    public Page<Voucher> Searchtt(Integer isTru, Integer page) {
        List<Voucher> vouchers = voucherResponsitory.searchtt(isTru);
        Pageable pageable = PageRequest.of(page - 1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > vouchers.size()
                ? vouchers.size() : pageable.getOffset() + pageable.getPageSize());
        vouchers = vouchers.subList(start, end);
        return new PageImpl<Voucher>(vouchers, pageable, voucherResponsitory.searchtt(isTru).size());
    }

    @Override
    public Page<Voucher> findByCreatedat(LocalDateTime startDate, LocalDateTime endDate, Integer page) {
        List<Voucher> vouchers = voucherResponsitory.findByNgayBatDauBetweenAndNgayKetThuc(startDate, endDate);
        Pageable pageable = PageRequest.of(page - 1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > vouchers.size()
                ? vouchers.size() : pageable.getOffset() + pageable.getPageSize());

        return new PageImpl<Voucher>(vouchers.subList(start, end), pageable, vouchers.size());
    }

    @Override
    public Page<Voucher> findBysoluong(Integer soluong1, Integer soluong2, Integer page) {
        List<Voucher> vouchers = voucherResponsitory.findBySoLuongBetweenAndSoLuong(soluong1, soluong2);
        Pageable pageable = PageRequest.of(page - 1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > vouchers.size()
                ? vouchers.size() : pageable.getOffset() + pageable.getPageSize());

        return new PageImpl<Voucher>(vouchers.subList(start, end), pageable, vouchers.size());
    }

    @Override
    public Page<Voucher> findBytienmat(BigDecimal TienMat1, BigDecimal TienMat2, Integer page) {
        List<Voucher> vouchers = voucherResponsitory.findByGiaTriTienMatBetweenAndGiaTriTienMat(TienMat1, TienMat2);
        Pageable pageable = PageRequest.of(page - 1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > vouchers.size()
                ? vouchers.size() : pageable.getOffset() + pageable.getPageSize());

        return new PageImpl<Voucher>(vouchers.subList(start, end), pageable, vouchers.size());
    }

    @Override
    public Page<Voucher> findByphantram(Integer phantram1, Integer phantram2, Integer page) {
        List<Voucher> vouchers = voucherResponsitory.findByGiaTriPhanTramBetweenAndGiaTriPhanTram(phantram1, phantram2);
        Pageable pageable = PageRequest.of(page - 1, 5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > vouchers.size()
                ? vouchers.size() : pageable.getOffset() + pageable.getPageSize());

        return new PageImpl<Voucher>(vouchers.subList(start, end), pageable, vouchers.size());
    }

    @Override
    public void updateVoucherStatus(List<Voucher> vouchers) {
        LocalDateTime now = LocalDateTime.now();
        for (Voucher voucher : vouchers) {
            Timestamp ngayBatDauTimestamp = voucher.getNgayBatDau();
            Timestamp ngayKetThucTimestamp = voucher.getNgayKetThuc();

            if (ngayBatDauTimestamp != null && ngayKetThucTimestamp != null) {
            if (now.isAfter(voucher.getNgayBatDau().toLocalDateTime()) && now.isBefore(voucher.getNgayKetThuc().toLocalDateTime())) {
                if (voucher.getTrangThai() == 1) {
                    voucher.setTrangThai(2);
                    voucherResponsitory.save(voucher);
                    System.out.println("Start :"+voucher.getTen());
                }
            } else if (now.isAfter(voucher.getNgayKetThuc().toLocalDateTime())) {
                if (voucher.getTrangThai() == 2) {
                    System.out.println("End :"+voucher.getTen());
                    voucher.setTrangThai(3);
                    voucherResponsitory.save(voucher);
                }
            }

        }}
    }

}
