package org.tools.hqlbuilder.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtils {
    public static Session newSession(SessionFactory sessionFactory) {
        return sessionFactory.openSession();
    }
}
