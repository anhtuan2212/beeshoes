package com.poly.BeeShoes;

import com.poly.BeeShoes.model.HangKhachHang;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.repository.UserRepository;
import com.poly.BeeShoes.service.HangKhachHangService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testHKH() {
        User user = userRepository.findByKhachHang_Id(7L);
        System.out.println("Email = " + user.getEmail());
    }

}
