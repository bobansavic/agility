package com.bobansavic.agility.web.common.security;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.model.UserRole;
import com.bobansavic.agility.model.UserRoleType;
import com.bobansavic.agility.service.UserRoleService;
import com.bobansavic.agility.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
@Transactional
public class AgilityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByUsername(s);
        if (user == null) {
            user = userService.findByEmail(s); {
                if (user == null) {
                    return new org.springframework.security.core.userdetails.User(
                            " ",
                            " ",
                            true,
                            true,
                            true,
                            true,
                            setDefaultAuthority());
                }
            }
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                setAuthorities(user.getRole()));
    }

    private Set<GrantedAuthority> setAuthorities(UserRole role) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (role.getName().equals(UserRoleType.ADMIN.name())) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            return authorities;
        } else {
            return setDefaultAuthority();
        }
    }

    private Set<GrantedAuthority> setDefaultAuthority() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(UserRoleType.USER.getValue()));
        return authorities;
    }
}
