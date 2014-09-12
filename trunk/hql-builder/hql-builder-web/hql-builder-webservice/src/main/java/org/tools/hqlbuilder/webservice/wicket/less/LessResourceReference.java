package org.tools.hqlbuilder.webservice.wicket.less;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.StreamResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import ro.isdc.wro.extensions.processor.css.LessCssProcessor;

import com.google.common.collect.Lists;

/**
 * response.render(CssHeaderItem.forReference(new lessResourceReference(WicketCSSRoot.class, "table.less")));
 *
 * @see http://less-lang.com/
 * @see https://code.google.com/p/wro4j/
 */
public class LessResourceReference extends StreamResourceReference implements IResourceStream {
    private static final long serialVersionUID = 4347055808647080465L;

    protected static final Logger logger = LoggerFactory.getLogger(LessResourceReference.class);

    protected final String charset = "utf-8";

    protected final String contentType = "text/css";

    protected transient Bytes length = null;

    protected transient String css = "";

    protected transient Time lastModified = null;

    protected transient LessCssProcessor lessCssProcessor;

    public LessResourceReference(Class<?> scope, String name) {
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
        if (lastModified == null) {
            try {
                getInputStream();
            } catch (ResourceStreamNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        logger.info(getLessName() + " - sending lastModifiedTime: " + lastModified);
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
            boolean rebuild = false;
            String fullPath = getResourcePath() + '/' + getLessName();
            if (css == null) {
                logger.info("building " + fullPath + " because is new");
                rebuild = true;
            } else if (lastModified == null) {
                logger.info("building " + fullPath + " because out of date");
                rebuild = true;
                // } else if (getlessStyle().getLastModified() == null) {
                // logger.info("building " + fullPath + " because style is new");
                // rebuild = true;
                // } else if (lastModified.getMilliseconds() < getlessStyle().getLastModified()) {
                // logger.info("building " + fullPath + " because style out of date");
                // rebuild = true;
            } else if (WicketApplication.get().usesDevelopmentConfig()) {
                try {
                    if (lastModified.getMilliseconds() < getClass().getClassLoader().getResource(fullPath).openConnection().getLastModified()) {
                        logger.info("building " + fullPath + " because template out of date");
                        rebuild = true;
                    }
                } catch (IOException ex1) {
                    logger.info("building " + fullPath + " because of exception: " + ex1);
                    rebuild = true;
                }
            }
            if (rebuild) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    write(out);
                } catch (IOException ex) {
                    throw new ResourceStreamNotFoundException(ex);
                }
                length = Bytes.bytes(out.size());
                lastModified = Time.now();
                css = new String(out.toByteArray(), charset);
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

    protected String getLessName() {
        return getName().replaceAll("\\.css", ".less");
    }

    protected String getResourcePath() {
        return getScope().getPackage().getName().replace('.', '/');
    }

    protected InputStream read() throws IOException {
        return getClass().getClassLoader().getResourceAsStream(getResourcePath() + '/' + getLessName());
    }

    protected void write(OutputStream out) throws IOException {
        getLessCssProcessor().process(new InputStreamReader(read()), new OutputStreamWriter(out));
    }

    public LessCssProcessor getLessCssProcessor() {
        if (lessCssProcessor == null) {
            lessCssProcessor = new LessCssProcessor();
        }
        return this.lessCssProcessor;
    }

    public void setLessCssProcessor(LessCssProcessor lessCssProcessor) {
        this.lessCssProcessor = lessCssProcessor;
    }

    protected final List<HeaderItem> dependencies = new ArrayList<>();

    protected final List<ResourceReference> dependenciesJavaScript = new ArrayList<>();

    protected final List<ResourceReference> dependenciesCss = new ArrayList<>();

    public LessResourceReference addDependency(HeaderItem dependency) {
        dependencies.add(dependency);
        return this;
    }

    public LessResourceReference addJavaScriptResourceReferenceDependency(ResourceReference dependency) {
        dependenciesJavaScript.add(dependency);
        return this;
    }

    public LessResourceReference addCssResourceReferenceDependency(ResourceReference dependency) {
        dependenciesCss.add(dependency);
        return this;
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        List<HeaderItem> l = Lists.newArrayList(super.getDependencies());
        l.addAll(dependencies);
        for (ResourceReference dependency : dependenciesJavaScript) {
            l.add(JavaScriptHeaderItem.forReference(dependency));
        }
        for (ResourceReference dependency : dependenciesCss) {
            l.add(CssHeaderItem.forReference(dependency));
        }
        return l;
    }
}
