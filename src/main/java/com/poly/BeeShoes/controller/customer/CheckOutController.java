package com.poly.BeeShoes.controller.customer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poly.BeeShoes.payment.vnpay.VNPayService;
import com.poly.BeeShoes.request.ProductCheckoutRequest;
import com.poly.BeeShoes.request.ProductDetailVersion;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Controller
public class CheckOutController {

    @Autowired
    private VNPayService vnPayService;

    Gson gson = new Gson();
    @PostMapping("/checkout")
    public String getCheckout (@RequestParam("maGiamGia")String ma,@RequestParam("listData")String list, Model model){
        Type listType = new TypeToken<List<ProductCheckoutRequest>>() {
        }.getType();
        List<ProductCheckoutRequest> listData = gson.fromJson(list, listType);
        System.out.println(Arrays.toString(listData.toArray()));
        System.out.println("Mã Voucher :"+ma);
        return "customer/pages/shop/checkout";
    }
    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);

        model.addAttribute("paymentError", "Thanh toán thất bại, vui lòng thử lại!");

        return paymentStatus == 1 ? "payment/vnpay/order-success" : "customer/pages/shop/checkout";
    }

}
