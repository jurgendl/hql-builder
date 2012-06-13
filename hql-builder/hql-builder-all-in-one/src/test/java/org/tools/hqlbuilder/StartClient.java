package org.tools.hqlbuilder;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.springframework.beans.PropertyAccessorFactory;
import org.tools.hqlbuilder.client.PathResolver;
import org.tools.hqlbuilder.client.PathResolver.TreeNode;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.QueryParameter;

public class StartClient {
    public static void main(String[] args) {
        System.out.println("1");
        HqlServiceClient hqlServiceClient = (HqlServiceClient) new org.springframework.context.support.ClassPathXmlApplicationContext(
                "org/tools/hqlbuilder/applicationContext.xml").getBean("hqlServiceClient");
        for (Object o : hqlServiceClient.getNamedQueries().entrySet()) {
            System.out.println(o);
        }
        System.out.println(hqlServiceClient.getConnectionInfo());
        System.out.println(hqlServiceClient.getReservedKeywords());
        SortedSet<String> classes = hqlServiceClient.getClasses();
        System.out.println(classes);
        Iterator<String> cit = classes.iterator();
        System.out.println(hqlServiceClient.getProperties(cit.next()));
        String hqlshort = "from " + cit.next() + " o ";
        String hqllong = "from " + cit.next() + " o where o.id=?";
        System.out.println(hqlServiceClient.getSqlForHql(hqlshort));
        List<QueryParameter> p = hqlServiceClient.findParameters(hqllong);
        System.out.println(p);
        Object model = hqlServiceClient.execute(hqlshort, 1).getResults().iterator().next();
        Object id = PropertyAccessorFactory.forBeanPropertyAccess(model).getPropertyValue("id");
        p.get(0).setValue(id);
        System.out.println(hqlServiceClient.execute(hqllong, 50, p));
        try {
            HibernateWebResolver meta = hqlServiceClient.getHibernateWebResolver();
            PathResolver pathResolver = new PathResolver(meta.getNode(cit.next()), meta.getNode(cit.next()));
            for (TreeNode o : pathResolver.findPathNoEx()) {
                System.out.println(o);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
