package org.tools.hqlbuilder.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.ClassMetadata;
import org.tools.hqlbuilder.common.interfaces.Information;

public abstract class LuceneInformation implements Information {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(LuceneInformation.class);

    protected static final Version LUCENE_VERSION = Version.LATEST;

    protected static final Store STORE = Store.YES;

    public static final String FIELD = "field";

    public static final String CLASS = "class";

    public static final String TYPE = "type";

    public static final String DATA = "data";

    public static final String NAME = "name";

    protected boolean persistent = false;

    protected final Analyzer analyzer = new StandardAnalyzer();

    protected Directory index;

    protected boolean ready = false;

    public LuceneInformation() {
        super();
    }

    protected abstract void create(IndexWriter writer, SessionFactory sessionFactory, String classname, ClassMetadata classMetadata)
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, CorruptIndexException, IOException;

    @Override
    public void init(String id, Object sf) throws IOException, UnsupportedOperationException {
        SessionFactory sessionFactory = (SessionFactory) sf;

        Map<String, ?> allClassMetadata = new MetadataResolver().getAllClassMetadata(sessionFactory);

        if (this.persistent) {
            this.index = new NIOFSDirectory(Paths.get(System.getProperty("user.home"), "hqlbuilder", "lucene",
                    LuceneInformation.LUCENE_VERSION.toString(), id.replaceAll("[^A-Za-z0-9]", "")));
        } else {
            this.index = new RAMDirectory();
        }
        IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        IndexWriter w = new IndexWriter(this.index, config);

        try {
            for (Map.Entry<String, ?> i : allClassMetadata.entrySet()) {
                if (i.getValue() instanceof AbstractEntityPersister) {
                    AbstractEntityPersister p = (AbstractEntityPersister) i.getValue();
                    this.create(w, sessionFactory, i.getKey(), p.getClassMetadata());
                } else {
                    throw new UnsupportedOperationException(i.getValue().getClass().getName());
                }
            }
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new IOException(ex.getMessage());
        }

        w.close();

        this.ready = true;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public boolean isReady() {
        return this.ready;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void printEnumValues(Class<? extends Enum> enumclass, StringBuilder sb) {
        sb.append(this.transformClassName(enumclass.getName()));
        for (Object v : EnumSet.allOf(enumclass)) {
            sb.append(" ").append(v);
        }
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

    @Override
    public List<String> search(String text, String typeName, int hitsPerPage) {
        Query q;
        try {
            if (typeName != null) {
                BooleanQuery bq = new BooleanQuery();
                Query query = new StandardQueryParser(this.analyzer).parse(text, LuceneInformation.DATA);
                bq.add(query, BooleanClause.Occur.MUST);
                bq.add(new TermQuery(new Term(LuceneInformation.TYPE, typeName)), BooleanClause.Occur.MUST);
                q = bq;
            } else {
                q = new StandardQueryParser(this.analyzer).parse(text + "*", LuceneInformation.DATA);
            }
        } catch (QueryNodeException e) {
            throw new RuntimeException(e.getMessage());
        }

        List<String> results = new ArrayList<String>();
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(this.index));
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            LuceneInformation.logger.info("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                LuceneInformation.logger.debug((i + 1) + ". " + d.get(LuceneInformation.NAME));
                String stringValue = d.getField(LuceneInformation.DATA).stringValue();
                LuceneInformation.logger.debug(stringValue);
                results.add(d.get(LuceneInformation.NAME));
            }

            // searcher.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return results;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    protected Object transformClassName(String name) {
        try {
            return name.substring(1 + name.substring(0, name.lastIndexOf('.')).lastIndexOf('.')).replaceAll("\\.", " ");
        } catch (IndexOutOfBoundsException ex) {
            return name.replaceAll("\\.", " ");
        }
    }
}
