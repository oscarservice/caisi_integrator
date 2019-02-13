package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicPreventionTest extends DaoTestFixtures
{
	private CachedDemographicDao cachedDemographicDao = (CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");
	private CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");
	private CachedDemographicPreventionDao cachedDemographicPreventionDao=(CachedDemographicPreventionDao)SpringUtils.getBean("cachedDemographicPreventionDao");
	
	@Test
	public void cachedDemographicPreventionTest()
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
		
		// prevention
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(4523);
		
		CachedDemographicPrevention cachedDemographicPrevention=new CachedDemographicPrevention();
		cachedDemographicPrevention.setFacilityPreventionPk(pk);
		cachedDemographicPrevention.setCaisiDemographicId(543);
		cachedDemographicPrevention.setCaisiProviderId("624");
		Date date=new Date();
		cachedDemographicPrevention.setNextDate(new Date(date.getTime()+DateUtils.MILLIS_PER_DAY*4));
		cachedDemographicPrevention.setPreventionDate(date);
		cachedDemographicPrevention.setPreventionType("test type");
		
		cachedDemographicPrevention.setAttributes("xml data");
		
		cachedDemographicPreventionDao.persist(cachedDemographicPrevention);
		
		FacilityIdIntegerCompositePk pk1=new FacilityIdIntegerCompositePk();
		pk1.setIntegratorFacilityId(facility1.getId());
		pk1.setCaisiItemId(4524);
		
		CachedDemographicPrevention cachedDemographicPrevention1=new CachedDemographicPrevention();
		cachedDemographicPrevention1.setFacilityPreventionPk(pk1);
		cachedDemographicPrevention1.setCaisiDemographicId(543);
		cachedDemographicPrevention1.setCaisiProviderId("624");
		cachedDemographicPrevention1.setPreventionDate(date);
		cachedDemographicPrevention1.setPreventionType("test type2");
		
		cachedDemographicPreventionDao.persist(cachedDemographicPrevention1);
		
		
		// --- find ---
		CachedDemographicPrevention prevention1=cachedDemographicPreventionDao.find(pk);
		assertEquals(543, prevention1.getCaisiDemographicId().intValue());
		assertEquals("624", prevention1.getCaisiProviderId());
		assertEquals("test type", prevention1.getPreventionType());
		
		List<CachedDemographicPrevention> pTemp=cachedDemographicPreventionDao.findByFacilityIdAndDemographicId(facility1.getId(), 543);
		assertEquals(2, pTemp.size());
		
		// --- update ---
		prevention1.setPreventionType("new prevention type");
		cachedDemographicPreventionDao.merge(prevention1);
		
		prevention1=cachedDemographicPreventionDao.find(pk);
		assertEquals("new prevention type", prevention1.getPreventionType());
		
		// --- delete ---
		cachedDemographicPreventionDao.remove(pk);
		prevention1=cachedDemographicPreventionDao.find(pk);
	    assertNull(prevention1);

	}
}
