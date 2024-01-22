package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.Anh;
import com.poly.BeeShoes.model.SanPham;
import com.poly.BeeShoes.repository.AnhRepository;
import com.poly.BeeShoes.service.AnhService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnhServiceImpl implements AnhService {
    private final AnhRepository anhRepository;

    @Override
    public Anh save(Anh anh) {
        return anhRepository.save(anh);
    }

    @Override
    public boolean saveAnhSanPham(SanPham sanPham, String[] lst) {
        int size = sanPham.getAnh().size();
        List<Anh> imagesToRemove = new ArrayList<>();
        if (size >= 1 && size <= 4) {
            for (int i = 0; i < size; i++) {
                Anh a = sanPham.getAnh().get(i);
                a.setUrl(lst[i]);
                if (i == 0) {
                    a.setMain(true);
                }
                a.setNgaySua(Timestamp.from(Instant.now()));
                anhRepository.save(a);
            }
            if (size < 4 && lst.length == 4) {
                for (int i = size; i < 4; i++) {
                    Anh a = new Anh();
                    a.setUrl(lst[i]);
                    a.setSanPham(sanPham);
                    a.setNgayTao(Timestamp.from(Instant.now()));
                    a.setNgaySua(Timestamp.from(Instant.now()));
                    anhRepository.save(a);
                }
            }
        }
        if (size > lst.length) {
            for (int i = lst.length; i < size; i++) {
                Anh a = sanPham.getAnh().get(i);
                imagesToRemove.add(a);
            }
        }
        for (Anh imageToRemove : imagesToRemove) {
            sanPham.getAnh().remove(imageToRemove);
            anhRepository.delete(imageToRemove);
        }
        return true;
    }



    @Override
    public List<Anh> getAllBySanPham(SanPham sanPham) {
        return anhRepository.getAllBySanPham(sanPham);
    }

    @Override
    public boolean delete(Long id) {
        Anh a = anhRepository.findById(id).get();
        if (a.getId() != null) {
            anhRepository.deleteById(a.getId());
            return true;
        }
        return false;
    }
}
