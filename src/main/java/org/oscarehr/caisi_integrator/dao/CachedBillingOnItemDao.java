package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedBillingOnItemDao extends AbstractDao<CachedBillingOnItem>
{
	public CachedBillingOnItemDao()
	{
		super(CachedBillingOnItem.class);
	}

	public List<CachedBillingOnItem> findByFacilityAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedBillingOnItem> results = query.getResultList();

		return(results);
	}

	public List<CachedBillingOnItem> findByFacilityAndProviderId(Integer integratorFacilityId, String caisiProviderId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiProviderId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiProviderId);

		@SuppressWarnings("unchecked")
		List<CachedBillingOnItem> results = query.getResultList();

		return(results);
	}

	public List<CachedBillingOnItem> findByDx(String dx)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where dx=?1 or dx1=?1 or dx2=?1", modelClass);
		query.setParameter(1, dx);

		@SuppressWarnings("unchecked")
		List<CachedBillingOnItem> results = query.getResultList();

		return(results);
	}
}
