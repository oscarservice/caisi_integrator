package org.oscarehr.caisi_integrator.dao;

import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.oscarehr.caisi_integrator.util.QueueCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicConsentDao extends AbstractDao<CachedDemographicConsent>
{
	private static QueueCache<FacilityIdIntegerCompositePk, CachedDemographicConsent> latestConsents=new QueueCache<FacilityIdIntegerCompositePk, CachedDemographicConsent>(4, 100, DateUtils.MILLIS_PER_HOUR);
	
	@Autowired
	private DemographicLinkDao demographicLinkDao=null;
	
	public CachedDemographicConsentDao()
	{
		super(CachedDemographicConsent.class);
	}
	
	@Override
	public void persist(CachedDemographicConsent o)
	{
		super.persist(o);
		setCachedEntry(o.getFacilityDemographicPk(), null);
	}

	@Override
	public void remove(CachedDemographicConsent o)
	{
		setCachedEntry(o.getFacilityDemographicPk(), null);
		super.remove(o);
	}
	
	public CachedDemographicConsent findLatestConsentFromAllLinkedClients(FacilityIdIntegerCompositePk facilityDemographicPk)
	{
		// check the cache
		CachedDemographicConsent consent=latestConsents.get(facilityDemographicPk);
		
		// find latest entry
		Set<FacilityIdIntegerCompositePk> linked=demographicLinkDao.getAllLinkedDemographics(facilityDemographicPk, false);
		for (FacilityIdIntegerCompositePk tempPk : linked)
		{
			CachedDemographicConsent tempConsent=find(tempPk);
			if (tempConsent!=null)
			{
				if (consent==null || tempConsent.getCreatedDate().after(consent.getCreatedDate()))
				{
					consent=tempConsent;
				}
			}
		}
			
		// put it in the cache
		if (consent!=null)
		{
			for (FacilityIdIntegerCompositePk tempPk : linked)
			{
				latestConsents.put(tempPk, consent);
			}
		}

		return(consent);
	}
	
	/**
	 * This method can be used to flush the cache entry by setting it to null or
	 * you can use it to explicity set the cached entry if you "know" you're the
	 * latest consent right now.
	 */
	private static void setCachedEntry(FacilityIdIntegerCompositePk facilityDemographicPk, CachedDemographicConsent cachedDemographicConsent)
	{
		latestConsents.put(facilityDemographicPk, cachedDemographicConsent);
	}
}
