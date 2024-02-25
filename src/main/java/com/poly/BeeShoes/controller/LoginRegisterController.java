package com.poly.BeeShoes.controller;

import com.poly.BeeShoes.dto.LoginDto;
import com.poly.BeeShoes.dto.RegisterDto;
import com.poly.BeeShoes.model.KhachHang;
import com.poly.BeeShoes.model.Role;
import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.service.HangKhachHangService;
import com.poly.BeeShoes.service.KhachHangService;
import com.poly.BeeShoes.service.UserService;
import com.poly.BeeShoes.utility.ConvertUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class LoginRegisterController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final KhachHangService khachHangService;
    private final HangKhachHangService hangKhachHangService; // page đăng ký nmmowr cho em

    @GetMapping("/login")
    public String formLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "customer/auth/login";
    }

    @GetMapping("/register")
    public String formRegister(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "customer/auth/register";
    }

    @PostMapping("/login-handle")
    public String login(
            @Valid @ModelAttribute LoginDto loginDto,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpSession session,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "customer/auth/login";
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            return "redirect:/light-bee/";
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            model.addAttribute("message", "Sai email hoặc mật khẩu !");
            return "customer/auth/login";
        }
    }

    @PostMapping("/register")
    @Transactional
    public String register(
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpSession session,
            Model model
    ) {
        if(bindingResult.hasErrors()){
            return "customer/auth/register";
        } else {
            if(userService.existByUsername(registerDto.getEmail())) {
                model.addAttribute("email", "Email đã tồn tại!");
                return "customer/auth/register";
            } else {
                try {
                    KhachHang khachHang = new KhachHang();
                    khachHang.setHoTen(registerDto.getHo() + " " + registerDto.getTenDem() + " " + registerDto.getTen());
                    khachHang.setTrangThai(true);
                    khachHang.setDiem(0);
                    khachHang.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
                    khachHang.setGioiTinh(registerDto.isGioiTinh());
                    khachHang.setSdt(registerDto.getSdt());
                    khachHang.setMaKhachHang(khachHangService.generateCustomerCode());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(registerDto.getNgaySinh());
                    khachHang.setNgaySinh(new java.sql.Date(date.getTime()));
                    KhachHang createdKhachHang = khachHangService.add(khachHang);

                    User user = new User();
                    user.setEmail(registerDto.getEmail());
                    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                    user.setTrangThai(true); // true = active
                    user.setKhachHang(khachHang);
                    user.setRole(Role.CUSTOMER);
                    user.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
                    User createdUser = userService.createNewUser(user);

                    if(createdKhachHang == null || createdUser == null) {
                        throw new RuntimeException("Lỗi khi tạo mới khách hàng , vui lòng thử lại!");
                    } else {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(registerDto.getEmail(), registerDto.getPassword())
                        );
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        securityContext.setAuthentication(authentication);
                        session = request.getSession(true);
                        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                        User userTest = (User) authentication.getPrincipal();
                        System.out.println("hi " + userTest.getKhachHang().getHoTen());
                        return "redirect:/light-bee/";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    model.addAttribute("message", "Lỗi khi tạo mới khách hàng , vui lòng thử lại!");
                    return "customer/auth/register";
                }
            }
        }
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "cms/error/401";
    }
}
