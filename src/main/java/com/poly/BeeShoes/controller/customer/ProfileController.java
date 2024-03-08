package com.poly.BeeShoes.controller.customer;

import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    @GetMapping("/user-profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String email = userDetails.getUsername();
            model.addAttribute("user",userService.getByUsername(userDetails.getUsername()));
            System.out.println("User is authenticated. Email: " + email);
        } else {
            System.out.println("User is not authenticated or principal is not UserDetails instance");
        }
        return "customer/pages/profile/user-profile";
    }
}
