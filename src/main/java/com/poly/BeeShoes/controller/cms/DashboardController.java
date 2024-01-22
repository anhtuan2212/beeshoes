package com.poly.BeeShoes.controller.cms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cms")
public class DashboardController {
    @GetMapping({"/","","/index"})
    public String indexDashboard(){
        return "cms/index";
    }

}
