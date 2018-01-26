package org.tools.hqlbuilder.client;

import java.util.Collections;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

public class CleanupSqlTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CleanupSqlTest.class);

    /** info about purpose of {@link #hqlReplacers} and {@link #cleanupSql(String, String[], String[][], boolean, boolean, boolean)} */
    public static void main(String[] args) {
        new CleanupSqlTest().cleanupSqlTest();
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
}
