package org.tools.hqlbuilder.webservice.wicket;

import java.io.IOException;
import java.util.Properties;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IMarkupSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tools.hqlbuilder.webservice.WicketRoot;

import com.googlecode.wicket.jquery.core.resource.JQueryGlobalizeResourceReference;
import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;
import com.googlecode.wicket.jquery.core.settings.JQueryLibrarySettings;

public class WicketApplication extends WebApplication {
    protected static final Logger logger = LoggerFactory.getLogger(WicketApplication.class);

    @SpringBean(name = "securityProperties", required = false)
    private Properties securityProperties;

    public static WicketApplication get() {
        return WicketApplication.class.cast(WebApplication.get());
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static WicketSession getWebSession() {
        return WicketSession.class.cast(Session.get());
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new WicketSession(request);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<DefaultWebPage> getHomePage() {
        return DefaultWebPage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        Injector.get().inject(this);

        IJQueryLibrarySettings settings = new JQueryLibrarySettings();
        settings.setJQueryGlobalizeReference(JQueryGlobalizeResourceReference.get());
        this.setJavaScriptLibrarySettings(settings);

        IMarkupSettings markupSettings = getMarkupSettings();
        markupSettings.setStripComments(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT ? true : false);
        markupSettings.setCompressWhitespace(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT ? true : false);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        final AnnotationTypeFilter mountedPageFilter = new AnnotationTypeFilter(MountedPage.class);
        final AssignableTypeFilter webPageFilter = new AssignableTypeFilter(WebPage.class);
        TypeFilter TypeFilter = new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return mountedPageFilter.match(metadataReader, metadataReaderFactory) && webPageFilter.match(metadataReader, metadataReaderFactory);
            }
        };
        scanner.addIncludeFilter(TypeFilter);
        for (BeanDefinition bd : scanner.findCandidateComponents(WicketRoot.class.getPackage().getName())) {
            try {
                String className = bd.getBeanClassName();
                logger.info("mounting page " + className);
                @SuppressWarnings("unchecked")
                Class<WebPage> pageClass = (Class<WebPage>) Class.forName(className);
                MountedPage mountedPage = pageClass.getAnnotation(MountedPage.class);
                String path = mountedPage.value();
                if (path.startsWith("${") && path.endsWith("}")) {
                    if (securityProperties != null) {
                        path = securityProperties.getProperty(path.substring(2, path.length() - 1));
                    }
                }
                logger.info("on path " + path);
                mountPage(path, pageClass);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Properties getSecurityProperties() {
        return this.securityProperties;
    }

    public void setSecurityProperties(Properties securityProperties) {
        this.securityProperties = securityProperties;
    }
}