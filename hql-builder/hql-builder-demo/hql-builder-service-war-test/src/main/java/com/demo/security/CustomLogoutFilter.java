package com.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutFilter extends LogoutFilter {

    public CustomLogoutFilter(String logoutSuccessUrl, LogoutHandler[] handlers) {
        super(logoutSuccessUrl, handlers);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

        super.doFilter(arg0, arg1, arg2);
    }

    @Override
    protected String getFilterProcessesUrl() {

        return super.getFilterProcessesUrl();
    }

    @Override
    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {

        return super.requiresLogout(request, response);
    }

    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {

        super.setFilterProcessesUrl(filterProcessesUrl);
    }

}
