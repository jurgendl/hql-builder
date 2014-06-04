package org.tools.hqlbuilder.webservice.wicket;

import java.net.URL;
import java.util.Locale;

import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.resource.IResourceStream;
import org.tools.hqlbuilder.common.icons.WicketIconsRoot;

public class ResourceRef extends PackageResourceReference {
    private static final long serialVersionUID = -7397543245367865603L;

    private String path;

    public ResourceRef(Class<?> scope, String name, String path) {
        super(scope, name);
        this.path = path;
    }

    @Override
    public PackageResource getResource() {
        PackageResource resource = new PackageRes(getScope(), getName(), getLocale(), getStyle(), getVariation(), path);
        removeCompressFlagIfUnnecessary(resource);
        return resource;
    }

    public static class PackageRes extends PackageResource {
        private static final long serialVersionUID = 2571976283414015425L;

        private String path;

        protected PackageRes(Class<?> scope, String name, Locale locale, String style, String variation, String path) {
            super(scope, name, locale, style, variation);
            this.path = path;
        }

        @Override
        public IResourceStream getResourceStream() {
            URL fullpath = WicketIconsRoot.class.getClassLoader().getResource(
                    WicketIconsRoot.class.getPackage().getName().replace('.', '/') + "/" + path);
            return new UrlResourceStream(fullpath);
        }
    }
}
