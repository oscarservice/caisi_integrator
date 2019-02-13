package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedProgramDao extends AbstractDao<CachedProgram>
{
	public CachedProgramDao()
	{
		super(CachedProgram.class);
	}
    
    public List<CachedProgram> findByFacilityId(Integer integratorFacilityId)
	{
		Query query = entityManager.createNativeQuery("select * from "+modelClass.getSimpleName()+" where integratorFacilityId=?1", modelClass);
		query.setParameter(1, integratorFacilityId);
		
		@SuppressWarnings("unchecked")
		List<CachedProgram> results=query.getResultList();
		
		return(results);
	}	

    public List<CachedProgram> findAll()
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x");
		
		@SuppressWarnings("unchecked")
		List<CachedProgram> results=query.getResultList();
		
		return(results);
	}

	public List<CachedProgram> findByName(String name)
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.name=?1");
		query.setParameter(1, name);
		
		@SuppressWarnings("unchecked")
		List<CachedProgram> results=query.getResultList();
		
		return(results);
	}	
    
}
