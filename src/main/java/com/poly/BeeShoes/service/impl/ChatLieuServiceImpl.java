package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.ChatLieu;
import com.poly.BeeShoes.repository.ChatLieuRepository;
import com.poly.BeeShoes.service.ChatLieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ChatLieuServiceImpl implements ChatLieuService {
    private final ChatLieuRepository chatLieuRepository;
    @Override
    public ChatLieu save(ChatLieu chatLieu) {
        return chatLieuRepository.save(chatLieu);
    }

    @Override
    public ChatLieu getById(Long id) {
        return chatLieuRepository.findById(id).get();
    }

    @Override
    public List<ChatLieu> getAll() {
        return chatLieuRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        ChatLieu cl = chatLieuRepository.findById(id).get();
        if (cl.getId()!=null){
            chatLieuRepository.deleteById(cl.getId());
            return true;
        }
        return false;
    }
}