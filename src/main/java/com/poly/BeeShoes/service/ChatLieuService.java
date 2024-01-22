package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.ChatLieu;

import java.util.List;

public interface ChatLieuService {
    ChatLieu save(ChatLieu chatLieu);
    ChatLieu getById(Long id);
    List<ChatLieu> getAll();
    boolean delete(Long id);
}
