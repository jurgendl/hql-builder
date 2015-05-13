package org.tools.hqlbuilder.webservice.jquery.ui.flowplayer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.StreamResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketUtils;


/**
 * @see https://developer.mozilla.org/nl/docs/Web/Guide/HTML/HTML5_audio_en_video_gebruiken
 */
public class FlowPlayer {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(FlowPlayer.class, "flowplayer.js");

	public static CssResourceReference SKIN_CSS_MINI = new CssResourceReference(FlowPlayer.class, "skin/minimalist.css");

	public static CssResourceReference SKIN_CSS = new CssResourceReference(FlowPlayer.class, "skin/functional.css");

	public static String swf = "flowplayer.swf";

	static {
		try {
			JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
		} catch (Exception e) {
			//
		}
	}

	private static String url = null;

	public static String url() {
		if (url == null) {
			url = WicketUtils.mountStream(FlowPlayer.swf, new StreamResourceReference(FlowPlayer.class, FlowPlayer.swf) {
				private static final long serialVersionUID = -5168885592353617194L;

				@Override
				public IResourceStream getResourceStream() {
					return new AbstractResourceStream() {
						private static final long serialVersionUID = 8784023512223151193L;

						protected transient InputStream inputStream;

						@Override
						public void close() throws IOException {
							if (inputStream != null) {
								inputStream.close();
							}
						}

						@Override
						public InputStream getInputStream() throws ResourceStreamNotFoundException {
							if (inputStream == null) {
								try {
									inputStream = FlowPlayer.class.getClassLoader().getResourceAsStream(FlowPlayer.class.getPackage().getName().replace('.', '/') + "/" + swf);
								} catch (Exception e) {
									throw new ResourceStreamNotFoundException(e);
								}
							}
							return inputStream;
						}
					};
				}
			});
		}
		return url;
	}
}
