package org.tools.hqlbuilder.webcommon.resteasy;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.jhaws.common.io.jaxb.ThreadLocalMarshalling;

/**
 * <oxm:jaxb2-marshaller contextPath="org.tools.hqlbuilder.common:org.tools.hqlbuilder..."/>
 */
@Provider
@Produces({ "text/*+xml", "application/*+xml", "text/xml", "application/xml" })
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    protected static final char SEPERATOR = ':';

    protected static final String DEFAULT_PACKAGE = "org.tools.hqlbuilder.common";

    protected ThreadLocalMarshalling jaxbContext;

    protected Set<String> packages = new HashSet<>();

    public JAXBContextResolver(String... packages) {
            StringBuilder packagesstring = new StringBuilder(DEFAULT_PACKAGE);
            this.packages.add(DEFAULT_PACKAGE);
            for (String pack : packages) {
                this.packages.add(pack);
                packagesstring.append(SEPERATOR).append(pack);
            }
        jaxbContext = new ThreadLocalMarshalling(packagesstring.toString());
    }

    /**
     * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
     */
    @Override
    public JAXBContext getContext(Class<?> type) {
        String packname = type.getPackage().getName();
        for (String pack : packages) {
            if (packname.startsWith(pack)) {
                return jaxbContext.getJaxbContext();
            }
        }
        return null;
    }
}