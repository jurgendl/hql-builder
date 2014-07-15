package org.tools.hqlbuilder.webservice.wicket.zuss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.StreamResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.zkoss.zuss.Resolver;
import org.zkoss.zuss.Zuss;
import org.zkoss.zuss.impl.out.BuiltinResolver;
import org.zkoss.zuss.metainfo.ZussDefinition;

public class ZussResourceReference extends StreamResourceReference implements IResourceStream, Resolver {
    private static final long serialVersionUID = 6384603768717480808L;

    protected static final Logger logger = LoggerFactory.getLogger(ZussResourceReference.class);

    protected final String charset = "utf-8";

    protected final String contentType = "text/css";

    protected transient Bytes length = null;

    protected transient ZussStyle zussStyle = null;

    protected transient String css = "";

    protected transient Time lastModified = Time.millis(0l);

    protected transient Resolver resolver = null;

    public ZussResourceReference(Class<?> scope, String name) {
        super(scope, name);
    }

    @Override
    public PackageResource getResource() {
        return super.getResource();
    }

    @Override
    public IResourceStream getResourceStream() {
        return this;
    }

    @Override
    public Time lastModifiedTime() {
        logger.info(getZussName() + " - sending lastModifiedTime: " + lastModified);
        return lastModified;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public Bytes length() {
        return length;
    }

    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            if (css == null || getZussStyle().getLastModified() == null || lastModifiedTime() == null
                    || lastModifiedTime().getMilliseconds() < getZussStyle().getLastModified()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    write(out);
                } catch (IOException ex) {
                    throw new ResourceStreamNotFoundException(ex);
                }
                length = Bytes.bytes(out.size());
                lastModified = Time.now();
                css = new String(out.toByteArray(), charset);
                logger.info(getZussName() + " - building");
            }
            return new ByteArrayInputStream(css.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            throw new ResourceStreamNotFoundException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        //
    }

    @Override
    public void setLocale(Locale locale) {
        //
    }

    @Override
    public void setStyle(String style) {
        //
    }

    @Override
    public void setVariation(String variation) {
        //
    }

    protected String getZussName() {
        return getName().replaceAll("\\.css", ".zuss");
    }

    protected String getResourcePath() {
        return getScope().getPackage().getName().replace('.', '/');
    }

    protected InputStream read() throws IOException {
        return getClass().getClassLoader().getResourceAsStream(getResourcePath() + '/' + getZussName());
    }

    protected void write(OutputStream out) throws IOException {
        ZussDefinition parsed = Zuss.parse(read(), charset, null, getZussName());
        Zuss.translate(parsed, out, charset, getResolver());
    }

    @Override
    public Object getVariable(String name) {
        String value = getZussStyle().getStyling().get("@" + name);
        if (value == null) {
            return null;
        }
        value = value.trim();
        return value.substring(0, value.length() - 1);
    }

    @Override
    public Method getMethod(String name) {
        return null;
    }

    protected ZussStyle getZussStyle() {
        if (zussStyle == null) {
            zussStyle = WicketApplication.get().getZussStyle();
        }
        return this.zussStyle;
    }

    protected void setZussStyle(ZussStyle zussStyle) {
        this.zussStyle = zussStyle;
    }

    protected Resolver getResolver() {
        if (resolver == null) {
            resolver = new BuiltinResolver() {
                @Override
                public Object getVariable(String name) {
                    Object variable = ZussResourceReference.this.getVariable(name);
                    if (variable == null) {
                        variable = super.getVariable(name);
                    }
                    return variable;
                }

                @Override
                public Method getMethod(String name) {
                    Method method = ZussResourceReference.this.getMethod(name);
                    if (method == null) {
                        method = super.getMethod(name);
                    }
                    return method;
                };
            };
        }
        return this.resolver;
    }

    protected void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }
}
