package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.TheLoai;
import com.poly.BeeShoes.repository.TheLoaiRepository;
import com.poly.BeeShoes.service.TheLoaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheLoaiServiceImpl implements TheLoaiService {
    private final TheLoaiRepository theLoaiRepository;
    @Override
    public TheLoai save(TheLoai theLoai) {
        return theLoaiRepository.save(theLoai);
    }

    @Override
    public TheLoai getById(Long id) {
        return theLoaiRepository.findById(id).get();
    }

    @Override
    public boolean existsByTen(String ten) {
        return theLoaiRepository.existsByTen(ten);
    }


    @Override
    public List<TheLoai> getAll() {
        return theLoaiRepository.findAll(Sort.by(Sort.Direction.ASC, "ten"));
    }

    @Override
    public boolean delete(Long id) {
        TheLoai theLoai = theLoaiRepository.findById(id).get();
        if (theLoai.getId()!=null){
            theLoaiRepository.deleteById(theLoai.getId());
            return true;
        }
        return false;
    }
}
