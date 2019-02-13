package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicDrugTest extends DaoTestFixtures
{
	private CachedDemographicDao cachedDemographicDao = (CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");
	private CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");
	private CachedDemographicDrugDao cachedDemographicDrugDao=(CachedDemographicDrugDao)SpringUtils.getBean("cachedDemographicDrugDao");
	
	@Test
	public void cachedDemographicDrugTest()
	{
		// --- create ---

		// provider
		FacilityIdStringCompositePk pk5=new FacilityIdStringCompositePk();
		pk5.setIntegratorFacilityId(facility1.getId());
		pk5.setCaisiItemId("624");
		CachedProvider cachedProvider=new CachedProvider();
		cachedProvider.setFacilityIdStringCompositePk(pk5);
		cachedProvider.setFirstName("first name");
		cachedProvider.setLastName("last name");
		cachedProviderDao.persist(cachedProvider);

		// demographic
		FacilityIdIntegerCompositePk pk6=new FacilityIdIntegerCompositePk();
		pk6.setIntegratorFacilityId(facility1.getId());
		pk6.setCaisiItemId(543);
		CachedDemographic cachedDemographic=new CachedDemographic();
		cachedDemographic.setFacilityIdIntegerCompositePk(pk6);
		cachedDemographic.setFirstName("fn");
		cachedDemographic.setLastName("ln");
		cachedDemographicDao.persist(cachedDemographic);
		
		// drug
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(4523);
		
		CachedDemographicDrug cachedDemographicDrug=new CachedDemographicDrug();
		cachedDemographicDrug.setFacilityIdIntegerCompositePk(pk);
		cachedDemographicDrug.setCaisiDemographicId(543);
		cachedDemographicDrug.setCaisiProviderId("624");
		
		cachedDemographicDrugDao.persist(cachedDemographicDrug);
		
		
		// --- find ---
		CachedDemographicDrug drug1=cachedDemographicDrugDao.find(pk);
		assertEquals(543, drug1.getCaisiDemographicId().intValue());
		assertEquals("624", drug1.getCaisiProviderId());
		
		List<CachedDemographicDrug> drugs=cachedDemographicDrugDao.findByFacilityAndDemographicId(drug1.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), drug1.getCaisiDemographicId());
		assertTrue(drugs.size()>0);
		
		// --- update ---
		// meh, never seen these fail yet :)
		
		// --- delete ---
		// meh, never seen these fail yet :)
	}
}
