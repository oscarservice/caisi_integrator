package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDxresearchDao extends AbstractDao<CachedDxresearch>
{
	public CachedDxresearchDao()
	{
		super(CachedDxresearch.class);
	}

	public List<CachedDxresearch> findByFacilityAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedDxresearch> results = query.getResultList();

		return(results);
	}

	public List<CachedDxresearch> findByDxresearchCode(String dxresearchCode)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where dxresearchCode=?1", modelClass);
		query.setParameter(1, dxresearchCode);

		@SuppressWarnings("unchecked")
		List<CachedDxresearch> results = query.getResultList();

		return(results);
	}
}
