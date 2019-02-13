package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedFacilityTest extends DaoTestFixtures
{
    private static CachedFacilityDao cachedFacilityDao=(CachedFacilityDao)SpringUtils.getBean("cachedFacilityDao");
	
    public static CachedFacility createPersistedCachedFacility(Facility facility)
    {
		CachedFacility cachedFacility=new CachedFacility();
	    cachedFacility.setIntegratorFacilityId(facility.getId());
	    cachedFacility.setContactEmail("my email");
	    cachedFacility.setContactName("my name");
	    cachedFacility.setContactPhone("my phone");
	    cachedFacility.setDescription("my description");
	    cachedFacility.setName("facility name");
	    cachedFacilityDao.persist(cachedFacility);
	    
	    return(cachedFacility);
    }
    
	@Test
	public void facilityTest()
	{
		long time=System.currentTimeMillis();
		Facility facility=FacilityTest.createPersistedFacility(time);
		CachedFacility cachedFacility=createPersistedCachedFacility(facility);
		
	    // read 
	    cachedFacility=cachedFacilityDao.find(cachedFacility.getId());
	    assertEquals("my email", cachedFacility.getContactEmail());
	    assertEquals("my name", cachedFacility.getContactName());
	    assertEquals("my phone", cachedFacility.getContactPhone());
	    assertEquals("my description", cachedFacility.getDescription());
	    assertEquals("facility name", cachedFacility.getName());
	    
	    // update
	    cachedFacility.setContactName("my name 2");
	    cachedFacilityDao.merge(cachedFacility);
	    
	    cachedFacility=cachedFacilityDao.find(facility.getId());
	    assertEquals("my name 2", cachedFacility.getContactName());
	    assertEquals("my phone", cachedFacility.getContactPhone());
	    
	    // delete
	    cachedFacilityDao.remove(facility.getId());
	    cachedFacility=cachedFacilityDao.find(facility.getId());
	    assertNull(cachedFacility);
	}
	
}
