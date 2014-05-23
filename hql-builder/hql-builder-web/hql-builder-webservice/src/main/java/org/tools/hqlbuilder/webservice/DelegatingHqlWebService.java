package org.tools.hqlbuilder.webservice;

import org.tools.hqlbuilder.common.DelegatingHqlService;
import org.tools.hqlbuilder.common.HqlService;

public class DelegatingHqlWebService extends DelegatingHqlService implements HqlWebService {
    protected HqlWebService delegate;

    public DelegatingHqlWebService() {
        super();
    }

    public DelegatingHqlWebService(HqlWebService delegate) {
        this.delegate = delegate;
    }

    @Override
    public HqlService getDelegate() {
        return delegate;
    }

    public void setDelegate(HqlWebService delegate) {
        this.delegate = delegate;
    }
}
