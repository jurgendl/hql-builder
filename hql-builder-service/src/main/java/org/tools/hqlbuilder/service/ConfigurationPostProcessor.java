package org.tools.hqlbuilder.service;

import org.hibernate.cfg.Configuration;

public interface ConfigurationPostProcessor {
    void postProcessConfiguration(Configuration config);
}
