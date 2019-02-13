package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsent.ConsentStatus;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicConsentTest extends DaoTestFixtures
{
	private static CachedDemographicConsentDao cachedDemographicConsentDao = (CachedDemographicConsentDao)SpringUtils.getBean("cachedDemographicConsentDao");
	private static DemographicLinkDao demographicLinkDao = (DemographicLinkDao)SpringUtils.getBean("demographicLinkDao");

	@Test
	public void demographicConsentTest()
	{
		// create
		CachedDemographic cachedDemographic = CachedDemographicTest.createPersistedCachedDemographic(1234);

		CachedDemographicConsent consent = new CachedDemographicConsent();
		consent.setFacilityDemographicPk(cachedDemographic.getFacilityIdIntegerCompositePk());
		consent.setClientConsentStatus(ConsentStatus.REVOKED);
		consent.getConsentToShareData().put(1234, true);
		Date createdDate = new Date(123456000);
		consent.setCreatedDate(createdDate);
		consent.setExcludeMentalHealthData(true);

		cachedDemographicConsentDao.persist(consent);

		// read 
		CachedDemographicConsent consent1 = cachedDemographicConsentDao.find(cachedDemographic.getId());
		assertEquals(ConsentStatus.REVOKED.name(), consent1.getClientConsentStatus().name());
		assertEquals(1, consent1.getConsentToShareData().size());
		assertEquals(true, consent1.getConsentToShareData().get(1234));
		assertEquals(createdDate.getTime(), consent1.getCreatedDate().getTime());
		assertEquals(true, consent1.isExcludeMentalHealthData());

		// update
		// these never really fail after a RW works

		// delete
		// these never really fail after a RW works
	}

	@Test
	public void demographicConsentFindLatestTest()
	{
		CachedDemographic cachedDemographic = CachedDemographicTest.createPersistedCachedDemographic(23456);
		CachedDemographic cachedDemographic1 = CachedDemographicTest.createPersistedCachedDemographic(5432);
		CachedProvider cachedProvider = CachedProviderTest.createPersistedProvider(6243);

		DemographicLink link = new DemographicLink();
		link.setIntegratorDemographicFacilityId1(cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		link.setCaisiDemographicId1(cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId());
		link.setIntegratorDemographicFacilityId2(cachedDemographic1.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		link.setCaisiDemographicId2(cachedDemographic1.getFacilityIdIntegerCompositePk().getCaisiItemId());
		link.setCreatorCaisiProviderId(cachedProvider.getFacilityIdStringCompositePk().getCaisiItemId());
		link.setCreatorIntegratorProviderFacilityId(cachedProvider.getFacilityIdStringCompositePk().getIntegratorFacilityId());
		demographicLinkDao.persist(link);

		CachedDemographicConsent consent = new CachedDemographicConsent();
		consent.setFacilityDemographicPk(cachedDemographic.getFacilityIdIntegerCompositePk());
		consent.setClientConsentStatus(ConsentStatus.REVOKED);
		consent.getConsentToShareData().put(23456, true);
		Date createdDate = new Date(123456000);
		consent.setCreatedDate(createdDate);
		consent.setExcludeMentalHealthData(true);
		cachedDemographicConsentDao.persist(consent);

		CachedDemographicConsent consent1 = new CachedDemographicConsent();
		consent1.setFacilityDemographicPk(cachedDemographic1.getFacilityIdIntegerCompositePk());
		consent1.setClientConsentStatus(ConsentStatus.GIVEN);
		consent1.getConsentToShareData().put(23456, true);
		Date createdDate1 = new Date(123222000);
		consent1.setCreatedDate(createdDate1);
		consent1.setExcludeMentalHealthData(false);
		cachedDemographicConsentDao.persist(consent1);
		
		CachedDemographicConsent latest=cachedDemographicConsentDao.findLatestConsentFromAllLinkedClients(cachedDemographic1.getFacilityIdIntegerCompositePk());
		assertEquals(latest.getFacilityDemographicPk(), consent.getFacilityDemographicPk());
	}
}
