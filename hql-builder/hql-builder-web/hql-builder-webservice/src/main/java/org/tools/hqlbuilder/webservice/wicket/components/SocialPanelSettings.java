package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

public class SocialPanelSettings implements Serializable {
    private static final long serialVersionUID = 1091881462422787495L;

    private boolean barForm = false;

    private boolean fading = false;

    public boolean isBarForm() {
        return this.barForm;
    }

    public boolean isFading() {
        return this.fading;
    }

    public SocialPanelSettings setBarForm(boolean barForm) {
        this.barForm = barForm;
        return this;
    }

    public SocialPanelSettings setFading(boolean fading) {
        this.fading = fading;
        return this;
    }
}
