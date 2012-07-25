package org.tools.hqlbuilder.client;

import org.tools.hqlbuilder.common.DelegatingHqlService;
import org.tools.hqlbuilder.common.HqlService;

public class HqlServiceClientImpl extends DelegatingHqlService implements HqlServiceClient {
    private static final long serialVersionUID = 5659734722245348813L;

    private HqlService hqlService;

    private String serviceUrl;

    public HqlService getHqlService() {
        return hqlService;
    }

    public void setHqlService(HqlService hqlService) {
        this.hqlService = hqlService;
    }

    @Override
    public String getServiceUrl() {
        return this.serviceUrl;
    }

    @Override
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public HqlService getDelegate() {
        return hqlService;
    }
}
