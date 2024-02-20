package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.model.NhanVien;
import com.poly.BeeShoes.model.Role;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.request.KhachHangRequest;
import com.poly.BeeShoes.request.NhanVienRequest;
import com.poly.BeeShoes.service.ChucVuService;
import com.poly.BeeShoes.service.NhanVienService;
import com.poly.BeeShoes.service.UserService;
import com.poly.BeeShoes.utility.MailUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms/nhan-vien")
public class NhanVienController {
    private final NhanVienService nhanVienService;
    private final ChucVuService chucVuService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final MailUtility mailUtility;

    @GetMapping("")
    public String nhanVien(Model model){
        List<NhanVien> nv = nhanVienService.getAll();
//        model.addAttribute("listCV", chucVuService.getAll());
        model.addAttribute("listNV", nv);
        return "cms/pages/users/nhanVien";
    }

    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        NhanVienRequest nv = new NhanVienRequest();
        nv.setTrangThai(true);
        model.addAttribute("nhanVien", nv);
        model.addAttribute("listCV", chucVuService.getAll());
        return "cms/pages/users/add-nhanVien";
    }

    @PostMapping("/add")
    public String addNV(@ModelAttribute("nhanVien") NhanVienRequest nhanVien, Model model) {
        model.addAttribute("nhanVien", nhanVien);
        NhanVien nv = new NhanVien();
        nv.setHoTen(nhanVien.getHoTen());
        nv.setGioiTinh(nhanVien.isGioiTinh());
        nv.setNgaySinh(nhanVien.getNgaySinh());
        nv.setSdt(nhanVien.getSdt());
        nv.setNgayTao(Timestamp.from(Instant.now()));
        nv.setMaNhanVien(nhanVienService.generateEmployeeCode());
        nv.setTrangThai(nhanVien.isTrangThai());
        nv.setCccd(nhanVien.getCccd());
        nv.setChucVu(chucVuService.getById(nhanVien.getIdCV()));
        nv.setSoNha(nhanVien.getSoNha());
        nv.setPhuongXa(nhanVien.getPhuongXa());
        nv.setQuanHuyen(nhanVien.getQuanHuyen());
        nv.setTinhThanhPho(nhanVien.getTinhThanhPho());
        NhanVien nhanVien1 = nhanVienService.add(nv);
        User user = new User();
        user.setEmail(nhanVien.getEmail());
        user.setAvatar(nhanVien.getAvatar());
        user.setRole(Role.CUSTOMER);
        String password = LibService.generateRandomString(10);
        user.setPassword(passwordEncoder.encode(password));
        user.setNhanVien(nhanVien1);
        User u =  userService.createNewUser(user);
        if(u!=null){
            String tb = "Chúc mừng bạn đã tạo tài khoản thành công!";
            String body = "<h1>Đăng Ký Thành Công !</h1><h2>email đăng nhập là : "+user.getEmail() +"</h2><h2>Mật Khẩu là : "+password+"</h2>";
            mailUtility.sendMail(user.getEmail(), tb, body);
        }
        return "redirect:/cms/nhan-vien";
    }

    @GetMapping("/view-detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model){
        NhanVien nhanVien = nhanVienService.detail(id);
        User user = userService.findByNhanVien_Id(nhanVien.getId());
        NhanVienRequest nv = new NhanVienRequest();
        nv.setId(nhanVien.getId());
        nv.setAvatar(user.getAvatar());
        nv.setEmail(user.getEmail());
        nv.setMaNhanVien(nhanVien.getMaNhanVien());
        nv.setIdCV(nhanVien.getChucVu().getId());
        nv.setSdt(nhanVien.getSdt());
        nv.setCccd(nhanVien.getCccd());
        nv.setHoTen(nhanVien.getHoTen());
        nv.setNgaySinh(nhanVien.getNgaySinh());
        nv.setGioiTinh(nhanVien.isGioiTinh());
        nv.setTrangThai(nhanVien.isTrangThai());
        nv.setSoNha(nhanVien.getSoNha());
        nv.setPhuongXa(nhanVien.getPhuongXa());
        nv.setQuanHuyen(nhanVien.getQuanHuyen());
        nv.setTinhThanhPho(nhanVien.getTinhThanhPho());
        model.addAttribute("nhanVien", nv);
        model.addAttribute("listCV",chucVuService.getAll());
        return "cms/pages/users/detail-nhanVien";
    }

    @PostMapping("/update/{id}")
    public String updateNV(@PathVariable Long id, Model model,
                           @ModelAttribute("nhanVien") NhanVienRequest nhanVien){
        NhanVien updatedNhanVien = nhanVienService.detail(id);
        User user = userService.findByNhanVien_Id(nhanVien.getId());
        updatedNhanVien.setId(nhanVien.getId());
        updatedNhanVien.setChucVu(chucVuService.getById(nhanVien.getIdCV()));
        updatedNhanVien.setHoTen(nhanVien.getHoTen());
        updatedNhanVien.setSdt(nhanVien.getSdt());
        updatedNhanVien.setTrangThai(nhanVien.isTrangThai());
        updatedNhanVien.setGioiTinh(nhanVien.isGioiTinh());
        updatedNhanVien.setCccd(nhanVien.getCccd());
        updatedNhanVien.setNgaySinh(nhanVien.getNgaySinh());
        updatedNhanVien.setSoNha(nhanVien.getSoNha());
        updatedNhanVien.setPhuongXa(nhanVien.getPhuongXa());
        updatedNhanVien.setQuanHuyen(nhanVien.getQuanHuyen());
        updatedNhanVien.setTinhThanhPho(nhanVien.getTinhThanhPho());
        user.setEmail(nhanVien.getEmail());
        user.setAvatar(nhanVien.getAvatar());
        user.setNhanVien(updatedNhanVien);
        nhanVienService.update(updatedNhanVien, id);
        userService.update(user);
        return "redirect:/cms/nhan-vien";
    }

    @GetMapping("/delete/{id}")
    public String deleteNV(@PathVariable Long id, Model model) {
        nhanVienService.delete(id);
        return "redirect:/cms/nhan-vien";
    }
}
