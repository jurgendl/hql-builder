package org.tools.hqlbuilder.webservice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.PortResolver;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CustomRequestCache extends HttpSessionRequestCache {
    @Override
    public HttpServletRequest getMatchingRequest(HttpServletRequest request, HttpServletResponse response) {
        return super.getMatchingRequest(request, response);
    }

    @Override
    public SavedRequest getRequest(HttpServletRequest request, HttpServletResponse response) {
        return super.getRequest(request, response);
    }

    @Override
    public void removeRequest(HttpServletRequest request, HttpServletResponse response) {
        super.removeRequest(request, response);
    }

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        super.saveRequest(request, response);
    }

    @Override
    public void setCreateSessionAllowed(boolean createSessionAllowed) {
        super.setCreateSessionAllowed(createSessionAllowed);
    }

    @Override
    public void setPortResolver(PortResolver portResolver) {
        super.setPortResolver(portResolver);
    }

    @Override
    public void setRequestMatcher(RequestMatcher requestMatcher) {
        super.setRequestMatcher(requestMatcher);
    }
}
