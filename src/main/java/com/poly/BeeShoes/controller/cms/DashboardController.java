package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import static com.poly.BeeShoes.library.LibService.calculatePercentageChange;

@Controller
@RequestMapping("/cms")
@RequiredArgsConstructor
public class DashboardController {
    private final HoaDonService hoaDonService;

    @GetMapping({"/", "", "/index"})
    public String indexDashboard(Model model) {
        List<HoaDon> lstHD = hoaDonService.getAllHoaDon();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        List<HoaDon> lstToDay = hoaDonService.getAllByDate(today);
        List<HoaDon> lstYesterDay = hoaDonService.getAllByDate(yesterday);

        int quantity_oder_today = lstToDay.size();
        int quantity_oder_yesterday = lstYesterDay.size();

        int quantity_online_oder_today = 0;
        int quantity_store_oder_today = 0;
        int quantity_online_oder_yesterday = 0;
        int quantity_store_oder_yesterday = 0;

        int total_all = 0;
        int total_online = 0;
        int total_store = 0;
        int total_discount = 0;

        int num_oder_all = lstHD.size();
        int num_oder_online = 0;
        int num_oder_store = 0;
        int num_oder_discount = 0;

        int total_online_revenue_today = 0;
        int total_online_revenue_yesterday = 0;
        int total_store_revenue_today = 0;
        int total_store_revenue_yesterday = 0;

        for (HoaDon hd : lstToDay) {
            if (hd.isLoaiHoaDon()) {
                quantity_store_oder_today++;
                total_store_revenue_today += hd.getThucThu().intValue();
            } else {
                quantity_online_oder_today++;
                total_online_revenue_today += hd.getThucThu().intValue();
            }
        }
        for (HoaDon hd : lstYesterDay) {
            if (hd.isLoaiHoaDon()) {
                quantity_store_oder_yesterday++;
                total_store_revenue_yesterday += hd.getThucThu().intValue();
            } else {
                quantity_online_oder_yesterday++;
                total_online_revenue_yesterday += hd.getThucThu().intValue();
            }
        }
        for (HoaDon hd : lstHD) {
            total_all += hd.getThucThu().intValue();
            if (hd.getGiamGia().intValue() > 0) {
                num_oder_discount++;
                total_discount += hd.getGiamGia().intValue();
            }
            if (hd.isLoaiHoaDon()) {
                total_store += hd.getThucThu().intValue();
                num_oder_store++;
            } else {
                total_online += hd.getThucThu().intValue();
                num_oder_online++;
            }
        }


        Map<String, Object> online_data = createDataMap(total_online, num_oder_online, calculatePercentageChange(quantity_online_oder_yesterday, quantity_online_oder_today), quantity_online_oder_today > quantity_online_oder_yesterday);
        Map<String, Object> total_all_data = createDataMap(total_all, num_oder_all, calculatePercentageChange(quantity_oder_yesterday, quantity_oder_today), quantity_oder_today > quantity_oder_yesterday);
        Map<String, Object> in_store_data = createDataMap(total_store, num_oder_store, calculatePercentageChange(quantity_store_oder_yesterday, quantity_store_oder_today), quantity_store_oder_today > quantity_store_oder_yesterday);
        Map<String, Object> discount_data = createDataMap(total_discount, num_oder_discount, 0, false);

        int change_online = total_online_revenue_today - total_online_revenue_yesterday;
        int change_store = total_store_revenue_today - total_store_revenue_yesterday;
        Map<String, Object> today_detail_data = new HashMap<>();
        today_detail_data.put("revenue_today_online", total_online_revenue_today);
        today_detail_data.put("revenue_today_store", total_store_revenue_today);
        today_detail_data.put("change_store", change_store);
        today_detail_data.put("change_online", change_online);
        today_detail_data.put("percent_online", calculatePercentageChange(total_online_revenue_yesterday, total_online_revenue_today));
        today_detail_data.put("percent_store", calculatePercentageChange(total_store_revenue_yesterday, total_store_revenue_today));
        today_detail_data.put("direction_store", total_store_revenue_today > total_store_revenue_yesterday);
        today_detail_data.put("direction_online", total_online_revenue_today > total_online_revenue_yesterday);
        today_detail_data.put("quantity_today_store", quantity_store_oder_today);
        today_detail_data.put("quantity_yesterday_store", quantity_store_oder_yesterday);
        today_detail_data.put("quantity_today_online", quantity_online_oder_today);
        today_detail_data.put("quantity_yesterday_online", quantity_online_oder_yesterday);

        model.addAttribute("today_detail_data", today_detail_data);
        model.addAttribute("online_data", online_data);
        model.addAttribute("discount_data", discount_data);
        model.addAttribute("total_all_data", total_all_data);
        model.addAttribute("in_store_data", in_store_data);
        return "cms/index";
    }

    public Map<String, Object> createDataMap(int total, int numOder, int percent, boolean direction) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", total);
        dataMap.put("numOder", numOder);
        dataMap.put("percent", percent);
        dataMap.put("direction", direction);
        return dataMap;
    }
}
