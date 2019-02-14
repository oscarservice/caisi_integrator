package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicHL7LabResultDao extends AbstractDao<CachedDemographicHL7LabResult>
{

	public CachedDemographicHL7LabResultDao()
	{
		super(CachedDemographicHL7LabResult.class);
	}

	/**
	 * Returns a list of cached allergies for the specified patient at a specific facility.
	 * @param facilityId
	 * @param demographicId
	 * @return
	 */
	public List<CachedDemographicHL7LabResult> findByFacilityIdAndDemographicId(Integer facilityId, Integer demographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicHL7LabResult> results = query.getResultList();

		return results;
	}

}
