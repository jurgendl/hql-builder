package org.tools.hqlbuilder.webservice.security;

import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAnonymousAuthenticationProvider extends AnonymousAuthenticationProvider {
    public CustomAnonymousAuthenticationProvider(String key) {
        super(key);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }
}
