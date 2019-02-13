package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicLabResultDao extends AbstractDao<CachedDemographicLabResult>
{

	public CachedDemographicLabResultDao()
	{
		super(CachedDemographicLabResult.class);
	}

	/**
	 * Returns a list of cached allergies for the specified patient at a specific facility.
	 * @param facilityId
	 * @param demographicId
	 * @return
	 */
	public List<CachedDemographicLabResult> findByFacilityIdAndDemographicId(Integer facilityId, Integer demographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicLabResult> results = query.getResultList();

		return results;
	}

}