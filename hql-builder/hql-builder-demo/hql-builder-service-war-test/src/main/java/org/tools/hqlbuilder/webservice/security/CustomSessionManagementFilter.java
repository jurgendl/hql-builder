package org.tools.hqlbuilder.webservice.security;

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
    }

    public CustomSessionManagementFilter(SecurityContextRepository securityContextRepository, SessionAuthenticationStrategy sessionStrategy) {
        super(securityContextRepository, sessionStrategy);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }
}
