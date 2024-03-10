package com.poly.BeeShoes;

import com.poly.BeeShoes.model.HangKhachHang;
import com.poly.BeeShoes.model.NhanVien;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.repository.NhanVienRepository;
import com.poly.BeeShoes.repository.UserRepository;
import com.poly.BeeShoes.service.HangKhachHangService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class UTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Test
    public void testHKH() {
        Long id = 32L;
        String email = "ngadtqph28985@fpt.edu.vn";
        String phoneNumber = "0768241337";
        String cccd = "010201006558";
        NhanVien nhanVien = nhanVienRepository.findById(id).get();
        User user = userRepository.findByNhanVien_Id(id);

        boolean emailExists = userRepository.existsByEmail(email);
        boolean phoneNumberExists = nhanVienRepository.existsBySdt(phoneNumber);
        boolean cccdExists = nhanVienRepository.existsByCccd(cccd);
        System.out.println(emailExists + " " + phoneNumberExists + " " + cccdExists);
        if(user.getEmail().equalsIgnoreCase(email)){
            emailExists = false;
        }
        if(nhanVien.getSdt().equalsIgnoreCase(phoneNumber)){
            phoneNumberExists = false;
        }if(nhanVien.getCccd().equalsIgnoreCase(cccd)){
            cccdExists = false;
        }
        System.out.println(emailExists + " " + phoneNumberExists + " " + cccdExists);

        Map<String, Boolean> result = new HashMap<>();
        result.put("email", emailExists);
        result.put("phoneNumber", phoneNumberExists);
        result.put("cccd", cccdExists);

    }

}
