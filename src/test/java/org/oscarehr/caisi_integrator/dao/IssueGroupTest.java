package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class IssueGroupTest extends DaoTestFixtures
{
	private static IssueGroupDao issueGroupDao=(IssueGroupDao)SpringUtils.getBean("issueGroupDao");
	
	@Test
	public void issueGroupTest()
	{
		//--- find ---
		List<IssueGroup> results=issueGroupDao.findByName("HIV");		
		assertEquals(3, results.size());
	}
	
}
