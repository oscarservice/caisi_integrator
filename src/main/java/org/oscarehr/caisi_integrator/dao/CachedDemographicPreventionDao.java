package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicPreventionDao extends AbstractDao<CachedDemographicPrevention>
{
	public CachedDemographicPreventionDao()
	{
		super(CachedDemographicPrevention.class);
	}

    /**
     * This method will get preventions for the given demographic only, non linked.
     */
    public List<CachedDemographicPrevention> findByFacilityIdAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
    {
    	Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicPrevention> results=query.getResultList();
		
		return(results);
    }
}
