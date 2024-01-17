package com.poly.BeeShoes.service.impl;

import com.poly.BeeShoes.model.User;
import com.poly.BeeShoes.repository.UserRepository;
import com.poly.BeeShoes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("username does not exist"));
    }

    @Override
    public boolean existByUsername(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User createNewUser(User user) {
        return userRepository.save(user);
    }

}
