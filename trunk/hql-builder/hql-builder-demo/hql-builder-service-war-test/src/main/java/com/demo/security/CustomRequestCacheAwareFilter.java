package com.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

public class CustomRequestCacheAwareFilter extends RequestCacheAwareFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        super.doFilter(request, response, chain);
    }

    @Override
    public void setRequestCache(RequestCache requestCache) {

        super.setRequestCache(requestCache);
    }

}
