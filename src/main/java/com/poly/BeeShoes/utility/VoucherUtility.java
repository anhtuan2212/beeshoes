package com.poly.BeeShoes.utility;

import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VoucherUtility {

    private final VoucherService voucherService;

    @Scheduled(fixedRate = 1000)
    public void updateVoucher() {
        List<Voucher> voucherList = voucherService.getAll();

        for(Voucher voucher : voucherList) {
            if (voucher.getNgayBatDau().before(new Date())) {
                voucher.setTrangThai(false);
                voucherService.update(voucher.getId(), voucher);
            } else if (voucher.getNgayBatDau().equals(new Date())) {
                voucher.setTrangThai(true);
                voucherService.update(voucher.getId(), voucher);
            } else if (voucher.getNgayKetThuc().equals(new Date()) || voucher.getNgayKetThuc().after(new Date())) {
                voucherService.delete(voucher.getId());
            }
        }
    }

}
