package org.tools.hqlbuilder.webservice.wicket;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Session;
import org.apache.wicket.devutils.diskstore.DebugDiskDataStore;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.memory.HttpSessionDataStore;
import org.apache.wicket.pageStore.memory.PageNumberEvictionStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.MessageDigestResourceVersion;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.tools.hqlbuilder.common.icons.WicketIconsRoot;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.htmlcompressor.HtmlCompressingMarkupFactory;
import org.wicketstuff.logback.ConfiguratorPage;
import org.wicketstuff.pageserializer.kryo2.KryoSerializer;

import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;

public class WicketApplication extends WebApplication {
    protected static final Logger logger = LoggerFactory.getLogger(WicketApplication.class);

    @SpringBean(name = "webProperties", required = false)
    protected transient Properties webProperties;

    protected boolean diskStore = false;

    protected boolean showDebugbars = true;

    protected boolean checkCookiesEnabled = true;

    protected boolean checkJavaScriptEnabled = true;

    protected boolean checkAdsEnabled = false;

    protected String pagesPackages = "org.tools.hqlbuilder.webservice.wicket.pages";

    public static WicketApplication get() {
        return WicketApplication.class.cast(WebApplication.get());
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

        boolean deployed = usesDeploymentConfig();
        boolean inDevelopment = !deployed;

        // spring injector
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        Injector.get().inject(this);

        // framework settings
        if (deployed) {
            getFrameworkSettings().setSerializer(new KryoSerializer());
        }

        // markup settings
        getMarkupSettings().setStripComments(deployed);
        getMarkupSettings().setCompressWhitespace(deployed);
        getMarkupSettings().setStripWicketTags(true);
        if (deployed) {
            getMarkupSettings().setMarkupFactory(new HtmlCompressingMarkupFactory());
        }

        // request logger settings
        getRequestLoggerSettings().setRecordSessionSize(inDevelopment);
        getRequestLoggerSettings().setRequestLoggerEnabled(inDevelopment);

        // debug settings
        getDebugSettings().setAjaxDebugModeEnabled(showDebugbars && inDevelopment);
        getDebugSettings().setComponentUseCheck(inDevelopment);
        getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
        getDebugSettings().setOutputMarkupContainerClassName(inDevelopment);
        getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
        // getDebugSettings().setOutputComponentPath(inDevelopment);

        // resource settings
        getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));
        getResourceSettings().setUseMinifiedResources(deployed);
        getResourceSettings().setEncodeJSessionId(deployed);
        getResourceSettings().setDefaultCacheDuration(inDevelopment ? Duration.NONE : WebResponse.MAX_CACHE_DURATION);

        if (deployed) {
            // minify your resources on deploy
            // getResourceSettings().setJavaScriptCompressor(new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
            // getResourceSettings().setCssCompressor(new YuiCssCompressor());
        }

        // library resources
        this.setJavaScriptLibrarySettings(new WicketResourceReferences());

        // to put javascript down on the page (DefaultWebPage.html must contain wicket:id='footer-bucket'
        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator("footer-bucket"));

        // store
        initStore();

        // stateless checker
        if (inDevelopment) {
            getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
        }

        // mount resources
        mountImages();

        // mount pages
        mountPages();

        getMarkupSettings().setDefaultBeforeDisabledLink("");
        getMarkupSettings().setDefaultAfterDisabledLink("");

        if (!inDevelopment) {
            getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
            // getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class);
            // getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
            // getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
        }
    }

    protected void initStore() {
        if (usesDevelopmentConfig()) {
            if (!diskStore) {
                setPageManagerProvider(new DefaultPageManagerProvider(this) {
                    @Override
                    protected IDataStore newDataStore() {
                        return new HttpSessionDataStore(getPageManagerContext(), new PageNumberEvictionStrategy(20));
                    }
                });
            } else {
                DebugDiskDataStore.register(this);
            }
        } else {
            if (!diskStore) {
                setPageManagerProvider(new DefaultPageManagerProvider(this) {
                    @Override
                    protected IDataStore newDataStore() {
                        return new HttpSessionDataStore(getPageManagerContext(), new PageNumberEvictionStrategy(20));
                    }
                });
            } // no else
        }
    }

    protected void mountPages() {
        // https://github.com/wicketstuff/core/wiki/Logback
        // also see web.xml and logback.xml
        mountPage("logback", ConfiguratorPage.class);

        new AnnotatedMountScanner().scanPackage(pagesPackages).mount(this);

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
        List<String> paths = new ArrayList<String>();
        for (BeanDefinition bd : scanner.findCandidateComponents(WicketRoot.class.getPackage().getName())) {
            try {
                String className = bd.getBeanClassName();
                logger.info("mounting page " + className);
                @SuppressWarnings("unchecked")
                Class<WebPage> pageClass = (Class<WebPage>) Class.forName(className);
                MountedPage mountedPage = pageClass.getAnnotation(MountedPage.class);
                String path = mountedPage.value();
                boolean doMount = true;
                if (path.startsWith("${") && path.endsWith("}")) {
                    if (webProperties != null) {
                        path = webProperties.getProperty(path.substring(2, path.length() - 1));
                    } else {
                        doMount = false;
                    }
                }
                if (paths.contains(path)) {
                    throw new IllegalArgumentException("mounting multiple pages on the same path " + path);
                }
                paths.add(path);
                logger.info("on path " + path);
                if (doMount) {
                    mountPage(path, pageClass);
                }
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected void mountImages() {
        String cssImages = "css/images/";
        for (Field field : WicketIconsRoot.class.getFields()) {
            try {
                final String name = String.valueOf(field.get(WicketIconsRoot.class));
                PackageResourceReference reference = new VirtualPackageResourceReference(WicketCSSRoot.class, cssImages + name,
                        WicketIconsRoot.class, name);
                getSharedResources().add(cssImages + name, reference.getResource());
                mountResource(cssImages + name, reference);
                logger.info("mounting image: " + WicketRoot.class.getCanonicalName() + ": " + cssImages + name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        PrimeUI.mountImages(this);
    }

    public Properties getWebProperties() {
        return this.webProperties;
    }

    public void setWebProperties(Properties webProperties) {
        this.webProperties = webProperties;
    }

    /**
     * @see org.apache.wicket.Application#newConverterLocator()
     */
    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = new ConverterLocator();
        DateTimeConverter dateConverter = new DateTimeConverter();
        locator.set(java.sql.Date.class, dateConverter);
        locator.set(java.util.Date.class, dateConverter);
        locator.set(java.sql.Timestamp.class, dateConverter);
        return locator;
    }

    public final class DateTimeConverter extends DateConverter {
        private static final long serialVersionUID = -6075171947424780395L;

        @Override
        public DateFormat getDateFormat(Locale locale) {
            return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        }
    }

    public boolean isShowDebugbars() {
        return this.showDebugbars;
    }

    public void setShowDebugbars(boolean showDebugbars) {
        this.showDebugbars = showDebugbars;
    }

    public boolean isCheckCookiesEnabled() {
        return this.checkCookiesEnabled;
    }

    public boolean isCheckJavaScriptEnabled() {
        return this.checkJavaScriptEnabled;
    }

    public void setCheckCookiesEnabled(boolean checkCookiesEnabled) {
        this.checkCookiesEnabled = checkCookiesEnabled;
    }

    public void setCheckJavaScriptEnabled(boolean checkJavaScriptEnabled) {
        this.checkJavaScriptEnabled = checkJavaScriptEnabled;
    }

    public boolean isCheckAdsEnabled() {
        return this.checkAdsEnabled;
    }

    public void setCheckAdsEnabled(boolean checkAdsEnabled) {
        this.checkAdsEnabled = checkAdsEnabled;
    }
}