package com.bobansavic.agility.service;

import com.bobansavic.agility.model.LoginData;
import com.bobansavic.agility.model.User;
import com.bobansavic.agility.repository.UserRepository;
import com.bobansavic.agility.web.common.security.AgilityAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImplementation implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User saveAndReturn(User user) {
        return userRepository.save(user);
    }

    @Override
    public void login(LoginData loginData) {
        AgilityAuthenticationToken authenticationToken = new AgilityAuthenticationToken(loginData);
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            log.error("Could not delete user: {0}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
