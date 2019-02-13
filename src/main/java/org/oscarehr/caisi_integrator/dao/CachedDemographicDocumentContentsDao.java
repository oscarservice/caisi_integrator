package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicDocumentContentsDao extends AbstractDao<CachedDemographicDocumentContents>
{

	public CachedDemographicDocumentContentsDao()
	{
		super(CachedDemographicDocumentContents.class);
	}

	public List<CachedDemographicDocumentContents> findByFacilityIdAndDemographicId(Integer facilityId, Integer demographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, facilityId);
		query.setParameter(2, demographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicDocumentContents> results = query.getResultList();

		return(results);
	}
}