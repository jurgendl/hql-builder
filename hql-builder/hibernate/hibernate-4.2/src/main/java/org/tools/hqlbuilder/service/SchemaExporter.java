package org.tools.hqlbuilder.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.CommonUtils;

public class SchemaExporter {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(SchemaExporter.class);

    public String export(Configuration configuration) {
        try {
            SchemaExport export = new SchemaExport(configuration);
            export.setDelimiter(";");
            export.setFormat(true);
            export.setHaltOnError(false);
            try {
                File tmp = File.createTempFile("create", "sql");
                export.setOutputFile(tmp.getAbsolutePath());
                export.execute(true, false, false, true);
                return new String(CommonUtils.read(new FileInputStream(tmp)));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (RuntimeException ex) {
            logger.error("{}", ex);
            return null;
        }
    }
}
