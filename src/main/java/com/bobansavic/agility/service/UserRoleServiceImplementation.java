package com.bobansavic.agility.service;

import com.bobansavic.agility.model.UserRole;
import com.bobansavic.agility.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImplementation implements UserRoleService {

    @Autowired
    UserRoleRepository roleRepository;

    @Override
    public UserRole findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void save(UserRole role) {
        roleRepository.save(role);
    }
}
