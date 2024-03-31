package com.poly.BeeShoes.api;

import com.poly.BeeShoes.constant.Constant;
import com.poly.BeeShoes.model.HinhThucThanhToan;
import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.service.HinhThucThanhToanService;
import com.poly.BeeShoes.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ThongKeRestController {
    private final HoaDonService hoaDonService;
    private final HinhThucThanhToanService hinhThucThanhToanService;

    @GetMapping("/cnt-inv-hourOfDay")
    public ResponseEntity<Map<Integer, Integer>[]> cntInvByHourOfDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMATTER);
        // mỗi key là 1 giờ, không có trong map tương đương số lượng = 0
        Map<Integer, Integer>[] arrMap = new HashMap[2];
        arrMap[0] = new HashMap<>(); //0 = today
        arrMap[1] = new HashMap<>(); //1 = yesterday
        List<int[]> invToday = hoaDonService.cntInvoiceInHourByCreatedDate(sdf.format(new Date()));
        invToday.forEach(inv -> System.out.println(inv[0] + "-" + inv[1]));
        invToday.forEach(inv -> arrMap[0].put(inv[0], inv[1]));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date date = calendar.getTime();
        String yesterday = sdf.format(date);
        System.out.println(yesterday);
        List<int[]> invYesterday = hoaDonService.cntInvoiceInHourByCreatedDate(yesterday);
        invYesterday.forEach(inv -> System.out.println(inv[0] + "-" + inv[1]));
        invYesterday.forEach(inv -> arrMap[1].put(inv[0], inv[1]));
        return new ResponseEntity<>(arrMap, HttpStatus.OK);
    }

    @GetMapping("/cnt-inv-typeOfInv")
    public ResponseEntity<Map<String, Integer>> cntInvByPaymentMethod() {
        Map<String, Integer> map = new HashMap<>();
        List<HoaDon> invToday = hoaDonService.getAllByDate(new Date());
        List<HoaDon> invTaiQuay = invToday.stream()
                .filter(HoaDon::isLoaiHoaDon)
                .collect(Collectors.toList());
        map.put("Bán Tại Quầy", invTaiQuay.size());

        List<HoaDon> invOnline = invToday.stream()
                .filter(inv -> !inv.isLoaiHoaDon())
                .collect(Collectors.toList());
        map.put("Bán Online", invOnline.size());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
