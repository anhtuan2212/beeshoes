package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ChucVu;
import java.util.List;

public interface ChucVuService {
    List<ChucVu> getAll();
    void delete(Long id);
    ChucVu detail(Long id);
    ChucVu add(ChucVu chucVu);
    ChucVu update(ChucVu chucVu, Long id);
}
