package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicDrugDao extends AbstractDao<CachedDemographicDrug>
{
	public CachedDemographicDrugDao()
	{
		super(CachedDemographicDrug.class);
	}

	public List<CachedDemographicDrug> findByFacilityAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicDrug> results = query.getResultList();

		return(results);
	}
}
