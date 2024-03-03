package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.payment.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckOutController {

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);

        model.addAttribute("paymentError", "Thanh toán thất bại, vui lòng thử lại!");

        return paymentStatus == 1 ? "payment/vnpay/order-success" : "customer/pages/shop/checkout";
    }

}
