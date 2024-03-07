package com.poly.BeeShoes.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.poly.BeeShoes.dto.LichSuHoaDonDto;
import com.poly.BeeShoes.dto.WardDto;
import com.poly.BeeShoes.library.LibService;
import com.poly.BeeShoes.model.HoaDon;
import com.poly.BeeShoes.model.LichSuHoaDon;
import com.poly.BeeShoes.model.TrangThaiHoaDon;
import com.poly.BeeShoes.service.HoaDonService;
import com.poly.BeeShoes.service.LichSuHoaDonService;
import com.poly.BeeShoes.service.UserService;
import com.poly.BeeShoes.utility.ConvertUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonRestController {

    private final HoaDonService hoaDonService;
    private final LichSuHoaDonService lichSuHoaDonService;
    private final UserService userService;
    private final RestTemplate restTemplate;
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
            @RequestBody Long id
    ) {
        System.out.println(id);
        HoaDon hoaDon = hoaDonService.getHoaDonById(id).get();
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
        lichSuHoaDon.setTrangThaiSauUpdate(updatedHoaDon.getTrangThai().name());
        LichSuHoaDon lshd = lichSuHoaDonService.save(lichSuHoaDon);
        LichSuHoaDonDto lshdDto = new LichSuHoaDonDto();
        lshdDto.setIdHoaDon(lshd.getHoaDon().getId());
        lshdDto.setId(lshd.getId());
        lshdDto.setHanhDong(lshd.getHanhDong());
        lshdDto.setTrangThaiSauUpdate(lshd.getTrangThaiSauUpdate());
        lshdDto.setThoiGian(lshd.getThoiGian());
        lshdDto.setNguoiThucHien(lshd.getNguoiThucHien().getNhanVien().getHoTen());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lshdDto);
    }

    @PostMapping("/huy-detail")
    public ResponseEntity<LichSuHoaDonDto> huyDonDetail(
            @RequestBody Long idHoaDon
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idHoaDon).get();
        hoaDon.setTrangThai(TrangThaiHoaDon.Huy);
        HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(updatedHoaDon);
        lichSuHoaDon.setHanhDong("Hủy hóa đơn");
        lichSuHoaDon.setNguoiThucHien(userService.getByUsername("tuyen"));
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(updatedHoaDon.getTrangThai().name());
        LichSuHoaDon lshd = lichSuHoaDonService.save(lichSuHoaDon);
        LichSuHoaDonDto lshdDto = new LichSuHoaDonDto();
        lshdDto.setIdHoaDon(lshd.getHoaDon().getId());
        lshdDto.setId(lshd.getId());
        lshdDto.setHanhDong(lshd.getHanhDong());
        lshdDto.setTrangThaiSauUpdate(lshd.getTrangThaiSauUpdate());
        lshdDto.setThoiGian(lshd.getThoiGian());
        lshdDto.setNguoiThucHien(lshd.getNguoiThucHien().getNhanVien().getHoTen());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lshdDto);
    }

    @PostMapping("/hoan-tac-detail")
    public ResponseEntity<LichSuHoaDonDto> hoanTacDetail(@RequestBody Long idHoaDon) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idHoaDon).get();
        hoaDon.setTrangThai(TrangThaiHoaDon.ChoXacNhan);
        HoaDon updatedHoaDon = hoaDonService.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(updatedHoaDon);
        lichSuHoaDon.setHanhDong("Hoàn tác trạng thái về: " + updatedHoaDon.getTrangThai().name());
        lichSuHoaDon.setNguoiThucHien(userService.getByUsername("tuyen"));
        lichSuHoaDon.setThoiGian(ConvertUtility.DateToTimestamp(new Date()));
        lichSuHoaDon.setTrangThaiSauUpdate(updatedHoaDon.getTrangThai().name());
        LichSuHoaDon lshd = lichSuHoaDonService.save(lichSuHoaDon);
        LichSuHoaDonDto lshdDto = new LichSuHoaDonDto();
        lshdDto.setId(lshd.getId());
        lshdDto.setIdHoaDon(lshd.getHoaDon().getId());
        lshdDto.setHanhDong(lshd.getHanhDong());
        lshdDto.setThoiGian(lshd.getThoiGian());
        lshdDto.setNguoiThucHien(lshd.getNguoiThucHien().getNhanVien().getHoTen());
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
