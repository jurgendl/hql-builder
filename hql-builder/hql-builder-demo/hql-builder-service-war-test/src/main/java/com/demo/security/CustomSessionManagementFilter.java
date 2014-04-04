package com.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

public class CustomSessionManagementFilter extends SessionManagementFilter {

    public CustomSessionManagementFilter(SecurityContextRepository securityContextRepository) {
        super(securityContextRepository);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

        super.doFilter(arg0, arg1, arg2);
    }

    @Override
    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {

        super.setSessionAuthenticationStrategy(sessionStrategy);
    }

}
