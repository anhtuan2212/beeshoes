package com.poly.BeeShoes.controller.cms;


import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.repository.VoucherResponsitory;
import com.poly.BeeShoes.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Controller
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
                                 "page", defaultValue = "1") Integer page, @Param("key") String key) {
        Page<Voucher> list = voucherService.getAllpage(page);
        if (key != null) {
            list = voucherService.SearchVoucher(key, page);
            model.addAttribute("key", key);
        }
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currenPage", page);
        model.addAttribute("listVC", list);
        return "cms/pages/voucher/voucher.html";
    }

    @GetMapping("/add-voucher")
    public String addvoucher() {
        return "cms/pages/voucher/add-Voucher.html";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Voucher voucher,
                      @RequestParam(name =
                              "page", defaultValue = "1") Integer page)throws ParseException {


        long count = voucherResponsitory.count();
        int numberOfDigits = (int) Math.log10(count + 1) + 1;
        int numberOfZeros = Math.max(0, 5 - numberOfDigits);
        String ma = String.format("VC%0" + (numberOfDigits + numberOfZeros) + "d", count + 1);
        count++;
        voucher.setMa(ma);
        voucher.setTrangThai(true);
        voucherService.save(voucher);
        return "redirect:/cms/voucher";
    }

    @PostMapping("/update-voucher/{id}")
    public String update(@PathVariable Long id, Model model,
                         @ModelAttribute("Voucher") Voucher voucher) {
        Voucher voucher1 = voucherService.detail(id);
        model.addAttribute("List", voucher1);
        voucherService.update(id, voucher);

        return "redirect:/cms/voucher";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Voucher voucher1 = voucherService.detail(id);
        model.addAttribute("List", voucher1);
        return "cms/pages/voucher/update-voucher.html";
    }
}
