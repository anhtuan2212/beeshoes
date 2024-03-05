package com.poly.BeeShoes.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDto {

    private String notes;
    private int total;
    private String ctsp;
    private String coupon;

}
