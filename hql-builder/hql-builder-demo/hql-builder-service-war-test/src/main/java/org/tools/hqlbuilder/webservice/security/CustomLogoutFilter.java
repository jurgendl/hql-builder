package org.tools.hqlbuilder.webservice.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutFilter extends LogoutFilter {
    public CustomLogoutFilter(String logoutSuccessUrl, LogoutHandler[] handlers) {
        super(logoutSuccessUrl, handlers);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        super.doFilter(request, response, filterChain);
    }

    @Override
    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresLogout(request, response);
    }
}
