package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

public class LabeledLinkPanel extends Panel {
    public static final String LINK_ID = "link";

    public static final String LABEL_ID = "label";

    public static enum LinkType {
        page, submit;
    }

    private static final long serialVersionUID = -7352081661850450279L;

    public LabeledLinkPanel(String id, final IModel<String> labelModel, LinkType linkType, final Class<Page> page, PageParameters pageParameters) {
        this(id, labelModel, linkType, page, pageParameters, null);
    }

    public LabeledLinkPanel(String id, final IModel<String> labelModel, LinkType linkType, OnSubmit onSubmit) {
        this(id, labelModel, linkType, null, null, onSubmit);
    }

    public LabeledLinkPanel(String id, final IModel<String> labelModel, LinkType linkType, final Class<Page> page, PageParameters pageParameters,
            OnSubmit onSubmit) {
        super(id);
        switch (linkType) {
            case page:
                add(getBookmarkablePageLink(page, pageParameters, labelModel));
                break;
            case submit:
                add(getSubmitLink(onSubmit, labelModel));
                break;
        }
    }

    protected AbstractLink getSubmitLink(final OnSubmit onSubmit, IModel<String> labelModel) {
        SubmitLink submitLink = new SubmitLink(LINK_ID) {
            private static final long serialVersionUID = 5824948229357499707L;

            @Override
            public void onSubmit() {
                onSubmit.onSubmit();
            }
        };
        submitLink.add(new CssClassNameAppender(JQueryUI.jquibutton));
        Component labelFragment = getLabel(labelModel);
        submitLink.add(labelFragment);
        return submitLink;
    }

    protected AbstractLink getBookmarkablePageLink(final Class<Page> page, final PageParameters pageParameters, final IModel<String> labelModel) {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        BookmarkablePageLink bookmarkablePageLink = new BookmarkablePageLink(LINK_ID, page, pageParameters);
        Component labelFragment = getLabel(labelModel);
        bookmarkablePageLink.add(labelFragment);
        return bookmarkablePageLink;
    }

    protected Component getLabel(final IModel<String> labelModel) {
        return new Label(LABEL_ID, labelModel);
    }

    public static interface OnSubmit extends Serializable {
        public void onSubmit();
    }
}