package org.tools.hqlbuilder.client;

import org.tools.hqlbuilder.common.HqlService;

public interface HqlServiceClient extends HqlService {
    public abstract void setServiceUrl(String serviceUrl);

    public abstract String getServiceUrl();
}
