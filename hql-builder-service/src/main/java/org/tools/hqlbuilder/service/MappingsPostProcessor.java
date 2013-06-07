package org.tools.hqlbuilder.service;

import org.hibernate.cfg.Configuration;

public interface MappingsPostProcessor {
    void postProcessMappings(Configuration config);
}
