package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.dao.EventLog.DataActionValue;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class EventLogTest extends DaoTestFixtures
{
	private static EventLogDao eventLogDao = (EventLogDao)SpringUtils.getBean("eventLogDao");

	@Test
	public void eventLogTest()
	{
		//--- create ---
		CachedDemographic cachedDemographic = CachedDemographicTest.createPersistedCachedDemographic(19191);
		EventLog eventLog = eventLogDao.createDataEventEntry(DataActionValue.READ, cachedDemographic);

		//--- find ---
		EventLog eventLog1 = eventLogDao.find(eventLog.getId());

		assertNotNull(eventLog1.getDate());
		assertEquals("THREAD:JunitTestThread:DaoTestFixtures", eventLog1.getSource());
		assertEquals("DATA:READ", eventLog1.getAction());
		assertEquals("CachedDemographic:" + cachedDemographic.getId(), eventLog1.getParameters());

		//--- update ---
		eventLog1.setAction("blah blah");

		try
		{
			eventLogDao.merge(eventLog1);
			fail("exception should have been thrown, update is not allowed");
		}
		catch (Exception e)
		{
			// okay this is good
		}

		//--- delete ---
		try
		{
			eventLogDao.remove(eventLog1.getId());
			fail("exception should have been thrown, remove is not allowed");
		}
		catch (Exception e)
		{
			// okay this is good
		}
	}

	@Test
	public void testFindAll()
	{
		int startIndex = 0;
		int itemsToReturn = 30;

		List<EventLog> results = eventLogDao.findAll(startIndex, itemsToReturn);
		assertNotNull(results);
	}
	
	@Test
	public void findByActionParametersSourceTest() {
		
		int startIndex = 0, itemsToReturn = 30;
		CachedDemographic cachedDemographic = CachedDemographicTest.createPersistedCachedDemographic(29191);
		eventLogDao.createDataEventEntry(DataActionValue.READ, cachedDemographic);
		
		String action = "%";
		String parameters = "%" + cachedDemographic.getId() + "%";
		String source = "%";
		List<EventLog> results = eventLogDao.findByActionParametersSource(action, parameters, source, startIndex, itemsToReturn);
		assertTrue(!results.isEmpty());
		
		for(int i = 0; i < results.size(); i++)
		{
			assertNotNull(results.get(i).getDate());
			assertEquals("THREAD:JunitTestThread:DaoTestFixtures", results.get(i).getSource());
			assertEquals("CachedDemographic:" + cachedDemographic.getId(), results.get(i).getParameters());
		}
	}
}
