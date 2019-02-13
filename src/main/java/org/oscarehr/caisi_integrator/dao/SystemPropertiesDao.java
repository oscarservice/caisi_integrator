package org.oscarehr.caisi_integrator.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SystemPropertiesDao
{
	@PersistenceContext
	protected EntityManager entityManager = null;

	public void merge(SystemProperties o)
	{
		entityManager.merge(o);
	}

	public SystemProperties find()
	{
		return(entityManager.find(SystemProperties.class, SystemProperties.SYSTEM_PROPERTY_ID));
	}

	public void createInitialEntry()
	{
		SystemProperties systemProperties=new SystemProperties();
		entityManager.persist(systemProperties);
	}
}
