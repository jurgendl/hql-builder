package org.tools.hqlbuilder.webservice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class CustomSecurityContextRepository extends HttpSessionSecurityContextRepository {
    @Override
    public boolean containsContext(HttpServletRequest request) {
        return super.containsContext(request);
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return super.loadContext(requestResponseHolder);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        super.saveContext(context, request, response);
    }

    @Override
    public void setAllowSessionCreation(boolean allowSessionCreation) {
        super.setAllowSessionCreation(allowSessionCreation);
    }

    @Override
    public void setDisableUrlRewriting(boolean disableUrlRewriting) {
        super.setDisableUrlRewriting(disableUrlRewriting);
    }

    @Override
    public void setSpringSecurityContextKey(String springSecurityContextKey) {
        super.setSpringSecurityContextKey(springSecurityContextKey);
    }

    @Override
    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        super.setTrustResolver(trustResolver);
    }
}
