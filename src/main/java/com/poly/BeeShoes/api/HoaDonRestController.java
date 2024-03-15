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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
                hoaDon.setTrangThai(TrangThaiHoaDon.ChoGiao);
            } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoGiao) {
                hoaDon.setTrangThai(TrangThaiHoaDon.DangGiao);
            } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.DangGiao) {
                hoaDon.setTrangThai(TrangThaiHoaDon.ThanhCong);
            }
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
        System.out.println(idHoaDon);
        HoaDon hoaDon = hoaDonService.getHoaDonById(idHoaDon).get();
        String trangThaiBeforeUpdate = hoaDon.getTrangThai().name();
        if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoXacNhan) {
            hoaDon.setTrangThai(TrangThaiHoaDon.ChoGiao);
        } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.ChoGiao) {
            hoaDon.setTrangThai(TrangThaiHoaDon.DangGiao);
        } else if (hoaDon.getTrangThai() == TrangThaiHoaDon.DangGiao) {
            hoaDon.setTrangThai(TrangThaiHoaDon.ThanhCong);
        }
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

    @PostMapping("/add-product")
    public ResponseEntity themsanPham(@ModelAttribute AddProductOderRequest dt, HttpServletRequest request
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(dt.getId()).orElse(null);
        if (hoaDon == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "HoaDonNull").build();
        }
        System.out.println("Hoá Đơn Null");
        MauSac mauSac = mauSacService.getMauSacByMa(dt.getMauSac());
        KichCo kichCo = kichCoService.getById(dt.getKichCo());
        SanPham sanPham = sanPhamService.getById(dt.getSanPham());
        ChiTietSanPham ctsp = chiTietSanPhamService.getBySizeAndColorAndProduct(kichCo, mauSac, sanPham);
        if (ctsp == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("status", "CTSPNull").build();
        }
        System.out.println("CTSP Null");
        List<HoaDonChiTiet> lst = hoaDon.getHoaDonChiTiets();
        List<HoaDonChiTiet> lstNew = lst.stream()
                .filter(hoaDonChiTiet -> hoaDonChiTiet.getChiTietSanPham().getId().equals(ctsp.getId()))
                .collect(Collectors.toList());
        HoaDonChiTiet hdct = null;
        for (HoaDonChiTiet hoaDonChiTiet : lstNew) {
            if (hoaDonChiTiet.getChiTietSanPham().getGiaBan().equals(ctsp.getGiaBan())) {
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
        hdct = hoaDonChiTietService.save(hdct);
        lst.add(hdct);
        hoaDon.setHoaDonChiTiets(lst);
        hoaDon = hoaDonService.save(hoaDon);
        BigDecimal total = BigDecimal.ZERO;
        for (HoaDonChiTiet hdctt : hoaDon.getHoaDonChiTiets()) {
            total = total.add(hdctt.getChiTietSanPham().getGiaBan());
        }
        hoaDon.setTongTien(total);
        BigDecimal thucThu = BigDecimal.ZERO;
        if (hoaDon.getGiamGia() != null) {
            thucThu = total.subtract(hoaDon.getGiamGia());
        } else {
            thucThu = total;
        }
        hoaDon.setThucThu(thucThu);
        hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setHanhDong("Thêm sản phẩm '" + ctsp.getMaSanPham() + "' vào hóa đơn.");
        if (request.getUserPrincipal() != null) {
            lichSuHoaDon.setNguoiThucHien(userService.getByUsername(request.getUserPrincipal().getName()));
        }
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(hoaDon.getTrangThai().name());
        lichSuHoaDon = lichSuHoaDonService.save(lichSuHoaDon);
        UpdateProductRquest rs = new UpdateProductRquest();
        rs.setSoLuong(hdct.getSoLuong());
        rs.setId(ctsp.getId());
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
