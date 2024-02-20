package com.poly.BeeShoes.controller.cms;


import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.repository.VoucherResponsitory;
import com.poly.BeeShoes.request.VoucherRequest;
import com.poly.BeeShoes.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
@Component
@RequiredArgsConstructor
@RequestMapping("/cms")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherResponsitory voucherResponsitory;


    @GetMapping("/voucher")
    public String getAll(Model model,
                         @RequestParam(name =
                                 "page", defaultValue = "1") Integer page,
                         @Param("key") String key,
                         @Param("soluong1") Integer  soluong1,
                         @Param("soluong2") Integer  soluong2,
                         @Param("phantram1") Integer  phantram1,
                         @Param("phantram2") Integer  phantram2,
                         @Param("TienMat1") BigDecimal TienMat1,
                         @Param("TienMat2") BigDecimal TienMat2,
                         @Param("startDate") LocalDateTime startDate,
                         @Param("endDate") LocalDateTime endDate,
                         @Param("isTru") Integer isTru) {
        Page<Voucher> list = voucherService.getAllpage(page);
        if (key != null) {
            list = voucherService.SearchVoucher(key, page);
            model.addAttribute("key", key);
        }

        if (endDate != null && startDate != null) {
            list = voucherService.findByCreatedat(startDate,endDate,page);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        }
        if (soluong1 != null && soluong1 >= 0 && soluong2 != null && soluong2 >= 0) {
            list = voucherService.findBysoluong(soluong1, soluong2, page);
            model.addAttribute("soluong1", soluong1);
            model.addAttribute("soluong2", soluong2);
        }
        if (TienMat1 != null && TienMat1.compareTo(BigDecimal.ZERO) >= 0
                && TienMat2 != null && TienMat2.compareTo(BigDecimal.ZERO) >= 0) {
            list = voucherService.findBytienmat(TienMat1, TienMat2, page);
            model.addAttribute("TienMat1", TienMat1);
            model.addAttribute("TienMat2", TienMat2);
        }
        if (isTru != null) {
            list = voucherService.Searchtt(isTru, page);
            model.addAttribute("isTru", isTru);
        }
        if (phantram1 != null && phantram1 >= 0 && phantram2 != null && phantram2 >= 0) {
            list = voucherService.findByphantram(phantram1, phantram2, page);
            model.addAttribute("phantram1", phantram1);
            model.addAttribute("phantram2", phantram2);
        }
//        if (loai != null) {
//            list = voucherService.Searchlaoi(loai, page);
//            model.addAttribute("loai", loai);
//        }

        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currenPage", page);
        model.addAttribute("listVC", list);
        return "cms/pages/voucher/voucher.html";
    }


    @GetMapping("/add-voucher")
    public String addvoucher() {
        return "cms/pages/voucher/add.html";
    }

    @PostMapping("/add-voucher")
    public String add(@ModelAttribute Voucher voucher,
                      @RequestParam(name =
                              "page", defaultValue = "1") Integer page)throws ParseException {
        List<Voucher> list=voucherService.getAll();
        voucher.setNgayBatDau(Timestamp.valueOf(voucher.getStartDate1()));
        voucher.setNgayKetThuc(Timestamp.valueOf(voucher.getEndDate1()));
        long count = voucherResponsitory.count();

        int numberOfDigits = (int) Math.log10(count + 1) + 1;
        int numberOfZeros = Math.max(0, 5 - numberOfDigits);
        String ma = String.format("VC%0" + (numberOfDigits + numberOfZeros) + "d", count + 1);
        count++;
        voucher.setMa(ma);
        voucher.setTrangThai(1);
        voucherService.save(voucher);
        return "redirect:/cms/voucher";
    }

    @PostMapping("/update-voucher/{id}")
    public String update(@PathVariable Long id, Model model,
                         @ModelAttribute("Voucher") VoucherRequest voucher) {
        Voucher voucher1 = voucherService.detail(id);
        voucher1.setLoaiVoucher(voucher.getLoaiVoucher());
        voucher1.setTen(voucher.getTen());
        voucher1.setMa(voucher.getMa());
        voucher1.setGiaTriTienMat(voucher.getGiaTriTienMat());
        voucher1.setGiaTriPhanTram(voucher.getGiaTriPhanTram());
        voucher1.setGiaTriToiDa(voucher.getGiaTriToiDa());
        voucher1.setGiaTriToiThieu(voucher.getGiaTriToiThieu());
        voucher1.setSoLuong(voucher.getSoLuong());
        voucher1.setNgayBatDau(Timestamp.valueOf(voucher.getNgayBatDau()));
        voucher1.setNgayKetThuc(Timestamp.valueOf(voucher.getNgayKetThuc()));
        voucherService.update(id, voucher1);
            return "redirect:/cms/voucher";
    }

    @GetMapping("/update-voucher/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Voucher voucher1 = voucherService.detail(id);
        List<Voucher> options=voucherService.getAll();
        voucher1.setStartDate1(voucher1.getNgayBatDau().toLocalDateTime());
        voucher1.setStartDate1(voucher1.getNgayKetThuc().toLocalDateTime());
        model.addAttribute("Listvv", voucher1);
        List<Voucher> uniqueOptions = options.stream()
                .collect(Collectors.toMap(Voucher::getLoaiVoucher, Function.identity(), (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toList());
        List<Voucher> unique = options.stream()
                .collect(Collectors.toMap(Voucher::getTrangThai, Function.identity(), (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toList());


        model.addAttribute("option", unique);
        model.addAttribute("options", uniqueOptions);
        return "cms/pages/voucher/update-voucher.html";
    }
    @GetMapping ("/delete-voucher/{id}")
    public String delete(@PathVariable Long id, Model model,
                         @ModelAttribute("Voucher") Voucher voucher) {
        Voucher voucher1 = voucherService.detail(id);
       voucherService.delete(id);
        return "redirect:/cms/voucher";
    }
}
