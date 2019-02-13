package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class SiteUserTest extends DaoTestFixtures
{
	private static SiteUserDao siteUserDao=(SiteUserDao)SpringUtils.getBean("siteUserDao");
	
	public static SiteUser createPersistedSiteUser(long randomNumber)
	{
		SiteUser siteUser=new SiteUser();
		siteUser.setPassword("password_"+randomNumber);
		siteUser.setName("userName1");

		siteUserDao.persist(siteUser);
		
		return(siteUser);
	}
	
	@Test
	public void siteUserTest()
	{
		long currentTime=System.currentTimeMillis();
		SiteUser siteUser=createPersistedSiteUser(currentTime);
		
		//--- find ---
		SiteUser siteUser1=siteUserDao.find(siteUser.getId());
		
		assertTrue(siteUser1.checkPassword("password_"+currentTime));
		assertEquals("username1", siteUser1.getName());
		
		siteUser1=siteUserDao.findByName(siteUser.getName());
		assertEquals(siteUser.getId(), siteUser1.getId());
		
				
		//--- update ---
		siteUser1.setPassword("the updated password");
		siteUserDao.merge(siteUser1);
		
		SiteUser siteUser2=siteUserDao.find(siteUser.getId());
		assertTrue(siteUser2.checkPassword("the updated password"));
		        
		//--- delete ---
        siteUserDao.remove(siteUser1.getId());
        SiteUser siteUser3=siteUserDao.find(siteUser.getId());
		assertNull(siteUser3);
	}
	
}
