package org.tools.hqlbuilder.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.jhaws.common.web.resteasy.RestResource;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.DelegatingHqlService;
import org.tools.hqlbuilder.common.HqlService;

/**
 * @author Jurgen
 */
public class HqlServiceClientImpl extends DelegatingHqlService implements HqlServiceClient {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlServiceClientImpl.class);

    public static final String NEWLINE = "\n";

    private HqlService hqlService;

    private String serviceUrl;

    private String[] keywordGroups = {
            "right outer join",
            "left outer join",
            "inner join",
            "from",
            "where",
            "having",
            "and",
            "or",
            "group by",
            "order by" };

    /** when cleaning up HQL: replace key by value */
    private Map<String, String> hqlReplacers = new HashMap<>();

    public HqlService getHqlService() {
        return hqlService;
    }

    public void setHqlService(HqlService hqlService) {
        this.hqlService = hqlService;
    }

    /**
     * @see org.tools.hqlbuilder.client.HqlServiceClient#getServiceUrl()
     */
    @Override
    public String getServiceUrl() {
        return this.serviceUrl;
    }

    /**
     * @see org.tools.hqlbuilder.client.HqlServiceClient#setServiceUrl(java.lang.String)
     */
    @Override
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * @see org.tools.hqlbuilder.common.DelegatingHqlService#getDelegate()
     */
    @Override
    public HqlService getDelegate() {
        return hqlService;
    }

    /**
     * @see org.tools.hqlbuilder.client.HqlServiceClient#cleanupSql(java.lang.String, java.lang.String[], java.lang.String[][], boolean, boolean,
     *      boolean)
     */
    @Override
    public String cleanupSql(String sqlString, String[] queryReturnAliases, String[][] scalarColumnNames, boolean replaceProperties,
            boolean formatLines, boolean removeReplacers) {
        if (sqlString == null) {
            return "";
        }

        // kolom alias (kan enkel maar wanneer de query al is omgezet, dus de tweede maal dat deze methode wordt opgeroepen-
        if (queryReturnAliases != null) {
            for (int i = 0; i < queryReturnAliases.length; i++) {
                String queryReturnAlias = queryReturnAliases[i];
                try {
                    String scalarColumnName = scalarColumnNames[i][0];
                    try {
                        // nummers worden vervangen door 'kolom${nummer}' want nummer alleen wordt niet aanvaard
                        Long.parseLong(queryReturnAlias);
                        String newAlias = queryReturnAlias.replace('.', ' ').replace('(', ' ').replace(')', ' ').trim().replace(' ', '_');
                        logger.trace(": " + scalarColumnName + " >> " + queryReturnAlias + " >> " + newAlias);
                        sqlString = sqlString.replace(scalarColumnName, newAlias);
                    } catch (NumberFormatException ex) {
                        logger.trace(": " + scalarColumnName + " >> " + queryReturnAlias);
                        sqlString = sqlString.replace(scalarColumnName, queryReturnAlias);
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    //
                }
            }
        }

        // maakt replacers aan
        HashMap<String, String> replacers = new HashMap<>();

        // vervang tabel_?_?_ door tabelnamen in "from ..." en "... join ..."
        // vervang replacers
        {
            String prefix = "((from)|(join)|(,))";
            String joinfromgroup = "( ([a-zA-Z0-9_]+) ([a-zA-Z0-9_]+))";
            Matcher matcher = Pattern.compile(prefix + joinfromgroup, Pattern.CASE_INSENSITIVE).matcher(sqlString);
            int startpos = 0;

            while (matcher.find(startpos)) {
                String replacing = matcher.group(7);

                if ("when".equals(replacing)) {
                    startpos++;
                    continue;
                }

                String replaceBy = matcher.group(6);

                for (Map.Entry<String, String> hqlReplacer : hqlReplacers.entrySet()) {
                    if (replaceBy.contains(hqlReplacer.getKey())) {
                        logger.trace("-> " + replaceBy + " >> " + replaceBy.replace(hqlReplacer.getKey(), hqlReplacer.getValue()));
                        replaceBy = replaceBy.replace(hqlReplacer.getKey(), hqlReplacer.getValue());
                    }
                }

                @SuppressWarnings("deprecation")
                int existing = CollectionUtils.cardinality(replaceBy, replacers.values());

                if (existing > 0) {
                    logger.trace("-> " + replaceBy + " >> " + replaceBy + (existing + 1));
                    replaceBy = replaceBy + (existing + 1);
                }

                logger.trace("- " + replacing + " >> " + replaceBy);
                replacers.put(replacing, replaceBy);

                startpos = matcher.end();
            }
        }

        // vervang (1) door (2) om geen dubbels te hebben
        // (1) tabel_?_?_=tabelnaamY EN tabel_?_=tabelnaamX
        // (2) tabel_?_?_=tabelnaamX_tabelnaamY EN tabel_?_=tabelnaamX
        List<String> hqlReplacerMap = new ArrayList<>();

        for (Map.Entry<String, String> replacer : replacers.entrySet()) {
            for (Map.Entry<String, String> replacerOther : replacers.entrySet()) {
                if (!replacer.getKey().equals(replacerOther.getKey()) && replacer.getKey().startsWith(replacerOther.getKey())) {
                    String newvalue = replacerOther.getValue() + "_" + replacer.getValue();

                    // oracle heeft 30 len limiet
                    if (newvalue.length() > 30) {
                        newvalue = newvalue.substring(0, 30);
                    }

                    logger.trace("* " + replacer + " EN " + replacerOther + " >> " + replacer.getValue() + "=" + newvalue);
                    replacer.setValue(newvalue);

                    hqlReplacerMap.add(newvalue);
                }
            }
        }

        // sorteer replacers op langste eerst
        List<String> keys = new ArrayList<>(replacers.keySet());
        Collections.sort(keys, (o1, o2) -> {
            if (o1.length() < o2.length()) {
                return 1;
            } else if (o1.length() > o2.length()) {
                return -1;
            } else {
                return 0;
            }
        });

        // vervang nu replacers
        for (String key : keys) {
            String value = replacers.get(key);
            logger.trace("+ " + key + " > " + value);
            sqlString = sqlString.replace(key, value);
        }

        // vervang kolomnamen
        if (replaceProperties) {
            Matcher matcher = Pattern.compile("(( )([^ ]+)( as )([a-zA-Z0-9_]+))", Pattern.CASE_INSENSITIVE).matcher(sqlString);

            while (matcher.find()) {
                String newvalue = matcher.group(3).replace('.', ' ').replace('(', ' ').replace(')', ' ').trim().replace(' ', '_');

                // oracle heeft 30 len limiet
                if (newvalue.length() > 30) {
                    newvalue = newvalue.substring(0, 30);
                }

                newvalue = " " + matcher.group(3) + " as " + newvalue;
                String group = matcher.group();

                try {
                    logger.trace("/ " + group + " > " + newvalue);
                    sqlString = sqlString.replaceAll("\\Q" + group + "\\E", newvalue);
                } catch (Exception ex) {
                    logger.warn("ERROR: " + ex);
                }
            }
        }

        logger.debug(sqlString);

        if (formatLines) {
            sqlString = makeMultiline(sqlString);
        }

        sqlString = removeBlanks(sqlString);

        @SuppressWarnings("unused")
        String[] sqlStringParts = sqlString.split(getNewline());

        String[] lines = sqlString.split(getNewline());

        if (removeReplacers) {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                boolean keep = true;
                for (String hqlReplacer : hqlReplacers.values()) {
                    if (line.contains(hqlReplacer)) {
                        keep &= keep(hqlReplacer, hqlReplacerMap, lines, i, line);
                    }
                }
                // zal verwijderd worden
                if (!keep) {
                    lines[i] = null;
                }
            }
        }

        StringBuilder anew = new StringBuilder();

        for (String line : lines) {
            if (line != null) {
                anew.append(line).append(getNewline());
            }
        }

        sqlString = anew.toString();

        // logger.debug(sqlString);

        try {
            sqlString = CommonUtils.call(Class.forName("org.hibernate.jdbc.util.BasicFormatterImpl").newInstance(), "format", String.class,
                    sqlString);
        } catch (Throwable ex) {
            if (!warn1) {
                warn1 = true;
                logger.warn("{}", String.valueOf(ex));
            }
        }

        try {
            sqlString = CommonUtils.call(Class.forName("org.hibernate.engine.jdbc.internal.BasicFormatterImpl").newInstance(), "format", String.class,
                    sqlString);
        } catch (Throwable ex) {
            if (!warn2) {
                warn2 = true;
                logger.warn("{}", String.valueOf(ex));
            }
        }

        logger.info(sqlString);

        return sqlString;
    }

    private boolean warn1 = false;

    private boolean warn2 = false;

    /**
     * @see org.tools.hqlbuilder.client.HqlServiceClient#getNewline()
     */
    @Override
    public String getNewline() {
        return NEWLINE;
    }

    /**
     * @see org.tools.hqlbuilder.client.HqlServiceClient#makeMultiline(java.lang.String)
     */
    @Override
    public String makeMultiline(String string) {
        for (String kw : keywordGroups) {
            string = lineformat1replace(string, kw);
        }

        // (?i) : case insensitive
        // *+ : zero or more, possessive
        // $1 : replace with value of first group
        string = string.replaceAll("(?i) (ASC)[ ]*+,[ ]*+", " $1," + getNewline());
        string = string.replaceAll("(?i) (DESC)[ ]*+,[ ]*+", " $1," + getNewline());

        if (string.startsWith("select ")) {
            string = "select " + getNewline() + string.substring("select ".length());
        }

        String split = "from ";

        String[] parts = string.split(split);

        StringBuilder sb = new StringBuilder(parts[0].replaceAll(", ", "," + getNewline())).append(split);

        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i]);

            if (i < parts.length - 1) {
                sb.append(split);
            }
        }

        String result = sb.toString();

        if (result.trim().equals("from")) {
            return string;
        }

        String tmp = sb.toString();
        return tmp;
    }

    /**
     * @see org.tools.hqlbuilder.client.HqlServiceClient#removeBlanks(java.lang.String)
     */
    @Override
    public String removeBlanks(String string) {
        return CommonUtils.removeUnnecessaryWhiteSpaces(string);
    }

    private String lineformat1replace(String string, String splitter) {
        Matcher matcher = Pattern.compile(" " + splitter + " ", Pattern.CASE_INSENSITIVE).matcher(string);
        String CTE = "AAAAAAAAAAA";
        String replaceAll = matcher.replaceAll(" " + getNewline() + CTE + " ").replaceAll(CTE, splitter);
        return replaceAll;
    }

    private boolean keep(String hqlReplacerValue, List<String> hqlReplacerValueX, String[] lines, int i, String line) {
        if (!line.contains(hqlReplacerValue)) {
            return true;
        }
        for (String hqlReplacerValueXEl : hqlReplacerValueX) {
            if (line.contains(hqlReplacerValueXEl)) {
                for (int j = 0; j < lines.length; j++) {
                    if (i != j) {
                        if (lines[j] != null && lines[j].contains(hqlReplacerValueXEl)) {
                            // deze mag niet vervangen worden
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Map<String, String> getHqlReplacers() {
        return this.hqlReplacers;
    }

    public void setHqlReplacers(Map<String, String> hqlReplacers) {
        this.hqlReplacers = hqlReplacers;
    }

    /** info about purpose of {@link #hqlReplacers} and {@link #cleanupSql(String, String[], String[][], boolean, boolean, boolean)} */
    public static void main(String[] args) {
        new HqlServiceClientImpl().cleanupSqlTest();
    }

    public void cleanupSqlTest() {
        String q = "select Table0_.Id as Id4_0_ from Table Table0_ inner join SuperTable SuperTable0_1_ on Table0_.Id=SuperTable0_1_.Id inner join OtherTable OtherTable1_ on Table0_.OtherTableId=OtherTable1_.Id inner join SuperTable SuperTable1_1_ on OtherTable1_.Id=SuperTable1_1_.Id inner join ParentTable ParentTable3_1_ on ParentTable3_1_.Id=OtherTable1_.Id";
        HqlServiceClientImpl sq = new HqlServiceClientImpl();
        sq.setHqlReplacers(new HashMap<>(Collections.singletonMap("SuperTable", "STx")));
        logger.debug(q);
        String qq;
        {
            logger.debug("==================================");
            qq = sq.cleanupSql(q, null, null, false, true, false);
            logger.debug(qq);
        }
        {
            logger.debug("==================================");
            qq = sq.cleanupSql(q, null, null, true, true, false);
            logger.debug(qq);
        }
        {
            logger.debug("==================================");
            qq = sq.cleanupSql(q, null, null, true, true, true);
            logger.debug(qq);
        }
        sq.getHqlReplacers().put("ParentTable", "PTx");
        {
            logger.debug("==================================");
            qq = sq.cleanupSql(q, null, null, true, true, false);
            logger.debug(qq);
        }
    }

    @Override
    public String getHibernateHelpURL() {
        return this.hqlService.getHibernateHelpURL().replace(RestResource.INTERNET_SHORTCUT_URL, "");
    }

    @Override
    public String getHqlHelpURL() {
        return this.hqlService.getHqlHelpURL().replace(RestResource.INTERNET_SHORTCUT_URL, "");
    }

    @Override
    public String getLuceneHelpURL() {
        return this.hqlService.getLuceneHelpURL().replace(RestResource.INTERNET_SHORTCUT_URL, "");
    }
}
