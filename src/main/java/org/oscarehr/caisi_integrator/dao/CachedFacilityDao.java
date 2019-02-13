package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedFacilityDao extends AbstractDao<CachedFacility>
{
	public CachedFacilityDao()
	{
		super(CachedFacility.class);
	}
	
	public CachedFacility findByFacilityId(int integratorFacilityId)
	{
		return(find(integratorFacilityId));
	}
	
    public List<CachedFacility> findAll()
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x");
		
		@SuppressWarnings("unchecked")
		List<CachedFacility> results=query.getResultList();
		
		return(results);
	}
    
}
