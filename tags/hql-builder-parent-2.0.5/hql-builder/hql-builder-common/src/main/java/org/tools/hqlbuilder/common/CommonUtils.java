package org.tools.hqlbuilder.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CommonUtils {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static void find(Class<?> c) {
        try {
            java.util.Enumeration<?> r = c.getClassLoader().getResources(c.getName().replace('.', '/') + ".class");
            while (r.hasMoreElements()) {
                Object url = r.nextElement();
                System.out.println(url);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void findcp(String c) {
        try {
            ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
            for (String p : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
                try {
                    File f = new File(p);
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { f.toURI().toURL() }, parent);
                    System.out.println(p + "\n\t" + urlClassLoader.loadClass(c).getName());
                } catch (Throwable ex) {
                    System.out.println(p);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** getFromXml(mainpom.getAbsolutePath(), "/ns1:project/ns1:modules/ns1:module/text()"); */
    public static Object getFromXml(InputStream xmlFile, String base, String string) {
        try {
            Document doc = parseXml(xmlFile);
            return xpathXml(string, doc, base);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static class NamespaceHandler extends HashMap<String, String> implements NamespaceContext {
        private static final long serialVersionUID = -5509377982038203847L;

        private static final String DEFAULT = "default";

        private static final String XMLNS = "xmlns";

        private NamespaceHandler(NamedNodeMap basenode) {
            init(basenode);
        }

        protected void init(NamedNodeMap projectAtts) {
            for (int i = 0; i < projectAtts.getLength(); i++) {
                Node item = projectAtts.item(i);
                String ns = item.getNodeName();
                if (ns.startsWith(XMLNS)) {
                    int index = ns.indexOf(':');
                    if (index == -1) {
                        ns = DEFAULT;
                    } else {
                        ns = ns.substring(index + 1);
                    }
                    String v = item.getNodeValue();
                    put(ns, v);
                }
            }
        }

        @Override
        public String getNamespaceURI(String prefix) {
            for (String key : keySet()) {
                if (prefix.equals(key)) {
                    return get(key);
                }
            }
            return get(DEFAULT);
        }

        /** Dummy implemenation - not used! */
        @Override
        public Iterator<?> getPrefixes(String val) {
            return null;
        }

        /** Dummy implemenation - not used! */
        @Override
        public String getPrefix(String uri) {
            return null;
        }
    }

    private static Object xpathXml(String expr, Document doc, String basenodename) throws XPathExpressionException, IOException {
        // http://www.ibm.com/developerworks/library/x-javaxpathapi/
        // http://blog.davber.com/2006/09/17/xpath-with-namespaces-in-java/
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NamespaceHandler(NodeList.class.cast(doc.getElementsByTagName(basenodename)).item(0).getAttributes()));
        List<Node> results = new ArrayList<Node>();
        NodeList nodes = NodeList.class.cast(xpath.compile(expr).evaluate(doc, XPathConstants.NODESET));
        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            results.add(nodes.item(i));
        }
        return results.size() == 0 ? null : (results.size() == 1 ? results.get(0) : results);
    }

    private static Document parseXml(InputStream xmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        return doc;
    }

    public static char[] whitespace_chars = ( //
    "\u0009" // CHARACTER TABULATION
            + "\n" // LINE FEED (LF)
            + "\u000B" // LINE TABULATION
            + "\u000C" // FORM FEED (FF)
            + "\r" // CARRIAGE RETURN (CR)
            + "\u0020" // SPACE
            + "\u0085" // NEXT LINE (NEL)
            + "\u00A0" // NO-BREAK SPACE
            + "\u1680" // OGHAM SPACE MARK
            + "\u180E" // MONGOLIAN VOWEL SEPARATOR
            + "\u2000" // EN QUAD
            + "\u2001" // EM QUAD
            + "\u2002" // EN SPACE
            + "\u2003" // EM SPACE
            + "\u2004" // THREE-PER-EM SPACE
            + "\u2005" // FOUR-PER-EM SPACE
            + "\u2006" // SIX-PER-EM SPACE
            + "\u2007" // FIGURE SPACE
            + "\u2008" // PUNCTUATION SPACE
            + "\u2009" // THIN SPACE
            + "\u200A" // HAIR SPACE
            + "\u2028" // LINE SEPARATOR
            + "\u2029" // PARAGRAPH SEPARATOR
            + "\u202F" // NARROW NO-BREAK SPACE
            + "\u205F" // MEDIUM MATHEMATICAL SPACE
            + "\u3000" // IDEOGRAPHIC SPACE
    ).toCharArray();

    public static String removeUnnecessaryWhiteSpaces(String s) {
        StringBuilder sb = new StringBuilder();
        boolean wasWhiteSpace = false; // was previous character(s) whitespaces
        boolean start = true; // to remove whitespaces at front
        for (char c : s.toCharArray()) {
            if (isWhiteSpace(c)) {
                if (start || wasWhiteSpace) {
                    continue;
                }
                wasWhiteSpace = true;
            } else {
                if (wasWhiteSpace) {
                    sb.append(" "); // replace all previous whitespaces by a single space
                }
                sb.append(c); // append non-whitespace character
                wasWhiteSpace = false;
                start = false;
            }
        }
        return sb.toString();
    }

    public static boolean isWhiteSpace(char c) {
        for (char ws : whitespace_chars) {
            if (ws == c) {
                return true;
            }
        }
        return false;
    }

    public static byte[] read(InputStream in) throws IOException {
        byte[] tmp = new byte[in.available()];
        if (in.read(tmp) != tmp.length) {
            throw new IOException("not fully read");
        }
        in.close();
        return tmp;
    }

    public static void call(Object object, String methodName) {
        call(object, methodName, Void.TYPE);
    }

    public static Object create(String className) {
        return create(className, new Class[0], new Object[0]);
    }

    public static Object create(String className, Class<?> parameterTypes, Object initargs) {
        return create(className, new Class[] { parameterTypes }, new Object[] { initargs });
    }

    public static Object create(String className, Object initargs) {
        return create(className, new Class[] { initargs.getClass() }, new Object[] { initargs });
    }

    public static Object create(String className, Object[] initargs) {
        @SuppressWarnings("rawtypes")
        Class[] parameterTypes = new Class[initargs.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = initargs[i].getClass();
        }
        return create(className, parameterTypes, initargs);
    }

    public static Object create(String className, @SuppressWarnings("rawtypes") Class[] parameterTypes, Object[] initargs) {
        try {
            return Class.forName(className).getConstructor(parameterTypes).newInstance(initargs);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T call(Object object, String methodName, Class<T> type, Object... params) {
        logger.debug(String.valueOf(object));
        logger.debug(methodName);
        logger.debug(Arrays.toString(params));
        MethodInvokingFactoryBean mi = new MethodInvokingFactoryBean();
        mi.setTargetObject(object);
        mi.setTargetMethod(methodName);
        mi.setArguments(params);
        try {
            mi.afterPropertiesSet();
            T value = type.cast(mi.getObject());
            logger.debug(String.valueOf(value));
            return value;
        } catch (RuntimeException ex) {
            logger.error("org.tools.hqlbuilder.service.HqlServiceImpl.call(Object, String, Class<T>, Object...)");
            logger.error(ex.getClass().getName());
            logger.error(String.valueOf(ex));
            throw ex;
        } catch (Exception ex) {
            logger.error("org.tools.hqlbuilder.service.HqlServiceImpl.call(Object, String, Class<T>, Object...)");
            logger.error(ex.getClass().getName());
            logger.error(String.valueOf(ex));
            throw new RuntimeException(ex);
        }
    }

    public static String readMavenVersion(String pack) throws Exception {
        String[] dp = pack.split(":");
        Properties p = new Properties();
        String name = "META-INF/maven/" + dp[0] + "/" + dp[1] + "/pom.properties";
        InputStream resourceAsStream = CommonUtils.class.getClassLoader().getResourceAsStream(name);
        if (resourceAsStream == null) {
            return null;
        }
        p.load(resourceAsStream);
        String value = p.getProperty("version");
        if (value == null) {
            return null;
        }
        return value;
    }

    public static String readManifestVersion(String cn) throws Exception {
        Class<?> clazz = Class.forName(cn);
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            return null;
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());
        Attributes attr = manifest.getMainAttributes();
        String value = attr.getValue("Version");
        if (value == null) {
            value = attr.getValue("Implementation-Version");
        }
        if (value == null) {
            return null;
        }
        return value;
    }
}
