package org.tools.hqlbuilder.webcommon.resteasy;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
@Produces({ "text/*+xml", "application/*+xml" })
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
	protected JAXBContext jaxbContext;

	protected List<String> packages = new ArrayList<String>();

	public JAXBContextResolver(String... packages) {
		try {
			String contextPath = "org.tools.hqlbuilder.common";
			this.packages.add(contextPath);
			for (String pack : packages) {
				contextPath += ":" + pack;
				this.packages.add(pack);
			}
			jaxbContext = JAXBContext.newInstance(contextPath);
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		String packname = type.getPackage().getName();
		for (String pack : packages) {
			if (packname.startsWith(pack)) {
				return jaxbContext;
			}
		}
		return null;
	}
}