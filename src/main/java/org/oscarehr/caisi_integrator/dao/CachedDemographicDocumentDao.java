package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicDocumentDao extends AbstractDao<CachedDemographicDocument>
{

	public CachedDemographicDocumentDao()
	{
		super(CachedDemographicDocument.class);
	}

	public List<CachedDemographicDocument> findByFacilityIdAndDemographicId(Integer facilityId, Integer demographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicDocument> results = query.getResultList();

		return results;
	}
}