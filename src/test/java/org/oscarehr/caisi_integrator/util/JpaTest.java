package org.oscarehr.caisi_integrator.util;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.Test;

public class JpaTest
{
	@Test
	public void entityManagerFactoryTest()
	{
		EntityManagerFactory entityManagerFactory=(EntityManagerFactory)SpringUtils.getBean("entityManagerFactory");
		EntityManager entityManager=entityManagerFactory.createEntityManager();
		assertNotNull(entityManager);
		
		Query query=entityManager.createNativeQuery("select now()", String.class);
		String result=(String)query.getSingleResult();
		assertNotNull(result);
	}	
}
