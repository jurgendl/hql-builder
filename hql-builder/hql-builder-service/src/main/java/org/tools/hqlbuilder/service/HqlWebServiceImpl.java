package org.tools.hqlbuilder.service;

import org.tools.hqlbuilder.common.DelegatingHqlService;
import org.tools.hqlbuilder.common.HqlService;

public class HqlWebServiceImpl extends DelegatingHqlService implements HqlWebService {
    private static final long serialVersionUID = -7297624992346972087L;

    private HqlService hqlService;

    public HqlWebServiceImpl() {
        super();
    }

    public HqlService getHqlService() {
        return this.hqlService;
    }

    public void setHqlService(HqlService hqlService) {
        this.hqlService = hqlService;
    }

    @Override
    public HqlService getDelegate() {
        return hqlService;
    }
}