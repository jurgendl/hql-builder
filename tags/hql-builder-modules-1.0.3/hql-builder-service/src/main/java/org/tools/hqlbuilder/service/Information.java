package org.tools.hqlbuilder.service;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.MappingException;
import org.hibernate.QueryException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.JoinedSubclassEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.BagType;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

public class Information {
    public static final Version LUCENE_VERSION = Version.LUCENE_35;

    public static final String FIELD = "field";

    public static final String CLASS = "class";

    public static final String TYPE = "type";

    public static final String DATA = "data";

    public static final String NAME = "name";

    private static final Store STORE_DATA = Field.Store.YES;

    private final Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);

    private final Directory index;

    public Information(SessionFactory sessionFactory) throws IOException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException {
        Map<String, ?> allClassMetadata = sessionFactory.getAllClassMetadata();

        index = new RAMDirectory(); // new NIOFSDirectory(new File(".index"));
        IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION, analyzer);
        IndexWriter w = new IndexWriter(index, config);

        for (Map.Entry<String, ?> i : allClassMetadata.entrySet()) {
            if (i.getValue() instanceof JoinedSubclassEntityPersister) {
                JoinedSubclassEntityPersister p = (JoinedSubclassEntityPersister) i.getValue();
                create(w, sessionFactory, i.getKey(), p.getClassMetadata());
            } else if (i.getValue() instanceof SingleTableEntityPersister) {
                SingleTableEntityPersister p = (SingleTableEntityPersister) i.getValue();
                create(w, sessionFactory, i.getKey(), p.getClassMetadata());
            } else {
                throw new UnsupportedOperationException(i.getValue().getClass().getName());
            }
        }

        w.close();

        ready = true;
    }

    public List<String> search(String text, String typeName) throws ParseException, IOException {
        Query q;
        if (typeName != null) {
            BooleanQuery bq = new BooleanQuery();
            Query query = new QueryParser(LUCENE_VERSION, DATA, analyzer).parse(text);
            bq.add(query, BooleanClause.Occur.MUST);
            bq.add(new TermQuery(new Term(TYPE, typeName)), BooleanClause.Occur.MUST);
            q = bq;
        } else {
            q = new QueryParser(LUCENE_VERSION, DATA, analyzer).parse(text);
        }

        int hitsPerPage = 200;
        IndexSearcher searcher = new IndexSearcher(IndexReader.open(index));
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        List<String> results = new ArrayList<String>();

        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get(NAME));
            System.out.println(d.getFieldable(DATA).stringValue());
            System.out.println();
            results.add(d.get(NAME));
        }

        searcher.close();

        return results;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void create(IndexWriter writer, SessionFactory sessionFactory, String classname, ClassMetadata classMetadata)
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, CorruptIndexException, IOException {
        Document doc = new Document();
        doc.add(new Field(NAME, classname, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(TYPE, CLASS, Field.Store.YES, Field.Index.ANALYZED));
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
                            String assoc = bagtype.getAssociatedEntityName((SessionFactoryImplementor) sessionFactory);
                            fsb.append(transformClassName(assoc));
                        } catch (MappingException ex) {
                            try {
                                Type elementType = bagtype.getElementType((SessionFactoryImplementor) sessionFactory);
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

                fdoc.add(new Field(NAME, classname + "#" + f.getName(), Field.Store.YES, Field.Index.ANALYZED));
                fdoc.add(new Field(TYPE, FIELD, Field.Store.YES, Field.Index.ANALYZED));
                fdoc.add(new Field(DATA, fsb.toString().trim(), STORE_DATA, Field.Index.ANALYZED));
                writer.addDocument(fdoc);

                csb.append(fsb.toString());
            }

            c = c.getSuperclass();
            csb.append("\n");
        }

        doc.add(new Field(DATA, csb.toString().trim(), STORE_DATA, Field.Index.ANALYZED));
        writer.addDocument(doc);
    }

    private String proper(String name) {
        if (name.equals(name.toUpperCase())) {
            return name.replace("_", " ");
        }
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(" ");
            }
            sb.append(c);
        }
        return sb.toString().trim();
    }

    private Object transformClassName(String name) {
        try {
            return name.substring(1 + name.substring(0, name.lastIndexOf('.')).lastIndexOf('.')).replaceAll("\\.", " ");
        } catch (IndexOutOfBoundsException ex) {
            return name.replaceAll("\\.", " ");
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void printEnumValues(Class<? extends Enum> enumclass, StringBuilder sb) {
        sb.append(transformClassName(enumclass.getName()));
        for (Object v : EnumSet.allOf(enumclass)) {
            sb.append(" ").append(v);
        }
    }

    protected boolean ready = false;

    public boolean isReady() {
        return ready;
    }
}
