package com.demo.security;

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
    public void logout(HttpServletRequest arg0, HttpServletResponse arg1, Authentication arg2) {

        super.logout(arg0, arg1, arg2);
    }

    @Override
    public void setInvalidateHttpSession(boolean invalidateHttpSession) {

        super.setInvalidateHttpSession(invalidateHttpSession);
    }

}
