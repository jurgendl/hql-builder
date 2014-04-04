package com.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    protected RedirectStrategy getRedirectStrategy() {

        return super.getRedirectStrategy();
    }

    @Override
    protected boolean isAllowSessionCreation() {

        return super.isAllowSessionCreation();
    }

    @Override
    protected boolean isUseForward() {

        return super.isUseForward();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        super.onAuthenticationFailure(request, response, exception);
    }

    @Override
    public void setAllowSessionCreation(boolean allowSessionCreation) {

        super.setAllowSessionCreation(allowSessionCreation);
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {

        super.setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {

        super.setRedirectStrategy(redirectStrategy);
    }

    @Override
    public void setUseForward(boolean forwardToDestination) {

        super.setUseForward(forwardToDestination);
    }

}
