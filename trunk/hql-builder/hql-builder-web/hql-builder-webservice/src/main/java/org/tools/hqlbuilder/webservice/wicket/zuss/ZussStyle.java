package org.tools.hqlbuilder.webservice.wicket.zuss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;

public class ZussStyle implements Serializable {
    private static final long serialVersionUID = -2566996070037321836L;

    protected LinkedHashMap<String, String> style = new LinkedHashMap<String, String>();

    protected Long lastModified = null;

    protected String stylePath = WicketCSSRoot.class.getPackage().getName().replace('.', '/') + '/' + "settings.zuss";

    public Map<String, String> getStyling() {
        synchronized (style) {
            if (lastModified == null) {
                style.clear();
                lastModified = System.currentTimeMillis();

                URL resource = getClass().getClassLoader().getResource(stylePath);
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(resource.openStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            style.put(parts[0].trim(), parts[1].trim());
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception ex) {
                            //
                        }
                    }
                }
            }
            return Collections.unmodifiableMap(this.style);
        }
    }

    public void printStyling(PrintStream out) {
        if (style != null) {
            for (Map.Entry<String, String> entry : style.entrySet()) {
                out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public Long getLastModified() {
        if (lastModified == null) {
            getStyling();
        }
        return lastModified;
    }

    public String getStylePath() {
        return this.stylePath;
    }

    public void resetStyle() {
        synchronized (style) {
            this.style.clear();
            this.lastModified = null;
        }
    }

    public void setStyle(String key, String value) {
        synchronized (style) {
            if (this.style.containsKey(key)) {
                if (!this.style.get(key).equals(value)) {
                    this.style.put(key, value);
                    this.lastModified = System.currentTimeMillis();
                }
            }
        }
    }

    public String getStyle(String key) {
        synchronized (style) {
            return this.style.get(key);
        }
    }

    public void setStyle(LinkedHashMap<String, String> style) {
        synchronized (style) {
            for (Map.Entry<String, String> entry : style.entrySet()) {
                setStyle(entry.getKey(), entry.getValue());
            }
        }
    }

    public Iterable<String> keys() {
        synchronized (style) {
            return style.keySet();
        }
    }
}
