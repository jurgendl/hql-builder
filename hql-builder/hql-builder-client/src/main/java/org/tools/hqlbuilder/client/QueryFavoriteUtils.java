package org.tools.hqlbuilder.client;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.jhaws.common.io.jaxb.ThreadLocalMarshalling;
import org.slf4j.LoggerFactory;

public class QueryFavoriteUtils implements HqlBuilderFrameConstants {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QueryFavoriteUtils.class);

    private static ThreadLocalMarshalling jaxbContext;

    private static File[] convertV1ToV2(File[] xmls) {
        List<File> files = new ArrayList<>();
        for (File xml : xmls) {
            File c = convertV1ToV2(xml);
            if (c != null) {
                files.add(c);
            }
        }
        return files.toArray(new File[files.size()]);
    }

    private static File convertV1ToV2(File xml) {
        try {
            if (xml.getName().startsWith(HqlBuilderFrameConstants.FAVORITE_PREFIX)) {
                return null;
            }
            QueryFavorite favorite;
            try (FileInputStream is = new FileInputStream(xml); XMLDecoder dec = new XMLDecoder(is)) {
                favorite = QueryFavorite.class.cast(dec.readObject());
            }
            File file = new File(xml.getParentFile(), HqlBuilderFrameConstants.FAVORITE_PREFIX + xml.getName());
            try (FileOutputStream os = new FileOutputStream(file)) {
                getJaxbcontext().marshall(favorite, os);
            }
            xml.delete();
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static ThreadLocalMarshalling getJaxbcontext() throws JAXBException {
        if (jaxbContext == null) {
            jaxbContext = new ThreadLocalMarshalling(QueryFavorite.class);
            jaxbContext.setFormatOutput(true);
        }
        return jaxbContext;
    }

    public static void save(String projectdir, String name, QueryFavorite favorite) {
        File favoritesForProject = new File(FAVORITES_DIR, projectdir);
        File xmlhistory = new File(favoritesForProject, FAVORITE_PREFIX + name + FAVORITES_EXT);
        if (!favoritesForProject.exists()) {
            favoritesForProject.mkdirs();
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(xmlhistory);
            getJaxbcontext().marshall(favorite, os);
        } catch (IOException ex) {
            logger.error("", ex);
        } catch (JAXBException ex) {
            logger.error("", ex);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex2) {
                    //
                }
            }
        }
    }

    public static QueryFavorite load(String projectdir, LinkedList<QueryFavorite> favorites) {
        File favoritesForProject = new File(FAVORITES_DIR, projectdir);
        if (!favoritesForProject.exists()) {
            favoritesForProject.mkdir();
        }
        File[] xmls = favoritesForProject.listFiles((FilenameFilter) (dir, name) -> name.endsWith(FAVORITES_EXT));
        if (xmls == null) {
            return null;
        }
        QueryFavoriteUtils.convertV1ToV2(xmls);
        xmls = favoritesForProject.listFiles((FilenameFilter) (dir, name) -> name.startsWith(FAVORITE_PREFIX) && name.endsWith(FAVORITES_EXT));
        QueryFavorite last = null;
        File _xml = null;
        try {
            for (File xml : xmls) {
                _xml = xml;
                logger.info("favorite {}", xml);
                try {
                    try (FileInputStream is = new FileInputStream(xml)) {
                        QueryFavorite favorite = getJaxbcontext().<QueryFavorite> unmarshall(is);
                        if (favorites.contains(favorite)) {
                            favorites.remove(favorites.indexOf(favorite));
                        }
                        favorites.add(favorite);
                        if (xml.getName().equals(FAVORITE_PREFIX + LAST + FAVORITES_EXT)) {
                            last = favorite;
                        }
                    } catch (UnmarshalException ex) {
                        QueryFavoriteUtils.convertV2ToV3(xml);
                        try (FileInputStream xmlin = new FileInputStream(xml)) {
                            QueryFavorite favorite = getJaxbcontext().<QueryFavorite> unmarshall(xmlin);
                            if (favorites.contains(favorite)) {
                                favorites.remove(favorites.indexOf(favorite));
                            }
                            favorites.add(favorite);
                            if (xml.getName().equals(FAVORITE_PREFIX + LAST + FAVORITES_EXT)) {
                                last = favorite;
                            }
                        }
                    }
                } catch (Exception ex) {
                    logger.error("{}", xml, ex);
                }
            }
        } catch (Exception ex) {
            logger.error("{}", _xml, ex);
        }
        return last;
    }

    private static void convertV2ToV3(File xml) throws IOException {
        FileInputStream in = new FileInputStream(xml);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        String tmp = new String(buffer);
        tmp = tmp.replaceAll("\\Q<hql:\\E", "<");
        tmp = tmp.replaceAll("\\Q</hql:\\E", "</");
        tmp = tmp.replaceAll("\\Qxmlns:hql=\"http://hql-builder.google.com\"\\E", "");
        FileOutputStream out = new FileOutputStream(xml);
        out.write(tmp.getBytes());
        out.close();
    }
}
