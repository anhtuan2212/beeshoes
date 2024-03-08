package com.poly.BeeShoes.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.payment.vnpay.VNPayService;
import com.poly.BeeShoes.service.ChiTietSanPhamService;
import com.poly.BeeShoes.service.HoaDonChiTietService;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.VoucherService;
import com.poly.BeeShoes.utility.ConvertUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/check-out")
public class CheckOutRestController {

    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @PostMapping("/placeOrder-online")
    public ResponseEntity<String> createOrderOnline(
            @RequestBody String paymentDto,
            HttpServletRequest request
    ) {
        JsonObject jsonObject = JsonParser.parseString(paymentDto).getAsJsonObject();
        String notes = jsonObject.get("notes").getAsString();
        int total = jsonObject.get("total").getAsInt();
        String voucherCode = jsonObject.get("voucher").getAsString();
        JsonArray jsonArray = jsonObject.getAsJsonArray("productDetail");
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(hoaDonService.generateInvoiceCode());
        hoaDon.setTrangThai(TrangThaiHoaDon.ChoXacNhan);
        hoaDon.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
        hoaDon.setThucThu(BigDecimal.valueOf(total));
        Voucher voucher = voucherService.getByMa(voucherCode);
        hoaDon.setVoucher(voucher);
        hoaDon.setLoaiHoaDon(false);
        HoaDon savedHoaDon = hoaDonService.save(hoaDon);
        voucher.setSoLuong(voucher.getSoLuong() - 1);
        voucherService.save(voucher);
        for(JsonElement e : jsonArray) {
            Long idPDetail = e.getAsJsonObject().get("productDetailId").getAsLong();
            int quantity = e.getAsJsonObject().get("quantity").getAsInt();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietSanPham chiTietSanPham = chiTietSanPhamService.getById(idPDetail);
            hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietService.save(hoaDonChiTiet);
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - quantity);
            chiTietSanPhamService.save(chiTietSanPham);

            System.out.println(e.getAsJsonObject().get("productDetailId").getAsInt());
            System.out.println(e.getAsJsonObject().get("quantity").getAsInt());
            System.out.println(notes + " " + total + " " + voucherCode);
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
        String vnpUrl = vnPayService.createOrder(savedHoaDon.getMaHoaDon(), total, notes, baseUrl);
        return new ResponseEntity<>(vnpUrl, HttpStatus.OK);
    }

    @PostMapping("/placeOrder-whenReceive")
    public ResponseEntity<String> createOrderWhenReceive(
            @RequestBody String paymentDto,
            Model model
    ) {
        JsonObject jsonObject = JsonParser.parseString(paymentDto).getAsJsonObject();
        String notes = jsonObject.get("notes").getAsString();
        int total = jsonObject.get("total").getAsInt();
        String voucherCode = jsonObject.get("voucher").getAsString();
        JsonArray jsonArray = jsonObject.getAsJsonArray("productDetail");
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(hoaDonService.generateInvoiceCode());
        hoaDon.setTrangThai(TrangThaiHoaDon.ChoXacNhan);
        hoaDon.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
        hoaDon.setThucThu(BigDecimal.valueOf(total));
        Voucher voucher = voucherService.getByMa(voucherCode);
        hoaDon.setVoucher(voucher);
        hoaDon.setLoaiHoaDon(false);
        HoaDon savedHoaDon = hoaDonService.save(hoaDon);
        voucher.setSoLuong(voucher.getSoLuong() - 1);
        voucherService.save(voucher);
        for(JsonElement e : jsonArray) {
            Long idPDetail = e.getAsJsonObject().get("productDetailId").getAsLong();
            int quantity = e.getAsJsonObject().get("quantity").getAsInt();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietSanPham chiTietSanPham = chiTietSanPhamService.getById(idPDetail);
            hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietService.save(hoaDonChiTiet);
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - quantity);
            chiTietSanPhamService.save(chiTietSanPham);

            System.out.println(e.getAsJsonObject().get("productDetailId").getAsInt());
            System.out.println(e.getAsJsonObject().get("quantity").getAsInt());
            System.out.println(notes + " " + total + " " + voucherCode);
        }
        String urlRedirect = "/orderSuccess?invoiceCode=" + savedHoaDon.getMaHoaDon() + "&total=" + savedHoaDon.getThucThu();
        return new ResponseEntity<>(urlRedirect, HttpStatus.OK);
    }

}
