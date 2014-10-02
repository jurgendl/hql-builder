package org.tools.hqlbuilder.webservice.wicket.sass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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

import ro.isdc.wro.extensions.processor.css.SassCssProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.CssCompressorProcessor;

/**
 * response.render(CssHeaderItem.forReference(new
 * SassResourceReference(WicketCSSRoot.class, "table.sass")));
 *
 * @see http://sass-lang.com/
 * @see https://code.google.com/p/wro4j/
 */
public class SassResourceReference extends StreamResourceReference implements
		IResourceStream {
	private static final long serialVersionUID = 6384603768717480808L;

	protected static final Logger logger = LoggerFactory
			.getLogger(SassResourceReference.class);

	protected final String charset = "utf-8";

	protected final String contentType = "text/css";

	protected transient Bytes length = null;

	protected transient String css = "";

	protected transient Time lastModified = null;

	protected transient ResourcePreProcessor sassCssProcessor;

	protected transient ResourcePreProcessor cssCompressorProcessor;

	public SassResourceReference(Class<?> scope, String name) {
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
		logger.info(getSassName() + " - sending lastModifiedTime: "
				+ lastModified);
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
			String fullPath = getResourcePath() + '/' + getSassName();
			if (css == null) {
				logger.info("building " + fullPath + " because is new");
				rebuild = true;
			} else if (lastModified == null) {
				logger.info("building " + fullPath + " because out of date");
				rebuild = true;
			} else if (WicketApplication.get().usesDevelopmentConfig()) {
				try {
					if (lastModified.getMilliseconds() < getClass()
							.getClassLoader().getResource(fullPath)
							.openConnection().getLastModified()) {
						logger.info("building " + fullPath
								+ " because template out of date");
						rebuild = true;
					}
				} catch (IOException ex1) {
					logger.info("building " + fullPath
							+ " because of exception: " + ex1);
					rebuild = true;
				}
			}
			if (rebuild) {
				ByteArrayOutputStream out;
				try {
					out = new ByteArrayOutputStream();
					getSassCssProcessor().process(null,
							new InputStreamReader(read()),
							new OutputStreamWriter(out));
					// ByteArrayInputStream in = new ByteArrayInputStream(
					// out.toByteArray());
					// out = new ByteArrayOutputStream();
					// getCssCompressorProcessor().process(null,
					// new InputStreamReader(in),
					// new OutputStreamWriter(out));
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

	protected String getSassName() {
		return getName();
	}

	protected String getResourcePath() {
		return getScope().getPackage().getName().replace('.', '/');
	}

	protected InputStream read() throws IOException {
		return getClass().getClassLoader().getResourceAsStream(
				getResourcePath() + '/' + getSassName());
	}

	protected void write(OutputStream out) throws IOException {
		getSassCssProcessor().process(null, new InputStreamReader(read()),
				new OutputStreamWriter(out));
	}

	public ResourcePreProcessor getSassCssProcessor() {
		if (sassCssProcessor == null) {
			sassCssProcessor = new SassCssProcessor();
		}
		return this.sassCssProcessor;
	}

	public void setSassCssProcessor(ResourcePreProcessor sassCssProcessor) {
		this.sassCssProcessor = sassCssProcessor;
	}

	public ResourcePreProcessor getCssCompressorProcessor() {
		if (cssCompressorProcessor == null)
			cssCompressorProcessor = new CssCompressorProcessor();
		return cssCompressorProcessor;
	}

	public void setCssCompressorProcessor(
			ResourcePreProcessor cssCompressorProcessor) {
		this.cssCompressorProcessor = cssCompressorProcessor;
	}
}
