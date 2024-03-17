package com.poly.BeeShoes.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.poly.BeeShoes.dto.LichSuHoaDonDto;
import com.poly.BeeShoes.dto.WardDto;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.*;
import com.poly.BeeShoes.request.AddProductOderRequest;
import com.poly.BeeShoes.request.UpdateProductRquest;
import com.poly.BeeShoes.request.chiTietSanPhamApiRquest;
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
    private final MailUtility mailUtility;
    Gson gson = new Gson();

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
            String body = "<h1>Bạn đã thực hiện xóa sản phẩm '"+hdct.getChiTietSanPham().getMaSanPham()+"' thành công!";
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
            String body = "<h1>Bạn đã thực hiện xóa sản phẩm '"+hdct.getChiTietSanPham().getMaSanPham()+"' vào hóa đơn!";
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
