package org.tools.hqlbuilder.service;

import org.tools.hqlbuilder.common.DelegatingHqlService;
import org.tools.hqlbuilder.common.HqlService;

public class HqlWebServiceImpl extends DelegatingHqlService implements HqlWebService {
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

    /**
     * @see org.tools.hqlbuilder.common.DelegatingHqlService#getDelegate()
     */
    @Override
    public HqlService getDelegate() {
        return hqlService;
    }
}