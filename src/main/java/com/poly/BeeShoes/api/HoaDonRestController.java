package com.poly.BeeShoes.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.poly.BeeShoes.dto.LichSuHoaDonDto;
import com.poly.BeeShoes.dto.WardDto;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.request.*;
import com.poly.BeeShoes.service.*;
import com.poly.BeeShoes.utility.ConvertUtility;
import com.poly.BeeShoes.utility.MailUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonRestController {

    private final HoaDonService hoaDonService;
    private final LichSuHoaDonService lichSuHoaDonService;
    private final HoaDonChiTietService hoaDonChiTietService;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final VoucherService voucherService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final MauSacService mauSacService;
    private final KichCoService kichCoService;
    private final SanPhamService sanPhamService;
    private final KhachHangService khachHangService;
    private final MailUtility mailUtility;
    private final HinhThucThanhToanService hinhThucThanhToanService;
    Gson gson = new Gson();

    @GetMapping("/get-all-hoadon")
    public ResponseEntity getAll(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User  user = userService.getByUsername(userDetails.getUsername());
//            List<HoaDon> ListHD = hoaDonService.getByKhachHang(user.getKhachHang());
//            List<HoaDon> lst = new ArrayList<>();
//            ListHD.forEach(item->{
//                item.setNhanVien(null);
//                item.setKhachHang(null);
//                item.setVoucher(null);
//                item.setNhanVien(null);
//                item.setNhanVien(null);
//                item.setNhanVien(null);
//            });
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().header("status", "NotAuth").build();
        }
    }
    @PostMapping("/update-quantity")
    public ResponseEntity update(@RequestParam("id") Long id,
                                 @RequestParam("num") Integer num,
                                 @RequestParam("calcu") String calcu) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            HoaDonChiTiet hdct = hoaDonChiTietService.getById(id);
            if (hdct == null) {
                return ResponseEntity.notFound().header("status", "HDCTisNull").build();
            }
            ChiTietSanPham ctsp = hdct.getChiTietSanPham();
            HoaDonChiTiet newHD = null;
            boolean isNew = false;
            if (ctsp.getGiaBan().intValue() == hdct.getGiaBan().intValue()) {
                if (calcu.equals("plus")) {
                    if (hdct.getSoLuong() + num > ctsp.getSoLuongTon()) {
                        return ResponseEntity.notFound().header("status", "maxQuantity").build();
                    }
                    hdct.setSoLuong(hdct.getSoLuong() + num);
                } else {
                    if (hdct.getSoLuong() - num < 1) {
                        return ResponseEntity.notFound().header("status", "minQuantity").build();
                    } else {
                        hdct.setSoLuong(hdct.getSoLuong() - num);
                    }
                }
                hdct = hoaDonChiTietService.save(hdct);
            } else {
                if (calcu.equals("plus")) {
                    List<HoaDonChiTiet> lst = hdct.getHoaDon().getHoaDonChiTiets();
                    for (HoaDonChiTiet item : lst) {
                        if (item.getChiTietSanPham().getId() == ctsp.getId() && item.getGiaBan().intValue() == ctsp.getGiaBan().intValue()) {
                            System.out.println(item.getChiTietSanPham().getMaSanPham());
                            newHD = item;
                            newHD.setSoLuong(item.getSoLuong() + num);
                            break;
                        }
                    }
                    if (newHD == null) {
                        isNew = true;
                        newHD = new HoaDonChiTiet();
                        newHD.setSoLuong(num);
                        newHD.setChiTietSanPham(ctsp);
                        newHD.setGiaGoc(ctsp.getGiaGoc());
                        newHD.setGiaBan(ctsp.getGiaBan());
                        newHD.setHoaDon(hdct.getHoaDon());
                    }
                    newHD = hoaDonChiTietService.save(newHD);
                } else {
                    if (hdct.getSoLuong() - num < 1) {
                        return ResponseEntity.notFound().header("status", "minQuantity").build();
                    } else {
                        hdct.setSoLuong(hdct.getSoLuong() - num);
                    }
                }
            }
            HoaDon hoaDonRes = hoaDonService.getHoaDonById(hdct.getHoaDon().getId()).orElse(null);
            assert hoaDonRes != null;
            if (isNew){
                List<HoaDonChiTiet> lstHo = hoaDonRes.getHoaDonChiTiets();
                lstHo.add(newHD);
                hoaDonRes.setHoaDonChiTiets(lstHo);
            }
            hoaDonRes = tinhTien(hoaDonRes);
            List<HDCTResponse> lst = getHdctResponse(hoaDonRes);
            return ResponseEntity.ok().body(lst);
        } else {
            return ResponseEntity.notFound().header("status", "NotAuth").build();
        }
    }

    private static List<HDCTResponse> getHdctResponse(HoaDon hoaDon) {
        List<HDCTResponse> lst = new ArrayList<>();
        for (int i = 0; i < hoaDon.getHoaDonChiTiets().size(); i++) {
            HoaDonChiTiet newHD = hoaDon.getHoaDonChiTiets().get(i);
            HDCTResponse response = new HDCTResponse();
            response.setImg(newHD.getChiTietSanPham().getAnh().getUrl());
            response.setKichCo(newHD.getChiTietSanPham().getKichCo().getTen());
            response.setMaMauSac(newHD.getChiTietSanPham().getMauSac().getMaMauSac());
            response.setTenMau(newHD.getChiTietSanPham().getMauSac().getTen());
            response.setTen(newHD.getChiTietSanPham().getSanPham().getTen());
            response.setIdHDCT(newHD.getId());
            response.setIdCTSP(newHD.getChiTietSanPham().getId());
            response.setGiaBan(newHD.getGiaBan().intValue());
            response.setGiaGoc(newHD.getGiaGoc().intValue());
            response.setSoLuong(newHD.getSoLuong());
            response.setTongTien(hoaDon.getTongTien().intValue());
            response.setThucThu(hoaDon.getThucThu().intValue());
            response.setGiamGia(hoaDon.getGiamGia().intValue());
            lst.add(response);
        }
        return lst;
    }

    @PostMapping("/xac-nhan")
    public ResponseEntity<String> xacNhanDon(
            @RequestBody List<String> maHoaDonList
    ) {
        List<HoaDon> hoaDonList = new ArrayList<>();
        maHoaDonList.forEach(ma -> {
            HoaDon hoaDon = hoaDonService.getHoaDonByMa(ma);
            String trangThaiBeforeUpdate = hoaDon.getTrangThai().name();
            if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoXacNhan) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ChuanBiHang);
            } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChuanBiHang) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ChoGiao);
            } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoGiao) {
                hoaDon.setTrangThai(TrangThaiHoaDon.DangGiao);
            } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.DangGiao) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ThanhCong);
            }
            hoaDon.setNgayXacNhan(new Date());
            HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(updatedHoaDon);
            lichSuHoaDon.setHanhDong("Thay đổi trạng thái từ: " + trangThaiBeforeUpdate + " -> " + updatedHoaDon.getTrangThai().name());
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername("tuyen"));
            lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
            String trangThaiSauUpdate = updatedHoaDon.getTrangThai().name();
            lichSuHoaDon.setTrangThaiSauUpdate(trangThaiSauUpdate);
            lichSuHoaDonService.save(lichSuHoaDon);
            hoaDonList.add(updatedHoaDon);
            System.out.println(updatedHoaDon.getMaHoaDon() + " | " + updatedHoaDon.getTrangThai().name());
        });
        return ResponseEntity.status(HttpStatus.OK).body("Xác nhận thành công đơn hàng");
    }

    @PostMapping("/payment-cashier")
    public ResponseEntity banTaiQuay(@ModelAttribute PaymentCashierRequest request) {
        TypePaymentRequest typePayment = gson.fromJson(request.getTypePayment(), TypePaymentRequest.class);
        System.out.println(request);
        Type listType = new TypeToken<List<ProductCashierRequest>>() {
        }.getType();
        List<ProductCashierRequest> listProduct = gson.fromJson(request.getProduct(), listType);
        List<HoaDonChiTiet> lstHDCT = new ArrayList<>();
        Voucher voucher = voucherService.getByMa(request.getVoucher());
        List<HinhThucThanhToan> httt = new ArrayList<>();
        KhachHang kh = new KhachHang();
        if (!request.getCustomer().equals("#")) {
            kh = khachHangService.detail(Long.parseLong(request.getCustomer()));
        }
        if (typePayment.isChuyenKhoan()) {
            HinhThucThanhToan ht = hinhThucThanhToanService.getByTen("ChuyenKhoan");
            if (ht == null) {
                ht = new HinhThucThanhToan();
                ht.setHinhThuc("ChuyenKhoan");
                ht.setTrangThai(true);
                ht.setMoTa("Chuyển Khoản");
                ht = hinhThucThanhToanService.save(ht);
                httt.add(ht);
            }
        }
        if (typePayment.isTienMat()) {
            HinhThucThanhToan ht = hinhThucThanhToanService.getByTen("TienMat");
            if (ht == null) {
                ht = new HinhThucThanhToan();
                ht.setHinhThuc("TienMat");
                ht.setTrangThai(true);
                ht.setMoTa("Tiền Mặt");
                ht = hinhThucThanhToanService.save(ht);
                httt.add(ht);
            }
        }
        if (typePayment.isKhiNhanHang()) {
            HinhThucThanhToan ht = hinhThucThanhToanService.getByTen("KhiNhanHang");
            if (ht == null) {
                ht = new HinhThucThanhToan();
                ht.setHinhThuc("KhiNhanHang");
                ht.setTrangThai(true);
                ht.setMoTa("Thanh Toán Khi Nhận Hàng");
                ht = hinhThucThanhToanService.save(ht);
                httt.add(ht);
            }
        }
        HoaDon hd = new HoaDon();
        Integer total = 0;
        for (int i = 0; i < listProduct.size(); i++) {
            ProductCashierRequest pro = listProduct.get(i);
            ChiTietSanPham ctsp = chiTietSanPhamService.getById(pro.getId());
            total += ctsp.getGiaBan().intValue() * pro.getQuantity();
        }


        Integer giamGia = 0;
        if (voucher != null && total >= voucher.getGiaTriToiThieu().intValue()) {
            if (voucher.getLoaiVoucher().equals("$")) {
                giamGia = voucher.getGiaTriTienMat().intValue();
            } else {
                giamGia = total * voucher.getGiaTriPhanTram();
                if (giamGia > voucher.getGiaTriToiDa().intValue()) {
                    giamGia = voucher.getGiaTriToiDa().intValue();
                }
            }
        }
        Integer thucthu = total - giamGia + request.getShippingFee();
        if (request.getCustomer().equals("#")) {
            hd.setTenNguoiNhan("Khách Lẻ");
            hd.setKhachHang(null);
        } else {
            hd.setTenNguoiNhan(kh.getHoTen());
            hd.setKhachHang(kh);
            hd.setSdtNhan(kh.getSdt());
        }
        hd.setLoaiHoaDon(true);
        hd.setPhiShip(BigDecimal.valueOf(request.getShippingFee()));
        hd.setMaHoaDon(hoaDonService.generateInvoiceCode());
        hd.setTongTien(BigDecimal.valueOf(total));
        hd.setGiamGia(BigDecimal.valueOf(giamGia));
        hd.setHinhThucThanhToans(httt);
        hd.setVoucher(voucher);
        hd.setNgayTao(Timestamp.from(Instant.now()));
        hd.setThucThu(BigDecimal.valueOf(thucthu));
        if (request.getReceivingType().equals("TQ")) {
            hd.setDiaChiNhan("Tại Quầy");
            if (thucthu <= request.getTransfer() + request.getCash()) {
                hd.setSoTienDaThanhToan(BigDecimal.valueOf(thucthu));
                hd.setSoTienCanThanhToan(BigDecimal.ZERO);
            } else {
                hd.setSoTienDaThanhToan(BigDecimal.valueOf(request.getTransfer() + request.getCash()));
                Integer thieu = thucthu - request.getTransfer() + request.getCash();
                hd.setSoTienCanThanhToan(BigDecimal.valueOf(thieu));
            }
            hd.setTrangThai(TrangThaiHoaDon.ThanhCong);
        } else {
            hd.setDiaChiNhan(request.getAddress());
            if (typePayment.isKhiNhanHang()) {
                hd.setSoTienCanThanhToan(BigDecimal.valueOf(thucthu));
                hd.setSoTienDaThanhToan(BigDecimal.ZERO);
            } else {
                if (thucthu <= request.getTransfer() + request.getCash()) {
                    hd.setSoTienDaThanhToan(BigDecimal.valueOf(thucthu));
                    hd.setSoTienCanThanhToan(BigDecimal.ZERO);
                } else {
                    hd.setSoTienDaThanhToan(BigDecimal.valueOf(request.getTransfer() + request.getCash()));
                    Integer thieu = thucthu - request.getTransfer() + request.getCash();
                    hd.setSoTienCanThanhToan(BigDecimal.valueOf(thieu));
                }
            }
            hd.setTrangThai(TrangThaiHoaDon.ChuanBiHang);
        }
        User userTH = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.getByUsername(userDetails.getUsername());
            if (user.getNhanVien() != null) {
                userTH = user;
                hd.setNhanVien(user.getNhanVien());
            } else {
                hd.setNhanVien(null);
            }
        }

        hd = hoaDonService.save(hd);
        for (int i = 0; i < listProduct.size(); i++) {
            ProductCashierRequest pro = listProduct.get(i);
            ChiTietSanPham ctsp = chiTietSanPhamService.getById(pro.getId());
            HoaDonChiTiet hdct = new HoaDonChiTiet();
            hdct.setChiTietSanPham(ctsp);
            hdct.setHoaDon(hd);
            hdct.setSoLuong(pro.getQuantity());
            hdct.setGiaBan(ctsp.getGiaBan());
            hdct.setGiaGoc(ctsp.getGiaGoc());
            hdct = hoaDonChiTietService.save(hdct);
            lstHDCT.add(hdct);
        }

        hd.setHoaDonChiTiets(lstHDCT);
        String ten = hd.getNhanVien() == null ? "anonymous" : hd.getNhanVien().getHoTen();
        List<LichSuHoaDon> listLshd = new ArrayList<>();
        if (request.getReceivingType().equals("TQ")) {
            LichSuHoaDon lshd = new LichSuHoaDon();
            lshd.setHoaDon(hd);
            lshd.setHanhDong("Đơn Hàng Tạo Bởi :" + ten);
            lshd.setThoiGian(Timestamp.from(Instant.now()));
            lshd.setNguoiThucHien(userTH);
            lshd.setTrangThaiSauUpdate("ThanhCong");
            lshd = lichSuHoaDonService.save(lshd);
            listLshd.add(lshd);
        } else {
            LichSuHoaDon tao = new LichSuHoaDon();
            tao.setHoaDon(hd);
            tao.setHanhDong("Đơn Hàng Tạo Bởi " + ten + "Tại Quầy.");
            tao.setThoiGian(Timestamp.from(Instant.now()));
            tao.setNguoiThucHien(userTH);
            tao.setTrangThaiSauUpdate("ChuanBiHang");
            tao = lichSuHoaDonService.save(tao);
            LichSuHoaDon cb = new LichSuHoaDon();
            cb.setHoaDon(hd);
            cb.setHanhDong("Đơn Hàng được xác nhận bởi : " + ten);
            cb.setThoiGian(Timestamp.from(Instant.now()));
            cb.setNguoiThucHien(userTH);
            cb.setTrangThaiSauUpdate("ChuanBiHang");
            cb = lichSuHoaDonService.save(cb);
            listLshd.add(cb);
            listLshd.add(tao);
        }
        hd.setLichSuHoaDons(listLshd);
        hd = hoaDonService.save(hd);
        ResponseOder response = new ResponseOder();
        List<ProductInResponse> lstPro = new ArrayList<>();
        for (int i = 0; i < hd.getHoaDonChiTiets().size(); i++) {
            HoaDonChiTiet hdct = hd.getHoaDonChiTiets().get(i);
            ProductInResponse pro = new ProductInResponse();
            pro.setAnh(hdct.getChiTietSanPham().getAnh().getUrl());
            pro.setTen(hdct.getChiTietSanPham().getSanPham().getTen());
            pro.setId(hdct.getId());
            pro.setGiaGoc(hdct.getGiaGoc().intValue());
            pro.setGiaBan(hdct.getGiaBan().intValue());
            pro.setSoLuong(hdct.getSoLuong());
            pro.setKichCo(hdct.getChiTietSanPham().getKichCo().getTen());
            pro.setMauSac(hdct.getChiTietSanPham().getMauSac().getTen());
            pro.setMaSanPham(hdct.getChiTietSanPham().getMaSanPham());
            lstPro.add(pro);
        }
        response.setSanPham(lstPro);
        response.setDaThanhToan(hd.getSoTienDaThanhToan().intValue());
        response.setChuaThanhToan(hd.getSoTienCanThanhToan().intValue());
        response.setGiamGia(hd.getGiamGia().intValue());
        response.setPhiShip(hd.getPhiShip().intValue());
        response.setTenNguoiNhan(hd.getTenNguoiNhan());
        response.setSDT(hd.getSdtNhan());
        response.setMaHoaDon(hd.getMaHoaDon());
        response.setDiaChi(hd.getDiaChiNhan());
        response.setTongtien(hd.getTongTien().intValue());
        response.setThucthu(hd.getThucThu().intValue());
        response.setNgayTao(hd.getNgayTao());
        response.setType(request.getReceivingType());
        if (request.getReceivingType().equals("TQ")) {
            response.setNgayThanhToan(hd.getNgayTao());
        } else {
            response.setNgayThanhToan(null);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/huy")
    public ResponseEntity<String> huyDon(
            @RequestBody List<String> maHoaDonList
    ) {
        List<HoaDon> hoaDonList = new ArrayList<>();
        maHoaDonList.forEach(ma -> {
            HoaDon hoaDon = hoaDonService.getHoaDonByMa(ma);
            String trangThaiBeforeUpdate = hoaDon.getTrangThai().name();
            hoaDon.setTrangThai(TrangThaiHoaDon.Huy);
            hoaDon.setNgaySua(ConvertUtility.DateToTimestamp(new Date()));
            HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
            if (updatedHoaDon.getVoucher() != null) {
                Voucher voucher = voucherService.getByMa(updatedHoaDon.getVoucher().getMa());
                voucher.setSoLuong(voucher.getSoLuong() + 1);
                voucherService.save(voucher);
            }
            updatedHoaDon.getHoaDonChiTiets().forEach(hdct -> {
                ChiTietSanPham chiTietSanPham = chiTietSanPhamService.getById(hdct.getChiTietSanPham().getId());
                chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + hdct.getSoLuong());
                chiTietSanPhamService.save(chiTietSanPham);
            });
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(updatedHoaDon);
            lichSuHoaDon.setHanhDong("Thay đổi trạng thái từ: " + trangThaiBeforeUpdate + " -> " + updatedHoaDon.getTrangThai().name());
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername("tuyen"));
            lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
            String trangThaiSauUpdate = updatedHoaDon.getTrangThai().name();
            lichSuHoaDon.setTrangThaiSauUpdate(trangThaiSauUpdate);
            lichSuHoaDonService.save(lichSuHoaDon);
            hoaDonList.add(updatedHoaDon);
            System.out.println(updatedHoaDon.getMaHoaDon() + " | " + updatedHoaDon.getTrangThai().name());
        });
        return new ResponseEntity<>("Hủy thành công đơn hàng", HttpStatus.OK);
    }

    @PostMapping("/xac-nhan-detail")
    public ResponseEntity xacNhanDonDetail(
            @RequestBody Long idHoaDon,
            HttpServletRequest request
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idHoaDon).get();
        String trangThaiBeforeUpdate = hoaDon.getTrangThai().name();
        if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoXacNhan) {
            hoaDon.setTrangThai(TrangThaiHoaDon.ChuanBiHang);
        } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChuanBiHang) {
            hoaDon.setTrangThai(TrangThaiHoaDon.ChoGiao);
        } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoGiao) {
            hoaDon.setTrangThai(TrangThaiHoaDon.DangGiao);
        } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.DangGiao) {
            hoaDon.setTrangThai(TrangThaiHoaDon.ThanhCong);
        }
        hoaDon.setNgayXacNhan(new Date());
        HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(updatedHoaDon);
        lichSuHoaDon.setHanhDong("Thay đổi trạng thái từ: " + trangThaiBeforeUpdate + " -> " + updatedHoaDon.getTrangThai().name());
        if (request.getUserPrincipal() != null) {
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername(request.getUserPrincipal().getName()));
        }
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(updatedHoaDon.getTrangThai().name());
        LichSuHoaDon lshd = lichSuHoaDonService.save(lichSuHoaDon);
        LichSuHoaDonDto lshdDto = new LichSuHoaDonDto();
        lshdDto.setIdHoaDon(lshd.getHoaDon().getId());
        lshdDto.setId(lshd.getId());
        lshdDto.setHanhDong(lshd.getHanhDong());
        lshdDto.setTrangThaiSauUpdate(lshd.getTrangThaiSauUpdate());
        lshdDto.setThoiGian(lshd.getThoiGian());
        if (lshd.getNguoiThucHien() != null) {
            lshdDto.setNguoiThucHien(lshd.getNguoiThucHien().getNhanVien() != null ? lshd.getNguoiThucHien().getNhanVien().getHoTen() : "Anonymous");
        } else {
            lshdDto.setNguoiThucHien("Anoynomous");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lshdDto);
    }

    @PostMapping("/delete-product")
    public ResponseEntity xoaSanPham(@RequestParam("id") Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            user = userService.getByUsername(userDetails.getUsername());
        } else {
            return ResponseEntity.notFound().header("status", "NotAuth").build();
        }
        HoaDonChiTiet hdct = hoaDonChiTietService.getById(id);
        System.out.println(id);
        if (hdct == null) {
            return ResponseEntity.notFound().build();
        }
        HoaDon hoaDon = hdct.getHoaDon();
        List<HoaDonChiTiet> lst = hoaDon.getHoaDonChiTiets();
        if (lst.size() == 1) {
            return ResponseEntity.notFound().header("status", "minPro").build();
        }
        lst.remove(hdct);
        hoaDon.setHoaDonChiTiets(lst);
        hoaDon = tinhTien(hoaDon);
        hoaDonChiTietService.delete(hdct.getId());
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setHanhDong("Xóa sản phẩm '" + hdct.getChiTietSanPham().getMaSanPham() + "' khỏi hóa đơn.");
        if (request.getUserPrincipal() != null) {
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername(request.getUserPrincipal().getName()));
        }
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(hoaDon.getTrangThai().name());
        lichSuHoaDon = lichSuHoaDonService.save(lichSuHoaDon);
        if (user != null) {
            String tb = "Xóa sản phẩm khỏi hóa đơn thành công!";
            String body = "<h1>Bạn đã thực hiện xóa sản phẩm '" + hdct.getChiTietSanPham().getMaSanPham() + "' thành công!";
            mailUtility.sendMail(user.getEmail(), tb, body);
        }
        UpdateProductRquest rs = new UpdateProductRquest();
        rs.setId_hdct(hdct.getId());
        rs.setGiamGia(hoaDon.getGiamGia());
        rs.setTongTien(hoaDon.getTongTien());
        rs.setThucThu(hoaDon.getThucThu());
        rs.setTimes(lichSuHoaDon.getThoiGian());
        rs.setMessage(lichSuHoaDon.getHanhDong());
        return ResponseEntity.ok().body(rs);
    }

    private HoaDon tinhTien(HoaDon hoaDon) {
        List<HoaDonChiTiet> lstHDCT = new ArrayList<>(hoaDon.getHoaDonChiTiets());
        Map<Long, HoaDonChiTiet> uniqueMap = new LinkedHashMap<>();
        for (HoaDonChiTiet dct : lstHDCT) {
            uniqueMap.putIfAbsent(dct.getId(), dct);
        }
        lstHDCT = new ArrayList<>(uniqueMap.values());
        float total = lstHDCT.stream()
                .map(dct -> dct.getGiaBan().floatValue() * dct.getSoLuong())
                .reduce(0f, Float::sum);
        System.out.println("Total: " + total);

        hoaDon.setTongTien(BigDecimal.valueOf(total));

        float thucThu = 0;

        if (hoaDon.getGiamGia() != null) {
            thucThu = total - hoaDon.getGiamGia().floatValue();
        } else {
            thucThu = total;
        }
        thucThu += hoaDon.getPhiShip().floatValue();
        hoaDon.setThucThu(BigDecimal.valueOf(thucThu));
        hoaDon = hoaDonService.save(hoaDon);
        return hoaDon;
    }

    @PostMapping("/add-product")
    public ResponseEntity themsanPham(@ModelAttribute AddProductOderRequest dt, HttpServletRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            user = userService.getByUsername(userDetails.getUsername());
        } else {
            return ResponseEntity.notFound().header("status", "NotAuth").build();
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(dt.getId()).orElse(null);
        if (hoaDon == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "HoaDonNull").build();
        }
        MauSac mauSac = mauSacService.getMauSacByMa(dt.getMauSac());
        KichCo kichCo = kichCoService.getById(dt.getKichCo());
        SanPham sanPham = sanPhamService.getById(dt.getSanPham());
        ChiTietSanPham ctsp = chiTietSanPhamService.getBySizeAndColorAndProduct(kichCo, mauSac, sanPham);

        if (ctsp == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "CTSPNull").build();
        }

        List<HoaDonChiTiet> lst = hoaDon.getHoaDonChiTiets();
        List<HoaDonChiTiet> lstNew = lst.stream()
                .filter(hoaDonChiTiet -> hoaDonChiTiet.getChiTietSanPham().getId().equals(ctsp.getId()))
                .collect(Collectors.toList());

        HoaDonChiTiet hdct = null;
        for (HoaDonChiTiet hoaDonChiTiet : lstNew) {
            if (hoaDonChiTiet.getGiaBan().equals(ctsp.getGiaBan())) {
                hdct = hoaDonChiTiet;
                break;
            }
        }
        boolean isNew = true;
        if (hdct != null) {
            hdct.setSoLuong(hdct.getSoLuong() + dt.getSoLuong());
            isNew = false;
        } else {
            hdct = new HoaDonChiTiet();
            hdct.setHoaDon(hoaDon);
            hdct.setChiTietSanPham(ctsp);
            hdct.setSoLuong(dt.getSoLuong());
            hdct.setGiaGoc(ctsp.getGiaGoc());
            hdct.setGiaBan(ctsp.getGiaBan());
            isNew = true;
        }

        if (hdct.getSoLuong() < 1) {
            return ResponseEntity.notFound().header("status", "MinQuantity").build();
        }
        if (hdct.getSoLuong() > hdct.getChiTietSanPham().getSoLuongTon()) {
            return ResponseEntity.notFound().header("status", "MaxQuantity").build();
        }

        hdct = hoaDonChiTietService.save(hdct);
        lst.add(hdct);
        hoaDon.setHoaDonChiTiets(lst);
        hoaDon = hoaDonService.save(hoaDon);
        hoaDon = tinhTien(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setHanhDong("Thêm sản phẩm '" + ctsp.getMaSanPham() + "' vào hóa đơn.");
        if (request.getUserPrincipal() != null) {
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername(request.getUserPrincipal().getName()));
        }
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(hoaDon.getTrangThai().name());
        lichSuHoaDon = lichSuHoaDonService.save(lichSuHoaDon);
        if (user != null) {
            String tb = "Thêm sản phẩm vào hóa đơn thành công.";
            String body = "<h1>Bạn đã thực hiện xóa sản phẩm '" + hdct.getChiTietSanPham().getMaSanPham() + "' vào hóa đơn!";
            mailUtility.sendMail(user.getEmail(), tb, body);
        }
        UpdateProductRquest rs = new UpdateProductRquest();
        rs.setSoLuong(hdct.getSoLuong());
        rs.setId(ctsp.getId());
        rs.setId_hdct(hdct.getId());
        rs.setMaSanPham(ctsp.getMaSanPham());
        rs.setGiaGoc(ctsp.getGiaGoc().intValue());
        rs.setGiaBan(ctsp.getGiaBan().intValue());
        rs.setTen(ctsp.getSanPham().getTen());
        rs.setSale(isNew);
        rs.setAnh(ctsp.getAnh().getUrl());
        rs.setKichCo(ctsp.getKichCo().getTen());
        rs.setMauSac(ctsp.getMauSac().getTen());
        rs.setSoLuongTon(ctsp.getSoLuongTon());
        rs.setTimes(lichSuHoaDon.getThoiGian());
        rs.setMessage(lichSuHoaDon.getHanhDong());
        rs.setTongTien(hoaDon.getTongTien());
        rs.setThucThu(hoaDon.getThucThu());
        rs.setGiamGia(hoaDon.getGiamGia());
        System.out.println(rs);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @PostMapping("/huy-detail")
    public ResponseEntity<LichSuHoaDonDto> huyDonDetail(
            @RequestParam("id") Long idHoaDon,
            @RequestParam("lydo") String lydo,
            HttpServletRequest request
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idHoaDon).get();
        hoaDon.setTrangThai(TrangThaiHoaDon.Huy);
        hoaDon.setNgaySua(ConvertUtility.DateToTimestamp(new Date()));
        HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
        if (updatedHoaDon.getVoucher() != null) {
            Voucher voucher = voucherService.getByMa(updatedHoaDon.getVoucher().getMa());
            voucher.setSoLuong(voucher.getSoLuong() + 1);
            voucherService.save(voucher);
        }
        updatedHoaDon.getHoaDonChiTiets().forEach(hdct -> {
            ChiTietSanPham chiTietSanPham = chiTietSanPhamService.getById(hdct.getChiTietSanPham().getId());
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + hdct.getSoLuong());
            chiTietSanPhamService.save(chiTietSanPham);
        });
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(updatedHoaDon);
        lichSuHoaDon.setHanhDong("Lý Do: " + lydo);
        if (request.getUserPrincipal() != null) {
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername(request.getUserPrincipal().getName()));
        }
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(updatedHoaDon.getTrangThai().name());
        LichSuHoaDon lshd = lichSuHoaDonService.save(lichSuHoaDon);
        LichSuHoaDonDto lshdDto = new LichSuHoaDonDto();
        lshdDto.setIdHoaDon(lshd.getHoaDon().getId());
        lshdDto.setId(lshd.getId());
        lshdDto.setHanhDong(lshd.getHanhDong());
        lshdDto.setTrangThaiSauUpdate(lshd.getTrangThaiSauUpdate());
        lshdDto.setThoiGian(lshd.getThoiGian());
        if (lshd.getNguoiThucHien() != null) {
            lshdDto.setNguoiThucHien(lshd.getNguoiThucHien().getNhanVien() != null ? lshd.getNguoiThucHien().getNhanVien().getHoTen() : "Anonymous");
        } else {
            lshdDto.setNguoiThucHien("Anoynomous");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lshdDto);
    }

    @PostMapping("/hoan-tac-detail")
    public ResponseEntity<LichSuHoaDonDto> hoanTacDetail(
            @RequestBody Long idHoaDon,
            HttpServletRequest request
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idHoaDon).get();
        hoaDon.setTrangThai(TrangThaiHoaDon.ChoXacNhan);
        hoaDon.setNgaySua(ConvertUtility.DateToTimestamp(new Date()));
        HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(updatedHoaDon);
        lichSuHoaDon.setHanhDong("Hoàn tác trạng thái về: " + updatedHoaDon.getTrangThai().name());
        if (request.getUserPrincipal() != null) {
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername(request.getUserPrincipal().getName()));
        }
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(updatedHoaDon.getTrangThai().name());
        LichSuHoaDon lshd = lichSuHoaDonService.save(lichSuHoaDon);
        LichSuHoaDonDto lshdDto = new LichSuHoaDonDto();
        lshdDto.setId(lshd.getId());
        lshdDto.setIdHoaDon(lshd.getHoaDon().getId());
        lshdDto.setHanhDong(lshd.getHanhDong());
        lshdDto.setThoiGian(lshd.getThoiGian());
        if (lshd.getNguoiThucHien() != null) {
            lshdDto.setNguoiThucHien(lshd.getNguoiThucHien().getNhanVien() != null ? lshd.getNguoiThucHien().getNhanVien().getHoTen() : "Anonymous");
        } else {
            lshdDto.setNguoiThucHien("Anoynomous");
        }
        lshdDto.setTrangThaiSauUpdate(lshd.getTrangThaiSauUpdate());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lshdDto);
    }

    @GetMapping("/{id}/lich-su")
    public ResponseEntity<List<LichSuHoaDonDto>> lichSuHoaDon(@PathVariable Long id) {
        List<LichSuHoaDon> lichSuHoaDonList = lichSuHoaDonService.getAllLichSuHoaDonByIdHoaDon(id);
        List<LichSuHoaDonDto> lichSuHoaDonDtoList =
                lichSuHoaDonList
                        .stream()
                        .map(lshd -> new LichSuHoaDonDto(
                                lshd.getId(), lshd.getHoaDon().getId(), lshd.getHanhDong(),
                                lshd.getThoiGian(), lshd.getNguoiThucHien().getNhanVien().getHoTen(),
                                lshd.getTrangThaiSauUpdate()
                        )).collect(Collectors.toList());
        return new ResponseEntity<>(lichSuHoaDonDtoList, HttpStatus.OK);
    }

    @GetMapping("/printOrder")
    public ResponseEntity<String> callApiGHN(
            @RequestParam("token") String token
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551");
        HttpEntity entity = new HttpEntity<>(headers);
        String apiUrl = "https://dev-online-gateway.ghn.vn/a5/public-api/printA5?token=" + token;

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

//    @GetMapping("/getWard")
//    public ResponseEntity callWardGHN(
//            @RequestParam("district_id") int districtID
//    ) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Token", "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551");
//        HttpEntity entity = new HttpEntity<>(headers);
//        String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtID;
//
//        String stringResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class).getBody();
//        JsonObject jsonObject = JsonParser.parseString(stringResponse).getAsJsonObject();
//        if(!jsonObject.get("data").isJsonNull()) {
//            JsonArray arrayData = jsonObject.getAsJsonArray("data");
//            if (arrayData != null) {
//                for (JsonElement e : arrayData) {
//                    JsonObject ward = new JsonObject();
//                    ward.addProperty("Code", e.getAsJsonObject().get("WardCode").getAsString());
//                    ward.addProperty("Name", e.getAsJsonObject().get("WardName").getAsString());
//                    ward.addProperty("DistrictID", e.getAsJsonObject().get("DistrictID").getAsInt());
//                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("F:/lightbee/beeshoes/src/main/resources/static/assets/address-json/ward.json", true))) {
//                        writer.write(ward + ",");
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        }
//        return new ResponseEntity<>(stringResponse, HttpStatus.OK);
//    }
}
