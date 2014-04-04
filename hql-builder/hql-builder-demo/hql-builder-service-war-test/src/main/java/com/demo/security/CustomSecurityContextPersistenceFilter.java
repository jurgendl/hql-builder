package com.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomSecurityContextPersistenceFilter extends SecurityContextPersistenceFilter {

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

        super.doFilter(arg0, arg1, arg2);
    }

    @Override
    public void setForceEagerSessionCreation(boolean forceEagerSessionCreation) {

        super.setForceEagerSessionCreation(forceEagerSessionCreation);
    }

    @Override
    public void setSecurityContextRepository(SecurityContextRepository repo) {

        super.setSecurityContextRepository(repo);
    }

}
