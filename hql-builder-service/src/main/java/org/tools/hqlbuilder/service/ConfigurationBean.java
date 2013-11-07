package org.tools.hqlbuilder.service;

import org.hibernate.cfg.Configuration;

public class ConfigurationBean {
    protected Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
}
