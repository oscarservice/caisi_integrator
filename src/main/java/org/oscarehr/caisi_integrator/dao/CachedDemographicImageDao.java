package org.oscarehr.caisi_integrator.dao;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicImageDao extends AbstractDao<CachedDemographicImage>
{
	public CachedDemographicImageDao()
	{
		super(CachedDemographicImage.class);
	}
}
