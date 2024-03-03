package com.poly.BeeShoes.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDto {

    private String notes;
    private int total;

}
