package com.bobansavic.agility.vaadin.common;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.vaadin.common.security.AgilityAuthenticationToken;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.server.HandlerHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public class SecurityUtils {

    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(HandlerHelper.RequestType.values())
                .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    public static boolean isLoggedIn() {
        Authentication authentication = getAuthentication();
        return (authentication != null && authentication.isAuthenticated());
    }

    public static User getCurrentUser() {
        return getToken().getUser();
    }

    public static boolean isAdmin() {
        return getToken().isAdmin();
    }

    public static boolean isProjectManager() {
        return getToken().isProjectManager();
    }

    public static boolean isProjectView() {
        return getToken().isProjectView();
    }

    public static void setProjectView(boolean value) {
        getToken().setProjectView(value);
    }

    public static String getCurrentProjectTitle() {
        return getToken().getCurrentProjectTitle();
    }

    public static void setCurrentProjectTitle(String title) {
        getToken().setCurrentProjectTitle(title);
    }

    public static AgilityAuthenticationToken getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AgilityAuthenticationToken) authentication;
    }
}
