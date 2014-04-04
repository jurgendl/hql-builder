package org.tools.hqlbuilder.webservice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class CustomSecurityContextLogoutHandler extends SecurityContextLogoutHandler {
    @Override
    public boolean isInvalidateHttpSession() {
        return super.isInvalidateHttpSession();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
    }

    @Override
    public void setInvalidateHttpSession(boolean invalidateHttpSession) {
        super.setInvalidateHttpSession(invalidateHttpSession);
    }
}
