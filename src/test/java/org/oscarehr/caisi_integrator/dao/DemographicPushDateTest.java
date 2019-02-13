package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class DemographicPushDateTest extends DaoTestFixtures
{
	private DemographicPushDateDao demographicPushDateDao = (DemographicPushDateDao) SpringUtils.getBean("demographicPushDateDao");

	@Test
	public void demographicPushDateTest()
	{
		// create
		FacilityIdIntegerCompositePk id = new FacilityIdIntegerCompositePk();
		id.setIntegratorFacilityId(1);
		id.setCaisiItemId(222);

		DemographicPushDate demographicPushDate = new DemographicPushDate();
		demographicPushDate.setId(id);

		demographicPushDateDao.persist(demographicPushDate);

		// test find
		DemographicPushDate demographicPushDate1 = demographicPushDateDao.find(demographicPushDate.getId());
		assertNotNull(demographicPushDate1);

		// test find by update date
		id = new FacilityIdIntegerCompositePk();
		id.setIntegratorFacilityId(2);
		id.setCaisiItemId(333);

		demographicPushDate = new DemographicPushDate();
		demographicPushDate.setId(id);
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, 3);
		demographicPushDate.setLastPushDate(cal);

		demographicPushDateDao.persist(demographicPushDate);

		List<DemographicPushDate> results = demographicPushDateDao.findAfterDate(new GregorianCalendar());
		assertEquals(1, results.size());
	}

	@Test
	public void testLinkedUpdatesMethod()
	{
		// scenerio / demographics
		// 11111 (updated older than date)
		// 22222 (updated newer, linked to 13333)
		// 13333 (updated older than date)
		// 24444 (updated newer than date)

		// create link
		DemographicLink demographicLink = new DemographicLink();
		demographicLink.setCaisiDemographicId1(13333);
		demographicLink.setCaisiDemographicId2(22222);
		demographicLink.setCreatorCaisiProviderId("provider34");
		demographicLink.setCreatorIntegratorProviderFacilityId(facility1.getId());
		demographicLink.setIntegratorDemographicFacilityId1(facility1.getId());
		demographicLink.setIntegratorDemographicFacilityId2(facility2.getId());

		DemographicLinkDao demographicLinkDao = (DemographicLinkDao) SpringUtils.getBean("demographicLinkDao");
		demographicLinkDao.persist(demographicLink);

		//---

		Calendar olderDate = new GregorianCalendar();
		olderDate.add(Calendar.DAY_OF_YEAR, -2);
		olderDate.getTime();

		Calendar newerDate = new GregorianCalendar();
		newerDate.add(Calendar.DAY_OF_YEAR, 2);
		newerDate.getTime();

		// 11111
		FacilityIdIntegerCompositePk id = new FacilityIdIntegerCompositePk();
		id.setIntegratorFacilityId(facility1.getId());
		id.setCaisiItemId(11111);

		DemographicPushDate demographicPushDate = new DemographicPushDate();
		demographicPushDate.setId(id);
		demographicPushDate.setLastPushDate(olderDate);
		demographicPushDateDao.persist(demographicPushDate);

		// 22222
		id = new FacilityIdIntegerCompositePk();
		id.setIntegratorFacilityId(facility2.getId());
		id.setCaisiItemId(22222);

		demographicPushDate = new DemographicPushDate();
		demographicPushDate.setId(id);
		demographicPushDate.setLastPushDate(newerDate);
		demographicPushDateDao.persist(demographicPushDate);

		// 13333
		id = new FacilityIdIntegerCompositePk();
		id.setIntegratorFacilityId(facility1.getId());
		id.setCaisiItemId(13333);

		demographicPushDate = new DemographicPushDate();
		demographicPushDate.setId(id);
		demographicPushDate.setLastPushDate(olderDate);
		demographicPushDateDao.persist(demographicPushDate);

		// 24444
		id = new FacilityIdIntegerCompositePk();
		id.setIntegratorFacilityId(facility2.getId());
		id.setCaisiItemId(24444);

		demographicPushDate = new DemographicPushDate();
		demographicPushDate.setId(id);
		demographicPushDate.setLastPushDate(newerDate);
		demographicPushDateDao.persist(demographicPushDate);

		// check results
		Calendar now = new GregorianCalendar();

		// check from facility 1
		ArrayList<Integer> tempResults = demographicPushDateDao.getUpdatedLinkedDemographicIdsFromFacilityAfterDate(facility1.getId(), now);
		assertTrue(tempResults.contains(13333));
		assertFalse(tempResults.contains(11111));
		assertFalse(tempResults.contains(22222));
		assertFalse(tempResults.contains(24444));

		// check from facility 2
		tempResults = demographicPushDateDao.getUpdatedLinkedDemographicIdsFromFacilityAfterDate(facility2.getId(), now);
		assertFalse(tempResults.contains(13333));
		assertFalse(tempResults.contains(11111));
		assertTrue(tempResults.contains(22222));
		assertTrue(tempResults.contains(24444));

	}
}
