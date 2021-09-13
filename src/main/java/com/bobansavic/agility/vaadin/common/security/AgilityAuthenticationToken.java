package com.bobansavic.agility.vaadin.common.security;

import com.bobansavic.agility.model.LoginData;
import com.bobansavic.agility.model.User;
import com.google.common.collect.Lists;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class AgilityAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private boolean admin;
    private boolean projectManager;
    private boolean projectView;
    private String currentProjectTitle;

    public AgilityAuthenticationToken(LoginData loginData) {
        super(loginData.getUsername(), loginData.getPassword());
        username = loginData.getUsername();
        password = loginData.getPassword();
    }

    public AgilityAuthenticationToken(AgilityAuthenticationToken token, List<GrantedAuthority> authorities) {
        super(token.getPrincipal(), token.getCredentials(), authorities);
        this.username = token.getUsername();
        this.password = token.getPassword();
    }

    public AgilityAuthenticationToken(AgilityAuthenticationToken token, GrantedAuthority authority) {
        this(token, Lists.newArrayList(authority));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isProjectManager() {
        return projectManager;
    }

    public void setProjectManager(boolean projectManager) {
        this.projectManager = projectManager;
    }

    public boolean isProjectView() {
        return projectView;
    }

    public void setProjectView(boolean projectView) {
        this.projectView = projectView;
    }

    public String getCurrentProjectTitle() {
        return currentProjectTitle;
    }

    public void setCurrentProjectTitle(String currentProjectTitle) {
        this.currentProjectTitle = currentProjectTitle;
    }

    public User getUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }
}
