package com.poly.BeeShoes.api;

import com.poly.BeeShoes.dto.PaymentDto;
import com.poly.BeeShoes.payment.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check-out")
public class CheckOutRestController {

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/placeOrder")
    public ResponseEntity<String> createOrder(
            @RequestBody PaymentDto paymentDto,
            HttpServletRequest request
    ) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
        String vnpUrl = vnPayService.createOrder(paymentDto.getTotal(), paymentDto.getNotes(), baseUrl);
        return new ResponseEntity<>(vnpUrl, HttpStatus.OK);
    }

}
