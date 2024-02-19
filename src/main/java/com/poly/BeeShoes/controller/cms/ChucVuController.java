package com.poly.BeeShoes.controller.cms;

import com.poly.BeeShoes.model.ChucVu;
import com.poly.BeeShoes.service.ChucVuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class ChucVuController {
    private final ChucVuService chucVuService;

    @GetMapping("/chuc-vu")
    public String chucVu(Model model) {
        List<ChucVu> list = chucVuService.getAll();
        model.addAttribute("listCV", list);
        return "cms/pages/users/chuc-vu";
    }
}
