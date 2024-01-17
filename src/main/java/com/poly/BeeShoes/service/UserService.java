package com.poly.BeeShoes.service;

import com.poly.BeeShoes.model.User;

public interface UserService {

    User getByUsername(String email);

    boolean existByUsername(String email);

    User createNewUser(User user);

}
