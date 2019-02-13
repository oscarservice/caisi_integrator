package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedMeasurementTypeDao extends AbstractDao<CachedMeasurementType>
{
	public CachedMeasurementTypeDao()
	{
		super(CachedMeasurementType.class);
	}

	public List<CachedMeasurementType> findByType(String type)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where type=?1", modelClass);
		query.setParameter(1, type);

		@SuppressWarnings("unchecked")
		List<CachedMeasurementType> results = query.getResultList();

		return(results);
	}
}
