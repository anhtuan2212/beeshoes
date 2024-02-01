package com.poly.BeeShoes.library;

import com.poly.BeeShoes.request.ProductDetailVersion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LibService {
    public static BigDecimal convertStringToBigDecimal(String str) {
        if (str.contains(".")) {
            str = str.replace(".", "");
        }
        if (str.contains("đ")) {
            str = str.replace("đ", "");
        }
        BigDecimal bd = new BigDecimal(str);
        return bd;
    }

    public static List<String> checkDataProduct(Long sanPham, Long theLoai, Long thuongHieu, Long chatLieu, Long deGiay, Long coGiay, Long muiGiay, String giaNhap, String giaGoc) {
        List<String> lst = new ArrayList<>();
        if (sanPham < 1) {
            lst.add("sanPham");
        } else if (theLoai < 1) {
            lst.add("theLoai");
        } else if (thuongHieu < 1) {
            lst.add("thuongHieu");
        } else if (chatLieu < 1) {
            lst.add("chatLieu");
        } else if (deGiay < 1) {
            lst.add("deGiay");
        } else if (coGiay < 1) {
            lst.add("coGiay");
        } else if (muiGiay < 1) {
            lst.add("muiGiay");
        } else if (giaGoc.isEmpty()) {
            lst.add("giaGoc");
        } else if (giaNhap.isEmpty()) {
            lst.add("giaNhap");
        } else {
            lst = null;
        }
        return lst;
    }

    public BigDecimal StringToBigDecimal(String str) {
        if (str.contains(".")) {
            str = str.replace(".", "");
        }
        if (str.contains("đ")) {
            str = str.replace("đ", "");
        }
        BigDecimal bd = new BigDecimal(str);
        return bd;
    }

    public static List<String> checkDataProductDetail(List<String> lst, List<ProductDetailVersion> productdetail, String[] array) {
        if (array==null){
            lst.add("Anh");
            return lst;
        }
        if (productdetail == null) {
            lst.add("Version");
            return lst;
        }
        for (ProductDetailVersion pr : productdetail) {
            String gia = pr.getGiaBan().replace(".", "");
            gia = gia.replace("đ", "");
            int gb = Integer.valueOf(gia);
            if (gb < 999) {
                lst.add("giaBan");
            } else if (pr.getMaMauSac().isEmpty()) {
                lst.add("mauSac");
            } else if (pr.getKichCo().isEmpty()) {
                lst.add("kichCo");
            } else if (pr.getSoLuong() < 0) {
                lst.add("soLuong");
            }

        }
        return lst;
    }
}
