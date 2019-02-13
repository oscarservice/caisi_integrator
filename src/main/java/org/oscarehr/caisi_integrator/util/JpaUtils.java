package org.oscarehr.caisi_integrator.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class JpaUtils
{
	private static EntityManagerFactory entityManagerFactory = (EntityManagerFactory) SpringUtils.getBean("entityManagerFactory");

	/**
	 * This method will close the entity manager.
	 * Any active transaction will be rolled back.
	 */
	public static void close(EntityManager entityManager)
	{
		EntityTransaction tx = entityManager.getTransaction();
		if (tx != null && tx.isActive()) tx.rollback();
		entityManager.close();
	}

	public static EntityManager createEntityManager()
	{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		return(entityManager);
	}
}
