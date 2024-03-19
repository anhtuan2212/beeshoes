package com.poly.BeeShoes.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.payment.vnpay.VNPayService;
import com.poly.BeeShoes.service.*;
import com.poly.BeeShoes.utility.ConvertUtility;
import com.poly.BeeShoes.utility.MailUtility;
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
    @Autowired
    private UserService userService;
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private NhanVienService nhanVienService;
    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;
    @Autowired
    private MailUtility mailUtility;

    @PostMapping("/placeOrder-online")
    public ResponseEntity<String> createOrderOnline(
            @RequestBody String paymentDto,
            HttpServletRequest request
    ) {
        JsonObject jsonObject = JsonParser.parseString(paymentDto).getAsJsonObject();
        String notes = jsonObject.get("notes").getAsString();
        int total = jsonObject.get("total").getAsInt();
        int shippingFee = jsonObject.get("shippingFee").getAsInt();
        int totalAmount = jsonObject.get("totalAmount").getAsInt();
        String voucherCode = null;
        BigDecimal voucherValue = new BigDecimal(0);
        if(jsonObject.get("voucher") != null) {
            voucherCode = jsonObject.get("voucher").getAsString();
        }
        if(jsonObject.get("voucherValue") != null) {
            voucherValue = jsonObject.get("voucherValue").getAsBigDecimal();
        }
        JsonArray jsonArray = jsonObject.getAsJsonArray("productDetail");
        String customerName = jsonObject.get("customerName").getAsString();
        String customerPhone = jsonObject.get("customerPhone").getAsString();
        String addressReceive = jsonObject.get("addressReceive").getAsString();
        String orderCode = jsonObject.get("orderCode").getAsString();
        HoaDon hoaDon = new HoaDon();
        if(request.getUserPrincipal() != null) {
            User user = userService.getByUsername(request.getUserPrincipal().getName());
            if(user.getNhanVien() == null) {
                KhachHang khachHang = khachHangService.getByMa(user.getKhachHang().getMaKhachHang());
                hoaDon.setKhachHang(khachHang);
            }
        }
        hoaDon.setMaVanChuyen(orderCode);
        hoaDon.setMaHoaDon(hoaDonService.generateInvoiceCode());
        hoaDon.setTrangThai(TrangThaiHoaDon.ChoXacNhan);
        hoaDon.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
        hoaDon.setTongTien(BigDecimal.valueOf(total));
        hoaDon.setSoTienDaThanhToan(BigDecimal.valueOf(totalAmount));
        hoaDon.setGiamGia(voucherValue);
        hoaDon.setThucThu(BigDecimal.valueOf(totalAmount));
        hoaDon.setPhiShip(BigDecimal.valueOf(shippingFee));
        hoaDon.setSdtNhan(customerPhone);
        hoaDon.setTenNguoiNhan(customerName);
        hoaDon.setDiaChiNhan(addressReceive);
        Voucher voucher = voucherService.getByMa(voucherCode);
        hoaDon.setVoucher(voucher);
        hoaDon.setLoaiHoaDon(false);
        hoaDon.setHinhThucThanhToan(PaymentMethod.Online);
        HoaDon savedHoaDon = hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setTrangThaiSauUpdate(TrangThaiHoaDon.ChoXacNhan.name());
        lichSuHoaDon.setHoaDon(savedHoaDon);
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setHanhDong("Đặt đơn hàng");
        lichSuHoaDonService.save(lichSuHoaDon);
        if(voucher != null) {
            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucherService.save(voucher);
        }
        for(JsonElement e : jsonArray) {
            Long idPDetail = e.getAsJsonObject().get("productDetailId").getAsLong();
            int quantity = e.getAsJsonObject().get("quantity").getAsInt();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietSanPham chiTietSanPham = chiTietSanPhamService.getById(idPDetail);
            hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
            hoaDonChiTiet.setGiaGoc(chiTietSanPham.getGiaBan());
            hoaDonChiTiet.setGiaBan(chiTietSanPham.getGiaBan());
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietService.save(hoaDonChiTiet);
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - quantity);
            chiTietSanPhamService.save(chiTietSanPham);

            System.out.println(e.getAsJsonObject().get("productDetailId").getAsInt());
            System.out.println(e.getAsJsonObject().get("quantity").getAsInt());
            System.out.println(notes + " " + totalAmount + " " + voucherCode);
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
        String vnpUrl = vnPayService.createOrder(savedHoaDon.getMaHoaDon(), total, notes, baseUrl);
        return new ResponseEntity<>(vnpUrl, HttpStatus.OK);
    }

    @PostMapping("/placeOrder-whenReceive")
    public ResponseEntity<String> createOrderWhenReceive(
            @RequestBody String paymentDto,
            HttpServletRequest request,
            Model model
    ) {
        JsonObject jsonObject = JsonParser.parseString(paymentDto).getAsJsonObject();
        String notes = jsonObject.get("notes").getAsString();
        int total = jsonObject.get("total").getAsInt();
        int shippingFee = jsonObject.get("shippingFee").getAsInt();
        int totalAmount = jsonObject.get("totalAmount").getAsInt();
        String voucherCode = null;
        BigDecimal voucherValue = new BigDecimal(0);
        if(jsonObject.get("voucher") != null) {
            voucherCode = jsonObject.get("voucher").getAsString();
        }
        if(jsonObject.get("voucherValue") != null || !jsonObject.get("voucherValue").isJsonNull()) {
            voucherValue = jsonObject.get("voucherValue").getAsBigDecimal();
        }
        JsonArray jsonArray = jsonObject.getAsJsonArray("productDetail");
        String customerName = jsonObject.get("customerName").getAsString();
        String customerPhone = jsonObject.get("customerPhone").getAsString();
        String addressReceive = jsonObject.get("addressReceive").getAsString();
        String orderCode = jsonObject.get("orderCode").getAsString();
        HoaDon hoaDon = new HoaDon();
        if(request.getUserPrincipal() != null) {
            User user = userService.getByUsername(request.getUserPrincipal().getName());
            if(user.getNhanVien() == null) {
                KhachHang khachHang = khachHangService.getByMa(user.getKhachHang().getMaKhachHang());
                hoaDon.setKhachHang(khachHang);
            }
        }
        hoaDon.setMaVanChuyen(orderCode);
        hoaDon.setMaHoaDon(hoaDonService.generateInvoiceCode());
        hoaDon.setTrangThai(TrangThaiHoaDon.ChoXacNhan);
        hoaDon.setNgayTao(ConvertUtility.DateToTimestamp(new Date()));
        hoaDon.setTongTien(BigDecimal.valueOf(total));
        hoaDon.setThucThu(BigDecimal.valueOf(totalAmount));
        hoaDon.setSoTienCanThanhToan(BigDecimal.valueOf(totalAmount));
        hoaDon.setGiamGia(voucherValue);
        hoaDon.setPhiShip(BigDecimal.valueOf(shippingFee));
        hoaDon.setSdtNhan(customerPhone);
        hoaDon.setTenNguoiNhan(customerName);
        hoaDon.setDiaChiNhan(addressReceive);
        Voucher voucher = voucherService.getByMa(voucherCode);
        hoaDon.setVoucher(voucher);
        hoaDon.setLoaiHoaDon(false);
        hoaDon.setHinhThucThanhToan(PaymentMethod.KhiNhanHang);
        HoaDon savedHoaDon = hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setTrangThaiSauUpdate(TrangThaiHoaDon.ChoXacNhan.name());
        lichSuHoaDon.setHoaDon(savedHoaDon);
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setHanhDong("Đặt đơn hàng");
        lichSuHoaDonService.save(lichSuHoaDon);
        if(voucher != null) {
            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucherService.save(voucher);
        }
        for(JsonElement e : jsonArray) {
            Long idPDetail = e.getAsJsonObject().get("productDetailId").getAsLong();
            int quantity = e.getAsJsonObject().get("quantity").getAsInt();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietSanPham chiTietSanPham = chiTietSanPhamService.getById(idPDetail);
            hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
            hoaDonChiTiet.setGiaBan(chiTietSanPham.getGiaBan());
            hoaDonChiTiet.setGiaGoc(chiTietSanPham.getGiaBan());
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietService.save(hoaDonChiTiet);
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - quantity);
            chiTietSanPhamService.save(chiTietSanPham);

            System.out.println(e.getAsJsonObject().get("productDetailId").getAsInt());
            System.out.println(e.getAsJsonObject().get("quantity").getAsInt());
            System.out.println(notes + " " + totalAmount + " " + voucherCode);
        }
        if(savedHoaDon.getKhachHang() != null) {
            User user = userService.findByKhachHang_Id(savedHoaDon.getKhachHang().getId());
            mailUtility.sendMail(user.getEmail(), "[LightBee - Đặt hàng thành công]", "Cảm ơn bạn :D");
        }
        String urlRedirect = "/orderSuccess?invoiceCode=" + savedHoaDon.getMaHoaDon() + "&totalAmount=" + savedHoaDon.getThucThu();
        return new ResponseEntity<>(urlRedirect, HttpStatus.OK);
    }

}
