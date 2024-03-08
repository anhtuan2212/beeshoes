package com.poly.BeeShoes.controller.customer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poly.BeeShoes.model.ChiTietSanPham;
import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.payment.vnpay.VNPayService;
import com.poly.BeeShoes.request.ProductCheckoutRequest;
import com.poly.BeeShoes.request.ProductDetailVersion;
import com.poly.BeeShoes.service.ChiTietSanPhamService;
import com.poly.BeeShoes.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CheckOutController {

    private final VNPayService vnPayService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final VoucherService voucherService;

    Gson gson = new Gson();
    @PostMapping("/checkout")
    public String getCheckout (
            @RequestParam("maGiamGia")String ma,
            @RequestParam("listData")String list,
            Model model
    ){
        float totalAmount = 0;
        float total = 0;
        Type listType = new TypeToken<List<ProductCheckoutRequest>>() {
        }.getType();
        List<ProductCheckoutRequest> listData = gson.fromJson(list, listType);
        System.out.println(Arrays.toString(listData.toArray()));
        System.out.println(ma);
        Map<ChiTietSanPham, Integer> productDetailMap = new HashMap<>();
        listData.forEach(data ->
            productDetailMap.put(chiTietSanPhamService.getById(data.getId_product_detail()), data.getQuantity())
        );
        for(Map.Entry<ChiTietSanPham, Integer> entry : productDetailMap.entrySet()) {
            total += (entry.getKey().getGiaBan().floatValue() * entry.getValue());
        }
        if(ma != null) {
            Voucher voucher = voucherService.getByMa(ma);
            if(voucher.getLoaiVoucher().equals("$")) {
                float maxValueOfVoucher = voucher.getGiaTriToiDa().floatValue();
                if(maxValueOfVoucher >= total) {
                    totalAmount = 0;
                    model.addAttribute("voucherValue", total);
                }
                totalAmount = total - maxValueOfVoucher;
                model.addAttribute("voucherValue", maxValueOfVoucher);
            }
            model.addAttribute("voucher", voucher);
        }
        model.addAttribute("productDetailMap", productDetailMap);
        model.addAttribute("total", total);
        model.addAttribute("totalAmount", totalAmount);
        return "customer/pages/shop/checkout";
    }
    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);
        int total = Integer.parseInt(request.getParameter("vnp_Amount")) / 100;
        String[] invoiceCode = request.getParameter("vnp_TxnRef").split("-");
        model.addAttribute("total", total);
        model.addAttribute("invoiceCode", invoiceCode[0]);
        return paymentStatus == 1 ? "payment/vnpay/order-success" : "payment/vnpay/order-failed";
    }

    @GetMapping("/orderSuccess")
    public String orderSuccess(
            @RequestParam("invoiceCode") String invoiceCode,
            @RequestParam("total") int total,
            Model model
    ) {
        model.addAttribute("invoiceCode", invoiceCode);
        model.addAttribute("total", total);
        return "payment/vnpay/order-success";
    }

    @GetMapping("/orderFailed")
    public String orderFailed() {
        return "payment/vnpay/order-failed";
    }

}
