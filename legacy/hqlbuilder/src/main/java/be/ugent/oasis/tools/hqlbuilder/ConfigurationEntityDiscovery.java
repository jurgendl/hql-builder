package be.ugent.oasis.tools.hqlbuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * JDOC
 * 
 * @author jdlandsh
 * 
 */
public class ConfigurationEntityDiscovery implements EntityDiscovery {
    protected List<Class<?>> getEntityClasses0(String resource) throws ClassNotFoundException, ParserConfigurationException, SAXException,
            IOException {
        List<Class<?>> entities = new ArrayList<Class<?>>();

        for (String classname : parse(resource)) {
            entities.add(Class.forName(classname));
        }

        return entities;
    }

    protected List<String> parse(String resource) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = ConfigurationEntityDiscovery.class.getClassLoader().getResourceAsStream(resource);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.parse(is);
        Element root = doc.getDocumentElement();
        root.normalize();

        Node node = root.getChildNodes().item(1);
        NodeList childNodes = node.getChildNodes();

        ArrayList<String> classNames = new ArrayList<String>();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (child.getAttributes() != null) {
                classNames.add(child.getAttributes().item(0).getNodeValue());
            }
        }

        is.close();

        return classNames;
    }

    /**
     * 
     * @see util.hibernate.core.EntityDiscovery#getEntityClasses(java.lang.String)
     */
    @Override
    public List<Class<?>> getEntityClasses(String resource) {
        try {
            return getEntityClasses0(resource);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
