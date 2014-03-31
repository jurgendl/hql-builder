package org.tools.hqlbuilder.resteasy;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.tools.hqlbuilder.service.HqlWebService;

/**
 * @see http://docs.jboss.org/resteasy/docs/2.3.7.Final/userguide/html/
 * @see http://blog.comsysto.com/2012/08/02/resteasy-integration-with-spring-tutorial-part-1-introduction/
 */
@Component
public class PojoResourceImpl implements PojoResource {
    @Resource
    private HqlWebService hqlWebService;

    /**
     * @see org.tools.hqlbuilder.resteasy.PojoResource#ping()
     */
    @Override
    public String ping() {
        try {
            return "hello from Rest-Easy, service " + hqlWebService.getProject() + " on " + hqlWebService.getConnectionInfo() + " is available";
        } catch (Exception ex) {
            return "hello from Rest-Easy, service is not available: " + ex;
        }
    }
}
