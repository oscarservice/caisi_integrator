package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class DemographicLinkTest extends DaoTestFixtures
{
	private DemographicLinkDao demographicLinkDao = (DemographicLinkDao)SpringUtils.getBean("demographicLinkDao");
	private CachedDemographicDao cachedDemographicDao = (CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");

	@Test
	public void demographicLinkTest()
	{
		// create
		DemographicLink demographicLink = new DemographicLink();
		demographicLink.setCaisiDemographicId1(111);
		demographicLink.setCaisiDemographicId2(222);
		demographicLink.setCreatorCaisiProviderId("provider34");
		demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
		demographicLink.setIntegratorDemographicFacilityId1(facility1.getId());
		demographicLink.setIntegratorDemographicFacilityId2(facility2.getId());

		demographicLinkDao.persist(demographicLink);

		// test find
		DemographicLink demographicLink1 = demographicLinkDao.find(demographicLink.getId());
		assertEquals(111, demographicLink1.getCaisiDemographicId1().intValue());
		assertEquals(222, demographicLink1.getCaisiDemographicId2().intValue());
		assertEquals("provider34", demographicLink1.getCreatorCaisiProviderId());
		assertEquals(facility1.getId(), demographicLink1.getCreatorIntegratorProviderFacilityId());
		assertEquals(facility1.getId(), demographicLink1.getIntegratorDemographicFacilityId1());
		assertEquals(facility2.getId(), demographicLink1.getIntegratorDemographicFacilityId2());

		FacilityIdIntegerCompositePk pk1=new FacilityIdIntegerCompositePk();
		pk1.setIntegratorFacilityId(facility2.getId());
		pk1.setCaisiItemId(222);
		FacilityIdIntegerCompositePk pk2=new FacilityIdIntegerCompositePk();
		pk2.setIntegratorFacilityId(facility1.getId());
		pk2.setCaisiItemId(111);
		DemographicLink demographicLinkReverse=demographicLinkDao.findByPair(pk1, pk2);
		assertNotNull(demographicLinkReverse);
		
		// test update
		demographicLink1.setCaisiDemographicId2(22222);
		demographicLinkDao.merge(demographicLink1);

		demographicLink1 = demographicLinkDao.find(demographicLink.getId());
		assertEquals(22222, demographicLink1.getCaisiDemographicId2().intValue());

		// delete
		demographicLinkDao.remove(demographicLink.getId());
		demographicLink1 = demographicLinkDao.find(demographicLink.getId());
		assertNull(demographicLink1);
	}

	@Test
	public void demographicLinkedSearch()
	{
		CachedDemographic cachedDemographic1=CachedDemographicTest.createPersistedCachedDemographic(111);
		cachedDemographic1.setHin("asdf3465");
		cachedDemographic1.setHinType("pppp");
		cachedDemographicDao.merge(cachedDemographic1);
		
		CachedDemographic cachedDemographic2=CachedDemographicTest.createPersistedCachedDemographic(222);
		CachedDemographic cachedDemographic3=CachedDemographicTest.createPersistedCachedDemographic(333);
		CachedDemographic cachedDemographic4=CachedDemographicTest.createPersistedCachedDemographic(444);
		CachedDemographic cachedDemographic5=CachedDemographicTest.createPersistedCachedDemographic(555);
		CachedDemographic cachedDemographic6=CachedDemographicTest.createPersistedCachedDemographic(666);

		CachedDemographic cachedDemographic7=CachedDemographicTest.createPersistedCachedDemographic(777);
		cachedDemographic7.setHin("asdf3465");
		cachedDemographicDao.merge(cachedDemographic7);

		CachedDemographic cachedDemographic8=CachedDemographicTest.createPersistedCachedDemographic(888);
		cachedDemographic8.setHin("asdf3465");
		cachedDemographic8.setHinType("pppp");
		cachedDemographicDao.merge(cachedDemographic8);
		
		// create
		{
			DemographicLink demographicLink = new DemographicLink();
			demographicLink.setCaisiDemographicId1(cachedDemographic1.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCaisiDemographicId2(cachedDemographic2.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCreatorCaisiProviderId("provider34");
			demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId1(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId2(facility2.getId());

			demographicLinkDao.persist(demographicLink);
		}

		{
			DemographicLink demographicLink = new DemographicLink();
			demographicLink.setCaisiDemographicId1(cachedDemographic3.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCaisiDemographicId2(cachedDemographic1.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCreatorCaisiProviderId("provider34");
			demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId1(facility2.getId());
			demographicLink.setIntegratorDemographicFacilityId2(facility1.getId());

			demographicLinkDao.persist(demographicLink);
		}

		{
			DemographicLink demographicLink = new DemographicLink();
			demographicLink.setCaisiDemographicId1(cachedDemographic4.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCaisiDemographicId2(cachedDemographic3.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCreatorCaisiProviderId("provider34");
			demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId1(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId2(facility2.getId());

			demographicLinkDao.persist(demographicLink);
		}
		
		{
			DemographicLink demographicLink = new DemographicLink();
			demographicLink.setCaisiDemographicId1(cachedDemographic2.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCaisiDemographicId2(cachedDemographic4.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCreatorCaisiProviderId("provider34");
			demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId1(facility2.getId());
			demographicLink.setIntegratorDemographicFacilityId2(facility1.getId());

			demographicLinkDao.persist(demographicLink);
		}

		{
			DemographicLink demographicLink = new DemographicLink();
			demographicLink.setCaisiDemographicId1(cachedDemographic5.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCaisiDemographicId2(cachedDemographic6.getFacilityIdIntegerCompositePk().getCaisiItemId());
			demographicLink.setCreatorCaisiProviderId("provider34");
			demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId1(facility1.getId());
			demographicLink.setIntegratorDemographicFacilityId2(facility2.getId());

			demographicLinkDao.persist(demographicLink);
		}
		
		FacilityIdIntegerCompositePk facilityIdIntegerCompositePk=new FacilityIdIntegerCompositePk();
		facilityIdIntegerCompositePk.setIntegratorFacilityId(facility1.getId());
		facilityIdIntegerCompositePk.setCaisiItemId(cachedDemographic1.getFacilityIdIntegerCompositePk().getCaisiItemId());
		
		Set<FacilityIdIntegerCompositePk> results=demographicLinkDao.getDirectlyLinkedDemographics(facilityIdIntegerCompositePk, null);
		assertEquals(3,results.size());

		Set<FacilityIdIntegerCompositePk> results1=demographicLinkDao.getAllLinkedDemographics(facilityIdIntegerCompositePk, true);
		assertEquals(4,results1.size());
	}
}
