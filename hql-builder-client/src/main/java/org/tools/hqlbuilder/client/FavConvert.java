package org.tools.hqlbuilder.client;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class FavConvert {
    public static File[] convert(File[] xmls) {
        List<File> files = new ArrayList<File>();
        for (File xml : xmls) {
            File c = convert(xml);
            if (c != null) {
                files.add(c);
            }
        }
        return files.toArray(new File[files.size()]);
    }

    public static File convert(File xml) {
        try {

            if (xml.getName().startsWith(HqlBuilderFrameConstants.FAVORITE_PREFIX)) {
                return null;
            }
            QueryFavorite favorite;
            {
                FileInputStream is = new FileInputStream(xml);
                XMLDecoder dec = new XMLDecoder(is);
                favorite = (QueryFavorite) dec.readObject();
                is.close();
                dec.close();
            }
            File file;
            {
                JAXBContext jaxbContext = JAXBContext.newInstance(QueryFavorite.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                file = new File(xml.getParentFile(), HqlBuilderFrameConstants.FAVORITE_PREFIX + xml.getName());
                FileOutputStream os = new FileOutputStream(file);
                jaxbMarshaller.marshal(favorite, os);
                os.close();
            }
            xml.delete();
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
