package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedEformValueDao extends AbstractDao<CachedEformValue>
{
	public CachedEformValueDao()
	{
		super(CachedEformValue.class);
	}

	public List<CachedEformValue> findByFacilityAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedEformValue> results = query.getResultList();

		return(results);
	}
}
