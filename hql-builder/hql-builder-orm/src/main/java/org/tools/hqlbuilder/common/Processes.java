package org.tools.hqlbuilder.common;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class Processes {
    public static interface Processors extends Consumer<String> {
        public default Processors combine(Processors other) {
            return new CombinedProcessor(this, other);
        }

        public static class CombinedProcessor implements Processors {
            protected Processors processor1;

            protected Processors processor2;

            public CombinedProcessor(Processors processor1, Processors processor2) {
                this.processor1 = processor1;
                this.processor2 = processor2;
            }

            @Override
            public void accept(String t) {
                processor1.accept(t);
                processor2.accept(t);
            }
        }

        public static class PrintProcessor implements Processors {
            @Override
            public void accept(String t) {
                System.out.println(t);
            }
        }

        public static class ToTextProcessor implements Processors {
            protected StringBuilder sb = new StringBuilder();

            protected String seperator = System.getProperty("line.separator");

            @Override
            public void accept(String t) {
                sb.append(t).append(seperator);
            }

            public String getText() {
                return sb.toString();
            }
        }

        public static class ToLinesProcessor implements Processors {
            protected List<String> lines = new ArrayList<>();

            @Override
            public void accept(String t) {
                lines.add(t);
            }

            public List<String> getLines() {
                return lines;
            }
        }
    }

    public static Predicate<String> notBlankFilter() {
        return l -> StringUtils.isNotBlank(l);
    }

    private static Stream<String> newFilteredStream(LineIterator lineIterator, Predicate<String> filter) {
        Stream<String> stream = Collections8.stream(lineIterator);
        if (filter != null)
            stream = stream.filter(filter);
        return stream;
    }

    private static LineIterator newLineIterator(Process p) {
        return new LineIterator(p.getInputStream());
    }

    public static <C extends Consumer<String>> C callProcess(List<String> command, Path dir, Predicate<String> filter, C processor)
            throws IOException {
        Process p = startProcess(command, dir);
        try (LineIterator lineIterator = newLineIterator(p)) {
            newFilteredStream(lineIterator, filter).forEach(processor);
        }
        return processor;
    }

    public static Process startProcess(List<String> command, Path dir) throws IOException {
        if (!Files.exists(dir))
            throw new RuntimeException("target directory does not exists");
        if (!Files.isDirectory(dir))
            throw new RuntimeException("target is not a directory");
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir.toFile());
        pb.redirectErrorStream(true);
        return pb.start();
    }

    private static class LineIterator implements Iterator<String>, Closeable {
        protected transient String line;

        protected transient BufferedReader in;

        @SuppressWarnings("unused")
        protected transient boolean openened = true;

        public LineIterator(BufferedReader in) {
            this.in = in;
        }

        public LineIterator(InputStream in) {
            this(in, Charset.defaultCharset());
        }

        public LineIterator(InputStream in, Charset cs) {
            this(new BufferedReader(new InputStreamReader(in, cs)));
        }

        @Override
        public void close() throws IOException {
            this.openened = true;
            BufferedReader br = this.in();
            if (br != null) {
                br.close();
            }
        }

        @Override
        public boolean hasNext() {
            this.optionalRead();
            return this.line != null;
        }

        protected BufferedReader in() throws IOException {
            return this.in;
        }

        @Override
        public String next() {
            this.optionalRead();
            if (this.line == null) {
                throw new NoSuchElementException();
            }
            String tmp = this.line;
            this.line = null;
            return tmp;
        }

        protected void optionalRead() {
            if (this.line == null) {
                try {
                    BufferedReader br = this.in();
                    if (br != null) {
                        this.line = br.readLine();
                        if (this.line == null) {
                            this.close();
                        }
                    }
                } catch (IOException ex) {
                    this.line = null;
                }
            }
        }

        @Override
        public void remove() {
            this.line = null;
        }
    }
}
