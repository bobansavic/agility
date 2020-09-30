package com.bobansavic.agility.service;

import com.bobansavic.agility.model.UserRole;

public interface UserRoleService {

    UserRole findByName(String name);
    void save(UserRole role);
}
