package com.poly.BeeShoes;

import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.repository.HoaDonRepository;
import com.poly.BeeShoes.repository.NhanVienRepository;
import com.poly.BeeShoes.repository.UserRepository;
import com.poly.BeeShoes.service.HangKhachHangService;
import com.poly.BeeShoes.utility.ConvertUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class UTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Test
    public void invoice() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        java.sql.Date date= java.sql.Date.valueOf(yesterday);
        List<HoaDon> hoaDonList =
                hoaDonRepository.findByNgayTaoBetweenAndTrangThai(
                        ConvertUtility.DateToTimestamp(new Date()),
                        ConvertUtility.DateToTimestamp(new Date(date.getTime())),
                        TrangThaiHoaDon.ChoXacNhan);
        hoaDonList.forEach(hoaDon -> System.out.println(hoaDon.getMaHoaDon() + " "));
    }

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
