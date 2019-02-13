package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedMeasurementMapDao extends AbstractDao<CachedMeasurementMap>
{
	public CachedMeasurementMapDao()
	{
		super(CachedMeasurementMap.class);
	}

	public List<CachedMeasurementMap> findByIdentCode(String identCode)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where identCode=?1", modelClass);
		query.setParameter(1, identCode);

		@SuppressWarnings("unchecked")
		List<CachedMeasurementMap> results = query.getResultList();

		return(results);
	}

	public List<CachedMeasurementMap> findByLoincCode(String loincCode)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where loincCode=?1", modelClass);
		query.setParameter(1, loincCode);

		@SuppressWarnings("unchecked")
		List<CachedMeasurementMap> results = query.getResultList();

		return(results);
	}
}
