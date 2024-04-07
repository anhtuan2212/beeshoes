package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.poly.BeeShoes.library.LibService.calculatePercentageChange;

@Controller
@RequestMapping("/cms")
@RequiredArgsConstructor
public class DashboardController {
    private final HoaDonService hoaDonService;
    private final SanPhamService sanPhamService;

    @GetMapping({"/", "", "/index"})
    public String indexDashboard(Model model) {
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        System.out.println(startDate.toLocaleString() + "|" + endDate.toLocaleString());
        List<HoaDon> lstHD = hoaDonService.getHoaDonBetwent(startDate, endDate);
        Date today = new Date();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        List<HoaDon> lstToDay = hoaDonService.getAllByDate(today);
        List<HoaDon> lstYesterDay = hoaDonService.getAllByDate(yesterday);

        // Lấy top 6 sản phẩm có nhiều lượt mua nhất
        Map<Long, Map<String, Object>> productMap = new HashMap<>();
        lstHD.forEach(order -> {
            // Duyệt qua từng hóa đơn chi tiết của mỗi hóa đơn
            order.getHoaDonChiTiets().forEach(item -> {
                long productId = item.getChiTietSanPham().getSanPham().getId();
                int quantity = item.getSoLuong();
                BigDecimal totalRevenue = item.getGiaBan().multiply(BigDecimal.valueOf(quantity));

                // Nếu sản phẩm đã có trong Map, cập nhật thông tin số lượng bán và tổng tiền
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

        List<SanPham> prList = sanPhamService.findByChiTietSanPham_SoLuongTonLessThan();
        model.addAttribute("product_less", prList);
        model.addAttribute("top_products", top6Products);
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
