package org.tools.hqlbuilder.client;

import org.tools.hqlbuilder.common.HqlService;

/**
 * @author Jurgen
 */
public interface HqlServiceClient extends HqlService {
    public abstract void setServiceUrl(String serviceUrl);

    public abstract String getServiceUrl();

    public abstract String getNewline();

    public abstract void log(Object message);

    public abstract String cleanupSql(String sqlString, String[] queryReturnAliases, String[][] scalarColumnNames, boolean replaceProperties,
            boolean formatLines, boolean removeADOJoins);

    public abstract String makeMultiline(String string);

    public abstract String removeBlanks(String string);
}
