package org.tools.hqlbuilder.service;

import java.io.IOException;
import java.lang.reflect.Modifier;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.hibernate.MappingException;
import org.hibernate.QueryException;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.BagType;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

public class InformationImpl extends LuceneInformation {
    public InformationImpl() {
        super();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void create(IndexWriter writer, SessionFactory sessionFactory, String classname, ClassMetadata classMetadata)
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, CorruptIndexException, IOException {
        Document doc = new Document();
        doc.add(new Field(NAME, classname, STORE, INDEX));
        doc.add(new Field(TYPE, CLASS, STORE, INDEX));
        StringBuilder csb = new StringBuilder();
        Class<?> c = Class.forName(classname);
        while (c != null && !c.equals(Object.class)) {
            csb.append(transformClassName(c.getName())).append("\n");
            for (Class<?> interfaced : c.getInterfaces()) {
                csb.append(transformClassName(interfaced.getName())).append("\n");
            }
            for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                if (f.getName().equals("serialVersionUID")) {
                    continue;
                }
                if (f.getName().contains("$")) {
                    continue;
                }

                Document fdoc = new Document();
                StringBuilder fsb = new StringBuilder();
                Class<?> enumType = null;
                String value = null;

                try {
                    // classMetadata.getIdentifierPropertyName();
                    Type propertyType = classMetadata.getPropertyType(f.getName());
                    if (propertyType instanceof BagType) {
                        BagType bagtype = (BagType) propertyType;
                        try {
                            String assoc = null;//CommonUtils.call(bagtype, "getAssociatedEntityName", String.class, sessionFactory);
                            fsb.append(transformClassName(assoc));
                        } catch (MappingException ex) {
                            try {
                                Type elementType = null;//CommonUtils.call(bagtype, "getElementType", Type.class, sessionFactory);
                                if (elementType instanceof CustomType) {
                                    CustomType ct = (CustomType) elementType;
                                    if ("org.hibernate.type.EnumType".equals(ct.getName())) {
                                        enumType = ct.getReturnedClass();
                                    } else {
                                        throw new UnsupportedOperationException();
                                    }
                                } else {
                                    throw new UnsupportedOperationException();
                                }
                            } catch (MappingException ex2) {
                                throw new UnsupportedOperationException();
                            }
                        }
                    } else {
                        fsb.append(transformClassName(f.getType().getName()));

                        if (Enum.class.isAssignableFrom(f.getType())) {
                            enumType = f.getType();
                        }
                    }
                } catch (QueryException ex) {
                    fsb.append(transformClassName(f.getType().getName()));

                    if (Enum.class.isAssignableFrom(f.getType())) {
                        enumType = f.getType();
                    } else if (String.class.equals(f.getType())) {
                        try {
                            value = (String) f.get(Class.forName(classname).newInstance());
                        } catch (Exception ex2) {
                            //
                        }
                    }
                }

                fsb.append(" ").append(f.getName());
                fsb.append(" ").append(proper(f.getName()));

                f.setAccessible(true);

                if (Modifier.isStatic(f.getModifiers())) {
                    fsb.append(" ").append(String.valueOf(f.get(null)));
                } else if (value != null) {
                    fsb.append(" ").append(value);
                    fsb.append(" ").append(proper(value));
                }

                if (enumType != null) {
                    printEnumValues((Class<? extends Enum>) enumType, fsb);
                }

                fsb.append("\n");

                fdoc.add(new Field(NAME, classname + "#" + f.getName(), STORE, INDEX));
                fdoc.add(new Field(TYPE, FIELD, STORE, INDEX));
                fdoc.add(new Field(DATA, fsb.toString().trim(), STORE, INDEX));
                writer.addDocument(fdoc);

                csb.append(fsb.toString());
            }

            c = c.getSuperclass();
            csb.append("\n");
        }

        doc.add(new Field(DATA, csb.toString().trim(), STORE, INDEX));
        writer.addDocument(doc);
    }
}
