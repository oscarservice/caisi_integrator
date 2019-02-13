package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicTest extends DaoTestFixtures
{
    private static CachedDemographicDao cachedDemographicDao=(CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");

    protected static CachedDemographic createPersistedCachedDemographic(int seed)
    {
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(seed);
		
		CachedDemographic cachedDemographic=new CachedDemographic();
		cachedDemographic.setFacilityIdIntegerCompositePk(pk);
		cachedDemographic.setFirstName("bob ");
		cachedDemographic.setLastName("Dole");
		
		cachedDemographicDao.persist(cachedDemographic);
		
		return(cachedDemographic);
    }
    
	@Test
	public void demographicTest()
	{
		// create
		CachedDemographic cachedDemographic=createPersistedCachedDemographic(1234);
		
	    // read 
		CachedDemographic cachedDemographic1=cachedDemographicDao.find(cachedDemographic.getId());
		assertEquals(facility1.getId().intValue(),cachedDemographic1.getId().getIntegratorFacilityId().intValue());
		assertEquals(1234,cachedDemographic1.getId().getCaisiItemId().intValue());
		assertEquals("bob",cachedDemographic1.getFirstName());
		assertEquals("Dole",cachedDemographic1.getLastName());
		
	    // update
	    cachedDemographic1.setLastName("new last name");
	    cachedDemographicDao.merge(cachedDemographic1);

	    cachedDemographic1=cachedDemographicDao.find(cachedDemographic.getId());
		assertEquals("new last name",cachedDemographic1.getLastName());
	    
	    // delete
		cachedDemographicDao.remove(cachedDemographic.getId());
	    cachedDemographic1=cachedDemographicDao.find(cachedDemographic.getId());
	    assertNull(cachedDemographic1);
	}
	
	@Test
	public void fuzzyMatchTest()
	{
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility2.getId());
		pk.setCaisiItemId(100);

		CachedDemographic cachedDemographic=new CachedDemographic();
		cachedDemographic.setFacilityIdIntegerCompositePk(pk);
		cachedDemographic.setFirstName("first");
		cachedDemographic.setLastName("last");

		cachedDemographicDao.persist(cachedDemographic);

		List<MatchingCachedDemographicScore> results=cachedDemographicDao.findMatchingDemographics(1, 10, 1, "first", "last", null, null, null, null, null, null);
		assertEquals(1,results.size());
		
		results=cachedDemographicDao.findMatchingDemographics(1, 10, 1, "first", "xxx", null, null, null, null, null, null);
		assertEquals(1,results.size());
		
		results=cachedDemographicDao.findMatchingDemographics(1, 10, 1, "xxx", "last", null, null, null, null, null, null);
		assertEquals(1,results.size());
	}

	@Test
	public void countTest()
	{
		// create
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(12345);
		
		CachedDemographic cachedDemographic=new CachedDemographic();
		cachedDemographic.setFacilityIdIntegerCompositePk(pk);
		cachedDemographic.setFirstName("123455 ");
		cachedDemographic.setLastName("4321");
		cachedDemographicDao.persist(cachedDemographic);

		int count=cachedDemographicDao.getCount(facility1.getId());
		assertEquals(1,count);
	}
	
	@Test
	public void sameHCTest()
	{
		// create
		CachedDemographic cachedDemographic1=createPersistedCachedDemographic(11111);
		cachedDemographic1.setHin("1234asddf");
		cachedDemographic1.setHinType("zxcv");
		cachedDemographicDao.merge(cachedDemographic1);
		
		// create
		CachedDemographic cachedDemographic2=createPersistedCachedDemographic(22222);
		cachedDemographic2.setHin("1234asddf");
		cachedDemographic2.setHinType("zxcv");
		cachedDemographicDao.merge(cachedDemographic2);
		
		// find 
		List<CachedDemographic> results=cachedDemographicDao.findByHealthNumber(cachedDemographic1.getHin(), cachedDemographic1.getHinType());
		assertEquals(2,results.size());
	}
	
	@Test
	public void testGetIds()
	{
		List<FacilityIdIntegerCompositePk> results=cachedDemographicDao.findAllIds();
		assertTrue(results.size()>0);
	}
}
