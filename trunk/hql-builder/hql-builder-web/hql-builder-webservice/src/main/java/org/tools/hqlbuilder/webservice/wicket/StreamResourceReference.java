package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.resource.IResourceStream;

public abstract class StreamResourceReference extends PackageResourceReference {
    private static final long serialVersionUID = 2445853607747904603L;

    public StreamResourceReference(Class<?> scope, String name) {
        super(scope, name);
    }

    @Override
    public PackageResource getResource() {
        PackageResource resource = new PackageResource(getScope(), getName(), getLocale(), getStyle(), getVariation()) {
            private static final long serialVersionUID = -1135314184370523460L;

            @Override
            public IResourceStream getResourceStream() {
                return StreamResourceReference.this.getResourceStream();
            }
        };
        removeCompressFlagIfUnnecessary(resource);
        return resource;
    }

    public abstract IResourceStream getResourceStream();
}
