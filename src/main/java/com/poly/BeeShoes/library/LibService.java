package com.poly.BeeShoes.library;

import java.math.BigDecimal;

public class LibService {
    public static BigDecimal convertStringToBigDecimal(String str) {
        if (str.contains(".")) {
            str = str.replace(".", "");
        }
        if (str.contains("đ")) {
            str = str.replace("đ", "");
        }
        BigDecimal bd = new BigDecimal(str);
        return bd;
    }
}
