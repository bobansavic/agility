package com.bobansavic.agility.web.common.security;

import com.vaadin.server.VaadinSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * A custom {@link SecurityContextHolderStrategy} that stores the {@link SecurityContext} in the Vaadin Session.
 * @author see https://github.com/peholmst/SpringSecurityDemo
 */
public class VaadinSessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    @Override
    public void clearContext() {
        getSession().setAttribute(SecurityContext.class, null);
    }

    @Override
    public SecurityContext getContext() {
        VaadinSession session = getSession();
        SecurityContext context = session.getAttribute(SecurityContext.class);
        if (context == null) {
            context = createEmptyContext();
            session.setAttribute(SecurityContext.class, context);
        }
        return context;
    }

    @Override
    public void setContext(SecurityContext context) {
        getSession().setAttribute(SecurityContext.class, context);
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

    private static VaadinSession getSession() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            throw new IllegalStateException("No Vaadin Session bound to current thread");
        }
        return session;
    }
}
