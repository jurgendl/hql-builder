package org.tools.hqlbuilder.webservice.wicket;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

public class FileStreamResourceReference extends StreamResourceReference {
    private static final long serialVersionUID = -9033881031408124344L;

    protected transient Path file;

    protected transient InputStream in;

    public FileStreamResourceReference(Class<?> scope, String name, Path file) {
        super(scope, name);
        this.file = file;
    }

    @Override
    public IResourceStream getResourceStream() {
        return new AbstractResourceStream() {
            private static final long serialVersionUID = 8784023512223151193L;

            @Override
            public InputStream getInputStream() throws ResourceStreamNotFoundException {
                return getInputstream();
            }

            @Override
            public void close() throws IOException {
                if (in != null)
                    in.close();
            }
        };
    }

    public InputStream getInputstream() throws ResourceStreamNotFoundException {
        if (in == null)
            try {
                in = Files.newInputStream(file);
            } catch (IOException e) {
                throw new ResourceStreamNotFoundException(e);
            }
        return in;
    }
}
