package com.bobansavic.agility.service;

import com.bobansavic.agility.model.LoginData;
import com.bobansavic.agility.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);
    User findByEmail(String email);
    User findByUsername(String username);
    void save(User user);
    User saveAndReturn(User user);
    void login(LoginData loginData);
    boolean deleteUser(User user);
    List<User> getAllUsers();
}
