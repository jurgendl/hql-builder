package org.tools.hqlbuilder.webservice.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomSecurityContextPersistenceFilter extends SecurityContextPersistenceFilter {
    public CustomSecurityContextPersistenceFilter(SecurityContextRepository repo) {
        super(repo);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }

    @Override
    public void setForceEagerSessionCreation(boolean forceEagerSessionCreation) {
        super.setForceEagerSessionCreation(forceEagerSessionCreation);
    }
}
