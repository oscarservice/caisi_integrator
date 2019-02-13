package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class FacilityDao extends AbstractDao<Facility>
{
    public FacilityDao()
	{
		super(Facility.class);
	}

	public Facility findByName(String name)
	{
		name = MiscUtils.validateAndNormaliseUserName(name);

		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.name=?1");
		query.setParameter(1, name);

		return(getSingleResultOrNull(query));
	}

	public List<Facility> findAll()
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x");
		
		@SuppressWarnings("unchecked")
		List<Facility> results=query.getResultList();
		
		return(results);
	}
}
