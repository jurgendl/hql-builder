package org.tools.hqlbuilder.webcommon;

import java.util.List;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestEasyProvidersBean {
    private List<Object> providers;

    private ResteasyProviderFactory instance;

    public void setProviders(List<Object> providers) {
        this.providers = providers;
    }

    public void register() {
        for (Object provider : providers) {
            getInstance().registerProviderInstance(provider);
        }
    }

    public ResteasyProviderFactory getInstance() {
        if (instance == null) {
            instance = ResteasyProviderFactory.getInstance();
        }
        return this.instance;
    }

    public void setInstance(ResteasyProviderFactory instance) {
        this.instance = instance;
    }
}