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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ThongKeRestController {
    private final HoaDonService hoaDonService;

    @GetMapping("/get-arr-count")
    public ResponseEntity<Map<String, Map<Object, Object>>> cntInvByHourOfDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMATTER);
        Map<String, Map<Object, Object>> resultMap = new HashMap<>();
        Map<Object, Object> todayMap = new HashMap<>();
        Map<Object, Object> yesterdayMap = new HashMap<>();

        List<Object[]> invToday = hoaDonService.getAllCountCreatedByCreatDate(sdf.format(new Date()));
        invToday.forEach(inv -> todayMap.put(inv[0], inv[1]));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterdayDate = calendar.getTime();
        String yesterday = sdf.format(yesterdayDate);
        List<Object[]> invYesterday = hoaDonService.getAllCountCreatedByCreatDate(yesterday);
        invYesterday.forEach(inv -> yesterdayMap.put(inv[0], inv[1]));

        resultMap.put("today", todayMap);
        resultMap.put("yesterday", yesterdayMap);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/get-detail-arr-count")
    public ResponseEntity<Map<String, Map<Object, Object>>> getAllCount() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMATTER);
        Map<String, Map<Object, Object>> resultMap = new HashMap<>();
        Map<Object, Object> storeToday = new HashMap<>();
        Map<Object, Object> onlineToday = new HashMap<>();
        Map<Object, Object> storeYesterday = new HashMap<>();
        Map<Object, Object> onlineYesterday = new HashMap<>();
        Map<Object, Object> resultData = new HashMap<>();

        List<Object[]> StoreToDay = hoaDonService.getCountCreatedByCreatDateAndTypeHD(sdf.format(new Date()), true);
        StoreToDay.forEach(inv -> storeToday.put(inv[0], inv[1]));
        List<Object[]> OnlineToday = hoaDonService.getCountCreatedByCreatDateAndTypeHD(sdf.format(new Date()), false);
        OnlineToday.forEach(inv -> onlineToday.put(inv[0], inv[1]));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterdayDate = calendar.getTime();
        String yesterday = sdf.format(yesterdayDate);
        List<Object[]> StoreYesterday = hoaDonService.getCountCreatedByCreatDateAndTypeHD(yesterday, true);
        StoreYesterday.forEach(inv -> storeYesterday.put(inv[0], inv[1]));
        List<Object[]> OnlineYesterday = hoaDonService.getCountCreatedByCreatDateAndTypeHD(yesterday, true);
        OnlineYesterday.forEach(inv -> onlineYesterday.put(inv[0], inv[1]));

        int quantity_store = 0;
        int quantity_online = 0;
        int total_store_revenue = 0;
        int total_online_revenue = 0;

        Date today = new Date();
        List<HoaDon> lstToDay = hoaDonService.getAllByDate(today);
        for (HoaDon hd : lstToDay) {
            if (hd.isLoaiHoaDon()) {
                quantity_store++;
                total_store_revenue += hd.getThucThu().intValue();
            } else {
                quantity_online++;
                total_online_revenue += hd.getThucThu().intValue();
            }
        }
        resultData.put("quantity_store", quantity_store);
        resultData.put("quantity_online", quantity_online);
        resultData.put("total_store_revenue", total_store_revenue);
        resultData.put("total_online_revenue", total_online_revenue);
        resultMap.put("storeToday", storeToday);
        resultMap.put("onlineToday", onlineToday);
        resultMap.put("storeYesterday", storeYesterday);
        resultMap.put("onlineYesterday", onlineYesterday);
        resultMap.put("data", resultData);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/get-revenue-option")
    public ResponseEntity getRevenue(@RequestParam("start") String start,
                                     @RequestParam("end") String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMATTER);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (startDate.equals(endDate)) {
            Map<Object, Object> response = new HashMap<>();
            String date = sdf.format(startDate);
            List<Object[]> data = hoaDonService.getAllRevenueCreatedByCreatDate(date);
            data.forEach(inv ->{
                BigDecimal money = (BigDecimal) inv[1];
                int moneyResult = money.intValue() / 1000;
                response.put(inv[0], moneyResult);
            });
            return ResponseEntity.ok().body(response);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Map<LocalDate, Object> response = new TreeMap<>();
            List<Object[]> data = hoaDonService.getAllCountCreatedByDateRange(startDate, endDate);
            data.forEach(inv -> {
                System.out.println(inv[1]);
                String dateStr = (String) inv[0];
                LocalDate date = LocalDate.parse(dateStr.substring(0, 10), formatter);
                BigDecimal money = (BigDecimal) inv[1];
                int moneyResult = money.intValue() / 1000;
                response.put(date, moneyResult);
            });
            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping("/get-top-product")
    public ResponseEntity getTopPro(@RequestParam(name = "option") String option) {
        if (option.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        List<HoaDon> lstHD = null;

        if (option.equals("week")) {
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int daysToSubtract = (dayOfWeek == Calendar.SUNDAY) ? 6 : (dayOfWeek - Calendar.MONDAY);
            calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract);
            Date startOfWeek = calendar.getTime();
            lstHD = hoaDonService.getHoaDonBetwent(startOfWeek, today);
        } else if (option.equals("month")) {
            Date end = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();
            lstHD = hoaDonService.getHoaDonBetwent(startDate, end);
        } else if (option.equals("day")) {
            // Lấy ngày hiện tại
            Date today = new Date();

            // Tạo ngày bắt đầu của hôm nay (00:00:00)
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(today);
            calendarStart.set(Calendar.HOUR_OF_DAY, 0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Date startDate = calendarStart.getTime();

            // Tạo ngày kết thúc của hôm nay (23:59:59)
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(today);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            Date endDate = calendarEnd.getTime();

            lstHD = hoaDonService.getHoaDonBetwent(startDate, endDate);
        }
        Map<Long, Map<String, Object>> productMap = new HashMap<>();
        lstHD.forEach(order -> {
            order.getHoaDonChiTiets().forEach(item -> {
                long productId = item.getChiTietSanPham().getSanPham().getId();
                int quantity = item.getSoLuong();
                BigDecimal totalRevenue = item.getGiaBan().multiply(BigDecimal.valueOf(quantity));
                if (productMap.containsKey(productId)) {
                    Map<String, Object> productInfo = productMap.get(productId);
                    int currentQuantity = (int) productInfo.getOrDefault("soLuong", 0);
                    BigDecimal currentTotalRevenue = (BigDecimal) productInfo.getOrDefault("tongTien", BigDecimal.ZERO);
                    productInfo.put("soLuong", currentQuantity + quantity);
                    productInfo.put("tongTien", currentTotalRevenue.add(totalRevenue));
                } else {
                    Map<String, Object> productInfo = new HashMap<>();
                    productInfo.put("id", item.getChiTietSanPham().getSanPham().getId());
                    productInfo.put("ten", item.getChiTietSanPham().getSanPham().getTen());
                    productInfo.put("anh", item.getChiTietSanPham().getSanPham().getMainImage().getUrl());
                    productInfo.put("soLuong", quantity);
                    productInfo.put("tongTien", totalRevenue);
                    productInfo.put("giaBan", item.getChiTietSanPham().getGiaBan());
                    productMap.put(productId, productInfo);
                }
            });
        });
        List<Map<String, Object>> top6Products = productMap.values().stream()
                .sorted((p1, p2) -> Integer.compare((int) p2.get("soLuong"), (int) p1.get("soLuong")))
                .limit(10)
                .collect(Collectors.toList());
        System.out.println(top6Products);
        return ResponseEntity.ok().body(top6Products);
    }

}
