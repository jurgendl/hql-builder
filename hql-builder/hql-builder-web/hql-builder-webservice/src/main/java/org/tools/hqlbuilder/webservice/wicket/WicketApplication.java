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
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.memory.HttpSessionDataStore;
import org.apache.wicket.pageStore.memory.PageNumberEvictionStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.MessageDigestResourceVersion;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tools.hqlbuilder.common.icons.WicketIconsRoot;
import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;
import org.wicketstuff.htmlcompressor.HtmlCompressingMarkupFactory;
import org.wicketstuff.pageserializer.kryo2.KryoSerializer;

import com.googlecode.wicket.jquery.core.resource.JQueryGlobalizeResourceReference;
import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;
import com.googlecode.wicket.jquery.core.settings.JQueryLibrarySettings;

import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;

public class WicketApplication extends WebApplication {
    protected static final Logger logger = LoggerFactory.getLogger(WicketApplication.class);

    // @SpringBean(name = "webProperties", required = false)
    protected transient Properties webProperties;

    protected boolean diskStore = false;

    public static WicketApplication get() {
        return WicketApplication.class.cast(WebApplication.get());
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static WicketSession getWebSession() {
        return WicketSession.get();
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
        getDebugSettings().setAjaxDebugModeEnabled(inDevelopment);
        getDebugSettings().setComponentUseCheck(inDevelopment);
        getDebugSettings().setDevelopmentUtilitiesEnabled(inDevelopment);
        getDebugSettings().setOutputComponentPath(inDevelopment);
        getDebugSettings().setOutputMarkupContainerClassName(inDevelopment);

        // resource settings
        getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));
        getResourceSettings().setUseMinifiedResources(deployed);
        getResourceSettings().setEncodeJSessionId(deployed);
        getResourceSettings().setDefaultCacheDuration(inDevelopment ? Duration.NONE : WebResponse.MAX_CACHE_DURATION);
        if (deployed) {
            // getResourceSettings().setJavaScriptCompressor(new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
            getResourceSettings().setCssCompressor(new YuiCssCompressor());
        }

        // library resources
        initDefaultResources();

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
    }

    protected JavaScriptReferenceHeaderItem jsBundleReference = null;

    protected CssReferenceHeaderItem cssBundleReference = null;

    /** only add CssResourceReference */
    protected List<ResourceReference> jsResources = new ArrayList<ResourceReference>();

    /** only add JavaScriptResourceReference */
    protected List<ResourceReference> cssResources = new ArrayList<ResourceReference>();

    protected void addToJsBundle(List<ResourceReference> js) {
        js.add(WicketJSRoot.COLORS);
        js.add(WicketJSRoot.FLOATING_BAR);
    }

    protected void addToJsResources(List<ResourceReference> js) {
        IJavaScriptLibrarySettings javaScriptLibrarySettings = getJavaScriptLibrarySettings();
        js.add(javaScriptLibrarySettings.getJQueryReference());
        js.add(javaScriptLibrarySettings.getWicketAjaxReference());
        js.add(javaScriptLibrarySettings.getWicketEventReference());
        if (javaScriptLibrarySettings instanceof IJQueryLibrarySettings) {
            IJQueryLibrarySettings javaScriptSettings = (IJQueryLibrarySettings) javaScriptLibrarySettings;
            js.add(javaScriptSettings.getJQueryGlobalizeReference());
            js.add(javaScriptSettings.getJQueryUIReference());
        }
        if (WicketApplication.get().usesDevelopmentConfig()) {
            js.add(javaScriptLibrarySettings.getWicketAjaxDebugReference());
        }
        js.add(WicketJSRoot.VELOCITY);
        addToJsBundle(js);
    }

    protected void addToCssBundle(List<ResourceReference> css) {
        css.add(WicketCSSRoot.COLORS);
        css.add(WicketCSSRoot.WE_LOVE_ICONS);
        css.add(WicketCSSRoot.GENERAL);
        // css.add(WicketCSSRoot.HORIZONTAL_MENU);
        // css.add(WicketCSSRoot.FORMS);
        // css.add(WicketCSSRoot.TABLES);
    }

    protected void addToCssResources(List<ResourceReference> css) {
        addToCssBundle(css);
    }

    protected void initDefaultResources() {
        IJQueryLibrarySettings settings = new JQueryLibrarySettings();
        settings.setJQueryGlobalizeReference(JQueryGlobalizeResourceReference.get()); // not set by default
        this.setJavaScriptLibrarySettings(settings);

        // disable bundling: css image url bug?

        // addToJsBundle(jsResources);
        // if (WicketApplication.get().usesDeploymentConfig() && !jsResources.isEmpty()) {
        // jsBundleReference = getResourceBundles().addJavaScriptBundle(WicketJSRoot.class, "jsbundle.js", // virtual name
        // jsResources.toArray(new JavaScriptResourceReference[jsResources.size()]));
        // jsResources.clear();
        // }
        addToJsResources(jsResources);

        // addToCssBundle(cssResources);
        // if (WicketApplication.get().usesDeploymentConfig() && !cssResources.isEmpty()) {
        // cssBundleReference = getResourceBundles().addCssBundle(WicketCSSRoot.class, "cssbundle.css", // virtual name
        // cssResources.toArray(new CssResourceReference[cssResources.size()]));
        // cssResources.clear();
        // }
        addToCssResources(cssResources);
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
        String[] mountedImages = { //
                //
                "arrow_off.png", //
                "arrow_up.png",//
                "arrow_down.png" //
        };//
        for (String mountedImage : mountedImages) {
            String fullpath = cssImages + mountedImage;
            PackageResourceReference reference = new PackageResourceReference(WicketRoot.class, fullpath);
            mountResource(fullpath, reference);
            logger.info("mounting image: " + WicketRoot.class.getCanonicalName() + ": " + fullpath);
        }
        for (Field field : WicketIconsRoot.class.getFields()) {
            try {
                final String name = String.valueOf(field.get(WicketIconsRoot.class));
                PackageResourceReference reference = new ResourceRef(WicketCSSRoot.class, cssImages + name, name);
                getSharedResources().add(cssImages + name, reference.getResource());
                mountResource(cssImages + name, reference);
                logger.info("mounting image: " + WicketRoot.class.getCanonicalName() + ": " + cssImages + name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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

    public JavaScriptReferenceHeaderItem getJsBundleReference() {
        return this.jsBundleReference;
    }

    public CssReferenceHeaderItem getCssBundleReference() {
        return this.cssBundleReference;
    }

    public void setJsBundleReference(JavaScriptReferenceHeaderItem jsBundleReference) {
        this.jsBundleReference = jsBundleReference;
    }

    public void setCssBundleReference(CssReferenceHeaderItem cssBundleReference) {
        this.cssBundleReference = cssBundleReference;
    }

    public List<ResourceReference> getJsResources() {
        return this.jsResources;
    }

    public List<ResourceReference> getCssResources() {
        return this.cssResources;
    }

    public void setJsResources(List<ResourceReference> jsResources) {
        this.jsResources = jsResources;
    }

    public void setCssResources(List<ResourceReference> cssResources) {
        this.cssResources = cssResources;
    }
}