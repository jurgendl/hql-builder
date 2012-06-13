package org.tools.hqlbuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.tools.hqlbuilder.common.HqlService;

public class HqlServiceClient {
    @Autowired
    private HqlService hqlService;

    public HqlService getHqlService() {
        return hqlService;
    }

    public void setHqlService(HqlService hqlService) {
        this.hqlService = hqlService;
    }
}
