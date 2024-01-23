package com.poly.BeeShoes.controller.cms;


import com.poly.BeeShoes.model.Voucher;
import com.poly.BeeShoes.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/cms")
public class VoucherController {
  @Autowired
    private VoucherService voucherService;

  @GetMapping("/voucher")
    public String getAll(Model model){
      model.addAttribute("listVC",voucherService.getAll());
      return "cms/pages/voucher/voucher.html";
  }

  @GetMapping("/add-voucher")
  public String addvoucher(){

    return"cms/pages/voucher/add-Voucher.html";
  }
  @PostMapping("/add")
  public String add(Model model,@ModelAttribute Voucher voucher){

            var check=0;
        if(voucher.getMa().isEmpty()){
            model.addAttribute("tb1","Không để trống mã");
            check=1;
        }
        if(voucher.getTen().isEmpty()) {
            model.addAttribute("tb2", "Không để trống tên");
            check = 1;
        }
//        }  if(voucher.getGiaTriTienMat() == null){
//            model.addAttribute("tb3","Không để trống gía trị tiền mặt");
//            check=1;
//        } if(voucher.getGiaTriToiDa()==null){
//            model.addAttribute("tb4","Không để trống giá trị tối đa");
//            check=1;
//        }

        if(voucher.getDieuKien().isEmpty()){
            model.addAttribute("tb5","Không để trống điều kiện");
            check=1;
        }
          if(check==1){
            return"cms/pages/voucher/add-Voucher.html";
        }
    model.addAttribute("listVC",voucherService.getAll());
    voucherService.save(voucher);
    return "cms/pages/voucher/voucher.html";
  }
}
