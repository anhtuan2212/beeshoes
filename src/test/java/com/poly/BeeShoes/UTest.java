package com.poly.BeeShoes;

import com.poly.BeeShoes.model.HangKhachHang;
import com.poly.BeeShoes.service.HangKhachHangService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UTest {

    @Autowired
    private HangKhachHangService hangKhachHangService;

    @Test
    public void testHKH() {
        HangKhachHang hang = hangKhachHangService.getByMa("BRONZE");
        System.out.println("hang = " + hang.getTenHang());
    }

}
