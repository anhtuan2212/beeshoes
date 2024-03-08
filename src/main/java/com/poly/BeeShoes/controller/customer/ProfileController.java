package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    public Authentication getUserAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
    @GetMapping("/user-profile")
    public String profile() {
        Authentication auth = getUserAuth();
        System.out.println(auth.toString());
        User user = (User) auth.getPrincipal();
        System.out.println(user.getEmail());
        return "customer/pages/profile/user-profile";
    }
}
