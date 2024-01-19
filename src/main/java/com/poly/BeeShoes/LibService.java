package com.poly.BeeShoes;

import java.math.BigDecimal;

public class LibService {
    // Hàm static để kiểm tra và xóa ký tự khỏi chuỗi và chuyển thành BigDecimal
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
