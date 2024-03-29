package com.bobansavic.agility.vaadin.common.security;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.model.UserRoleType;
import com.bobansavic.agility.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

public class AgilityAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AgilityAuthenticationToken token = (AgilityAuthenticationToken) authentication;
        User user = null;
        user = userService.findByUsername(token.getUsername());
        if (user == null) {
            user = userService.findByEmail(token.getUsername());
        }

        if (user != null && token.getPassword().equals(user.getPassword())) {
            String role = user.getRole().getName();
            AgilityAuthenticationToken authenticationToken = new AgilityAuthenticationToken(token, new SimpleGrantedAuthority(role));
            authenticationToken.setUserId(user.getId());
            authenticationToken.setUsername(user.getUsername());
            authenticationToken.setEmail(user.getEmail());
            if (role.equals(UserRoleType.ADMIN.getValue())) {
                authenticationToken.setAdmin(true);
            } else if (role.equals(UserRoleType.PM.getValue())) {
                authenticationToken.setProjectManager(true);
            }
            return authenticationToken;
        } else {
            throw new BadCredentialsException("Wrong username and/or password.");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AgilityAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
