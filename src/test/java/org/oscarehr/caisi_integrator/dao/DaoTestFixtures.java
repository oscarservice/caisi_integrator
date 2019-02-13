package org.oscarehr.caisi_integrator.dao;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.oscarehr.caisi_integrator.dao.schema.InitialiseDataStore;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoInternalThread;
import org.oscarehr.caisi_integrator.util.SpringUtils;
import org.springframework.beans.BeansException;

public class DaoTestFixtures
{
	public static String THREAD_NAME="JunitTestThread";
	
	protected static Facility facility1=null;
	protected static Facility facility2=null;
	protected static Facility facility3=null;
	
	@BeforeClass
	public static void classSetUp() throws BeansException, IOException
	{
		createBlankDatabase();
		
		LoggedInInfoInternalThread.setLoggedInInfoToCurrentClassAndThreadName();

		createDefaultSiteUsers();
		createDefaultFacilities();
		createDefaultCachedFacility();
	}
	
	@AfterClass
	public static void classTearDown()
	{
		LoggedInInfo.loggedInInfo.remove();
	}

	public static void createBlankDatabase() throws IOException
	{
		Thread.currentThread().setName(THREAD_NAME);
		
		System.setProperty("caisi_integrator_properties", "/over_ride_config.properties");
		InitialiseDataStore.recreateDatabase();		
	}
	
	private static void createDefaultSiteUsers()
	{
	    SiteUserDao siteUserDao=(SiteUserDao)SpringUtils.getBean("siteUserDao");
	    SiteUser siteUser=new SiteUser();
	    siteUser.setName("user");
	    siteUser.setPassword("password");
	    siteUserDao.persist(siteUser);
	}

	private static void createDefaultCachedFacility()
    {
	    CachedFacilityDao cachedFacilityDao=(CachedFacilityDao)SpringUtils.getBean("cachedFacilityDao");
	    
	    CachedFacility cachedFacility1=new CachedFacility();
	    cachedFacility1.setIntegratorFacilityId(facility1.getId());
	    cachedFacility1.setName("facility 1");
	    cachedFacilityDao.persist(cachedFacility1);

	    CachedFacility cachedFacility2=new CachedFacility();
	    cachedFacility2.setIntegratorFacilityId(facility2.getId());
	    cachedFacility2.setName("facility 2");
	    cachedFacilityDao.persist(cachedFacility2);
    }

	private static void createDefaultFacilities()
    {
	    FacilityDao facilityDao=(FacilityDao)SpringUtils.getBean("facilityDao");
	    
	    facility1=new Facility();
	    facility1.setName("facility1");
	    facility1.setPassword("password1");
	    facilityDao.persist(facility1);
	    
	    facility2=new Facility();
	    facility2.setName("facility2");
	    facility2.setPassword("password2");
	    facilityDao.persist(facility2);	    

	    facility3=new Facility();
	    facility3.setName("facility3");
	    facility3.setPassword("password3");
	    facilityDao.persist(facility3);	    
    }
}