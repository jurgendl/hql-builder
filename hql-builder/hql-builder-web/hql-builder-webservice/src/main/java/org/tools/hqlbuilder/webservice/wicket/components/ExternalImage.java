package org.tools.hqlbuilder.webservice.wicket.components;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;

/**
 * @see https://cwiki.apache.org/confluence/display/WICKET/How+to+load+an+external+image
 */
public class ExternalImage extends Image {
	private static final long serialVersionUID = 7113713255809950318L;

	public static final String SRC = "src";

	public static URI convert(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public ExternalImage(String id, String path) {
		super(id, path);
	}

	public ExternalImage(String id, URI uri) {
		this(id, uri.toASCIIString());
	}

	public ExternalImage(String id, URL url) {
		this(id, ExternalImage.convert(url));
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("src", this.getDefaultModelObjectAsString());
	}
}
