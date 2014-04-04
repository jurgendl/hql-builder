package com.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class CustomAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {
    @Override
    protected boolean applyAnonymousForThisRequest(HttpServletRequest request) {

        return super.applyAnonymousForThisRequest(request);
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {

        Authentication a = super.createAuthentication(request);
        return super.createAuthentication(request);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        super.doFilter(req, res, chain);
    }

    @Override
    public void setAuthenticationDetailsSource(AuthenticationDetailsSource authenticationDetailsSource) {

        super.setAuthenticationDetailsSource(authenticationDetailsSource);
    }
}
