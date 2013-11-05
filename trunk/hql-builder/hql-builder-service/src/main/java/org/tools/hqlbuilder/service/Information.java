package org.tools.hqlbuilder.service;

import java.io.IOException;
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
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.JoinedSubclassEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

public abstract class Information {
    public static final Version LUCENE_VERSION = Version.LUCENE_35;

    public static final String FIELD = "field";

    public static final String CLASS = "class";

    public static final String TYPE = "type";

    public static final String DATA = "data";

    public static final String NAME = "name";

    protected static final Store STORE_DATA = Field.Store.YES;

    protected final Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);

    protected final Directory index;

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

    protected abstract void create(IndexWriter writer, SessionFactory sessionFactory, String classname, ClassMetadata classMetadata)
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, CorruptIndexException, IOException;

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

    protected String proper(String name) {
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

    protected Object transformClassName(String name) {
        try {
            return name.substring(1 + name.substring(0, name.lastIndexOf('.')).lastIndexOf('.')).replaceAll("\\.", " ");
        } catch (IndexOutOfBoundsException ex) {
            return name.replaceAll("\\.", " ");
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void printEnumValues(Class<? extends Enum> enumclass, StringBuilder sb) {
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
