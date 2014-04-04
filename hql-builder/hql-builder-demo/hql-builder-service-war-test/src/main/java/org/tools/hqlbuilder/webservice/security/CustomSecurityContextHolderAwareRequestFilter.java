package org.tools.hqlbuilder.webservice.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

public class CustomSecurityContextHolderAwareRequestFilter extends SecurityContextHolderAwareRequestFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }

    @Override
    public void setRolePrefix(String rolePrefix) {
        super.setRolePrefix(rolePrefix);
    }
}
