package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class FacilityTest extends DaoTestFixtures
{
	private static FacilityDao facilityDao=(FacilityDao)SpringUtils.getBean("facilityDao");
	
	public static Facility createPersistedFacility(long randomNumber)
	{
		Facility facility=new Facility();
		facility.setPassword("password_"+randomNumber);
		facility.setName("userName1");

		facilityDao.persist(facility);
		
		return(facility);
	}
	
	@Test
	public void facilityTest()
	{
		long currentTime=System.currentTimeMillis();
		Facility facility=createPersistedFacility(currentTime);
		
		//--- find ---
		Facility facility1=facilityDao.find(facility.getId());
		
		assertTrue(facility1.checkPassword("password_"+currentTime));
		assertEquals("username1", facility1.getName());
		
		facility1=facilityDao.findByName(facility.getName());
		assertEquals(facility.getId(), facility1.getId());
		
				
		//--- update ---
		facility1.setPassword("the updated password");
		facilityDao.merge(facility1);
		
		Facility facility2=facilityDao.find(facility.getId());
		assertTrue(facility2.checkPassword("the updated password"));
		        
		//--- delete ---
        facilityDao.remove(facility1.getId());
        Facility facility3=facilityDao.find(facility.getId());
		assertNull(facility3);
	}
	
}
