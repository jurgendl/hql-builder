package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;

public class WicketInitializer implements IInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WicketInitializer.class);

    @Override
    public void init(Application application) {
        Thread t = new Thread((Runnable) () -> warmUp(), getClass().getSimpleName());
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void destroy(Application application) {
        //
    }

    protected void warmUp() {
        try {
            logger.info("warmup form css - start");
            FormPanel.FORM_CSS.getInputStream();
            logger.info("warmup form css - end");
        } catch (Exception ex) {
            logger.error("warmup form css - {}", ex);
        }
    }
}
