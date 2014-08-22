package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.model.Model;

public class NullOption<T extends Serializable> extends SelectOption<T> {
    private static final long serialVersionUID = 3403464402860779603L;

    public NullOption() {
        super("nullOption", Model.of((T) null));
    }
}
