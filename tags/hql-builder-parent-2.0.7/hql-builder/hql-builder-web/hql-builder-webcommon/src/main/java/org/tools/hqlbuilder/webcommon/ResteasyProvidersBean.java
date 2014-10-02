package org.tools.hqlbuilder.webcommon;

import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class ResteasyProvidersBean {
    private List<?> providers;

    private ResteasyProviderFactory instance;

    public ResteasyProvidersBean(Object... providers) {
        this.providers = Arrays.asList(providers);
    }

    public ResteasyProvidersBean() {
        super();
    }

    public void setProviders(List<?> providers) {
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