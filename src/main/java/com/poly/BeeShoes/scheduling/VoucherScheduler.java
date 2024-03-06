package com.poly.BeeShoes.scheduling;

import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.repository.VoucherResponsitory;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class VoucherScheduler {
    private final VoucherService voucherService;

    private final VoucherResponsitory voucherRepository;
    @Scheduled(fixedRate = 30000) // Chạy mỗi phút
    public void updateVoucherStatus() {
        List<Integer> trangThaiList = Arrays.asList(1, 2);
        List<Voucher> vouchers = voucherRepository.findAllByTrangThaiInOrderByNgayBatDauAsc(trangThaiList);
        voucherService.updateVoucherStatus(vouchers);
    }
}
