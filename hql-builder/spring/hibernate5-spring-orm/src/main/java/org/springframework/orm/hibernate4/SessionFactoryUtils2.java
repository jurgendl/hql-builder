package org.springframework.orm.hibernate4;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

public class SessionFactoryUtils2 extends org.springframework.orm.hibernate4.SessionFactoryUtils {
	/**
	 * Apply the current transaction timeout, if any, to the given Hibernate Criteria object.
	 * 
	 * @param criteria the Hibernate Criteria object
	 * @param sessionFactory Hibernate SessionFactory that the Criteria was created for
	 * @see org.hibernate.Criteria#setTimeout
	 */
	public static void applyTransactionTimeout(Criteria criteria, SessionFactory sessionFactory) {
		Assert.notNull(criteria, "No Criteria object specified");
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		if (sessionHolder != null && sessionHolder.hasTimeout()) {
			criteria.setTimeout(sessionHolder.getTimeToLiveInSeconds());
		}
	}

	/**
	 * Apply the current transaction timeout, if any, to the given Hibernate Query object.
	 * 
	 * @param query the Hibernate Query object
	 * @param sessionFactory Hibernate SessionFactory that the Query was created for (may be {@code null})
	 * @see Query#setTimeout
	 */
	public static void applyTransactionTimeout(Query query, SessionFactory sessionFactory) {
		Assert.notNull(query, "No Query object specified");
		if (sessionFactory != null) {
			SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
			if (sessionHolder != null && sessionHolder.hasTimeout()) {
				query.setTimeout(sessionHolder.getTimeToLiveInSeconds());
			}
		}
	}

	public static Session getSession(SessionFactory sessionFactory, boolean allowCreate) {
		return sessionFactory.getCurrentSession();
	}
}
