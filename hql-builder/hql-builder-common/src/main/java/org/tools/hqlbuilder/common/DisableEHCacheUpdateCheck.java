package org.tools.hqlbuilder.common;

public class DisableEHCacheUpdateCheck {
    static {
        System.setProperty("net.sf.ehcache.skipUpdateCheck", "false");
    }

    public DisableEHCacheUpdateCheck() {
        super();
    }
}
