package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashSet;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.Role;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedProviderTest extends DaoTestFixtures
{
    private static CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");
	
    public static CachedProvider createPersistedProvider(int seed)
    {
		// create
		FacilityIdStringCompositePk pk=new FacilityIdStringCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(""+seed);
		
		CachedProvider cachedProvider=new CachedProvider();
		cachedProvider.setFacilityIdStringCompositePk(pk);
		cachedProvider.setFirstName("first name");
		cachedProvider.setLastName("last name");
		cachedProvider.setSpecialty("my specialty");
		cachedProvider.setWorkPhone("0401338607");
		cachedProviderDao.persist(cachedProvider);
		
		return(cachedProvider);
    }
    
	@Test
	public void providerTest()
	{
		// create
		CachedProvider cachedProvider=createPersistedProvider(624);
		FacilityIdStringCompositePk pk=cachedProvider.getFacilityIdStringCompositePk();
		
	    // read 
		cachedProvider=cachedProviderDao.find(pk);
		assertEquals("first name",cachedProvider.getFirstName());
		assertEquals("last name",cachedProvider.getLastName());
		assertEquals("my specialty",cachedProvider.getSpecialty());
		assertEquals("0401338607",cachedProvider.getWorkPhone());
		
	    // update
		cachedProvider.setSpecialty("new specialty");
		cachedProviderDao.merge(cachedProvider);

		cachedProvider=cachedProviderDao.find(pk);
		assertEquals("new specialty",cachedProvider.getSpecialty());
	    
	    // delete
		cachedProviderDao.remove(pk);
		cachedProvider=cachedProviderDao.find(pk);
	    assertNull(cachedProvider);
	}
	
	@Test
	public void testRoles()
	{
		// create
		CachedProvider cachedProvider=createPersistedProvider(7724);
		FacilityIdStringCompositePk pk=cachedProvider.getFacilityIdStringCompositePk();
		
		//--- make 1 role

		HashSet<Role> roles=new HashSet<Role>();
		roles.add(Role.RN);
		cachedProviderDao.setProviderRoles(pk, roles);
		
		roles=cachedProviderDao.getProviderRoles(pk);
		assertEquals(1, roles.size());

		//--- set the role to 2 others, old one should be removed and 2 new should be added.
		
		roles=new HashSet<Role>();
		roles.add(Role.DOCTOR);
		roles.add(Role.RN);
		cachedProviderDao.setProviderRoles(pk, roles);

		roles=cachedProviderDao.getProviderRoles(pk);
		assertEquals(2, roles.size());
	}
}
