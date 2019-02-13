package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedMeasurementExtDao extends AbstractDao<CachedMeasurementExt>
{
	public CachedMeasurementExtDao()
	{
		super(CachedMeasurementExt.class);
	}

	public List<CachedMeasurementExt> findByMeasurementId(Integer measurementId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where measurementId=?1", modelClass);
		query.setParameter(1, measurementId);

		@SuppressWarnings("unchecked")
		List<CachedMeasurementExt> results = query.getResultList();

		return(results);
	}
}
