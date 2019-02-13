package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedBillingOnItemTest extends DaoTestFixtures
{
	private CachedDemographicDao cachedDemographicDao = (CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");
	private CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");
	private CachedBillingOnItemDao cachedBillingOnItemDao=(CachedBillingOnItemDao)SpringUtils.getBean("cachedBillingOnItemDao");
	
	@Test
	public void cachedBillingOnItemTest()
	{
		// --- create ---

		// provider
		FacilityIdStringCompositePk pkp=new FacilityIdStringCompositePk();
		pkp.setIntegratorFacilityId(facility1.getId());
		pkp.setCaisiItemId("624");
		CachedProvider cachedProvider=new CachedProvider();
		cachedProvider.setFacilityIdStringCompositePk(pkp);
		cachedProvider.setFirstName("first name");
		cachedProvider.setLastName("last name");
		cachedProviderDao.persist(cachedProvider);

		// demographic
		FacilityIdIntegerCompositePk pkd=new FacilityIdIntegerCompositePk();
		pkd.setIntegratorFacilityId(facility1.getId());
		pkd.setCaisiItemId(543);
		CachedDemographic cachedDemographic=new CachedDemographic();
		cachedDemographic.setFacilityIdIntegerCompositePk(pkd);
		cachedDemographic.setFirstName("fn");
		cachedDemographic.setLastName("ln");
		cachedDemographicDao.persist(cachedDemographic);
		
		// billingOnItem
		FacilityIdIntegerCompositePk pkb=new FacilityIdIntegerCompositePk();
		pkb.setIntegratorFacilityId(facility1.getId());
		pkb.setCaisiItemId(4523);

                CachedBillingOnItem cachedBillingOnItem = new CachedBillingOnItem();
                cachedBillingOnItem.setFacilityIdIntegerCompositePk(pkb);
                cachedBillingOnItem.setCaisiDemographicId(543);
                cachedBillingOnItem.setCaisiProviderId("624");
                cachedBillingOnItem.setAppointmentId(123);
                cachedBillingOnItem.setApptProviderId("456");
                cachedBillingOnItem.setAsstProviderId("789");
                cachedBillingOnItem.setDx("2071");
                cachedBillingOnItem.setDx1("2072");
                cachedBillingOnItem.setDx2("2073");
                cachedBillingOnItem.setServiceCode("207120722073");
                cachedBillingOnItem.setStatus("A");
		Date date = new Date();
                cachedBillingOnItem.setServiceDate(date);

                cachedBillingOnItemDao.persist(cachedBillingOnItem);

		
		// --- find ---
                CachedBillingOnItem[] billing = new CachedBillingOnItem[6];
                billing[0] = cachedBillingOnItemDao.find(pkb);
                List<CachedBillingOnItem> billingList = cachedBillingOnItemDao.findByDx("2071");
                if (billingList.size()>0) billing[1] = billingList.get(0);
                billingList = cachedBillingOnItemDao.findByDx("2072");
                if (billingList.size()>0) billing[2] = billingList.get(0);
                billingList = cachedBillingOnItemDao.findByDx("2073");
                if (billingList.size()>0) billing[3] = billingList.get(0);
                billingList = cachedBillingOnItemDao.findByFacilityAndDemographicId(facility1.getId(), 543);
                if (billingList.size()>0) billing[4] = billingList.get(0);
                billingList = cachedBillingOnItemDao.findByFacilityAndProviderId(facility1.getId(), "624");
                if (billingList.size()>0) billing[5] = billingList.get(0);

                for (int i=0; i<billing.length; i++) {
                    if (billing[i]==null) billing[i] = new CachedBillingOnItem();
                    assertEquals(543, billing[i].getCaisiDemographicId().intValue());
                    assertEquals("624", billing[i].getCaisiProviderId());
                    assertEquals(123, billing[i].getAppointmentId().intValue());
                    assertEquals("456", billing[i].getApptProviderId());
                    assertEquals("789", billing[i].getAsstProviderId());
                    assertEquals("2071", billing[i].getDx());
                    assertEquals("2072", billing[i].getDx1());
                    assertEquals("2073", billing[i].getDx2());
                    assertEquals("207120722073", billing[i].getServiceCode());
                    assertEquals(date, billing[i].getServiceDate());
                    assertEquals("A", billing[i].getStatus());
                }
	}
}
