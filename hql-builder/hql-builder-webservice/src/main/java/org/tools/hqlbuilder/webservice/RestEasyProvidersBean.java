package org.tools.hqlbuilder.webservice;

import java.util.List;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestEasyProvidersBean {
    private List<Object> providers;

    public void setProviders(List<Object> providers) {
        this.providers = providers;
    }

    public void register() {
        for (Object provider : providers) {
            ResteasyProviderFactory.getInstance().registerProviderInstance(provider);
        }
    }
}