package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class SiteUserDao extends AbstractDao<SiteUser>
{
    public SiteUserDao()
	{
		super(SiteUser.class);
	}

	public SiteUser findByName(String name)
	{
		name = MiscUtils.validateAndNormaliseUserName(name);

		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.name=?1");
		query.setParameter(1, name);

		return(getSingleResultOrNull(query));
	}
	
    public List<SiteUser> findAll()
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x");
		
		@SuppressWarnings("unchecked")
		List<SiteUser> results=query.getResultList();
		
		return(results);
	}
    
	public void createInitialEntry()
	{
		SiteUser siteUser=new SiteUser();
		siteUser.setName("admin");
		siteUser.setPassword("admin");
		persist(siteUser);
	}

}
