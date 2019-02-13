package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.TreeMap;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicIssueTest extends DaoTestFixtures
{
    private CachedDemographicIssueDao cachedDemographicIssueDao=(CachedDemographicIssueDao)SpringUtils.getBean("cachedDemographicIssueDao");
	
	@Test
	public void demographicIssueTest()
	{
		// create
		int demographicId=543;
		CachedDemographicTest.createPersistedCachedDemographic(demographicId);
		
		FacilityIdDemographicIssueCompositePk pk1=new FacilityIdDemographicIssueCompositePk();
		pk1.setIntegratorFacilityId(facility1.getId());
		pk1.setCaisiDemographicId(demographicId);
		pk1.setCodeType(CodeType.CUSTOM_ISSUE);
		pk1.setIssueCode("my_issue_code");
		
		CachedDemographicIssue issue=new CachedDemographicIssue();
		issue.setFacilityDemographicIssuePk(pk1);
		issue.setIssueDescription("my issue description");
		cachedDemographicIssueDao.persist(issue);
		
		FacilityIdDemographicIssueCompositePk pk2=new FacilityIdDemographicIssueCompositePk();
		pk2.setIntegratorFacilityId(facility1.getId());
		pk2.setCaisiDemographicId(demographicId);
		pk2.setCodeType(CodeType.CUSTOM_ISSUE);
		pk2.setIssueCode("my_issue2_code");
		
		CachedDemographicIssue issue2=new CachedDemographicIssue();
		issue2.setFacilityDemographicIssuePk(pk2);
		issue2.setIssueDescription("my issue2 description");
		cachedDemographicIssueDao.persist(issue2);
		
		// test find 
		List<CachedDemographicIssue> results=cachedDemographicIssueDao.findByFacilityAndDemographicId(facility1.getId(), demographicId);
		assertEquals(2, results.size());
		
		CachedDemographicIssue tmp1=cachedDemographicIssueDao.find(pk2);
		assertNotNull(tmp1);
			
		// read 
		CachedDemographicIssue issueTemp=cachedDemographicIssueDao.find(issue.getId());
		assertEquals(CodeType.CUSTOM_ISSUE, issueTemp.getId().getCodeType());
		assertEquals("my_issue_code", issueTemp.getId().getIssueCode());
		assertEquals("my issue description", issueTemp.getIssueDescription());
		
		// update
		issueTemp.setIssueDescription("my desc updated");
		cachedDemographicIssueDao.merge(issueTemp);
	    	    
		issueTemp=cachedDemographicIssueDao.find(issue.getId());
		assertEquals("my desc updated", issueTemp.getIssueDescription());

		// delete
		cachedDemographicIssueDao.remove(issueTemp.getId());
		issueTemp=cachedDemographicIssueDao.find(issue.getId());
		assertNull(issueTemp);
		
		// count test 		
		int count=cachedDemographicIssueDao.getCount(facility1.getId());
		assertEquals(1,count);
	}

	@Test
	public void findIssuesTest()
	{
		// create
		int demographicId=54321;
		CachedDemographicTest.createPersistedCachedDemographic(demographicId);

		int demographicId2=543210;
		CachedDemographicTest.createPersistedCachedDemographic(demographicId2);

		//--- first issue 
		
		FacilityIdDemographicIssueCompositePk pk1=new FacilityIdDemographicIssueCompositePk();
		pk1.setIntegratorFacilityId(facility1.getId());
		pk1.setCaisiDemographicId(demographicId);
		pk1.setCodeType(CodeType.CUSTOM_ISSUE);
		pk1.setIssueCode("my_issue_code1");
		
		CachedDemographicIssue issue=new CachedDemographicIssue();
		issue.setFacilityDemographicIssuePk(pk1);
		issue.setIssueDescription("my issue description");
		cachedDemographicIssueDao.persist(issue);

		//--- second issue 
		
		FacilityIdDemographicIssueCompositePk pk2=new FacilityIdDemographicIssueCompositePk();
		pk2.setIntegratorFacilityId(facility1.getId());
		pk2.setCaisiDemographicId(demographicId);
		pk2.setCodeType(CodeType.CUSTOM_ISSUE);
		pk2.setIssueCode("my_issue_code2");
		
		CachedDemographicIssue issue2=new CachedDemographicIssue();
		issue2.setFacilityDemographicIssuePk(pk2);
		issue2.setIssueDescription("my issue2 description");
		cachedDemographicIssueDao.persist(issue2);
		
		//--- third issue 
		
		FacilityIdDemographicIssueCompositePk pk3=new FacilityIdDemographicIssueCompositePk();
		pk3.setIntegratorFacilityId(facility1.getId());
		pk3.setCaisiDemographicId(demographicId2);
		pk3.setCodeType(CodeType.CUSTOM_ISSUE);
		pk3.setIssueCode("my_issue_code2");
		
		CachedDemographicIssue issue3=new CachedDemographicIssue();
		issue3.setFacilityDemographicIssuePk(pk3);
		issue3.setIssueDescription("my issue2 description");
		cachedDemographicIssueDao.persist(issue3);
		
		//--- check
		TreeMap<NoteIssue, String> results=cachedDemographicIssueDao.findReportedIssues();
		assertEquals(3, results.size());
	}
	
	@Test
	public void testHasIssueGroupIssue()
	{
		int demographicId=448824;
		CachedDemographicTest.createPersistedCachedDemographic(demographicId);

		//--- issue in issueGroups 
		
		FacilityIdDemographicIssueCompositePk pk1=new FacilityIdDemographicIssueCompositePk();
		pk1.setIntegratorFacilityId(facility1.getId());
		pk1.setCaisiDemographicId(demographicId);
		pk1.setCodeType(CodeType.ICD10);
		pk1.setIssueCode("J100");
		
		CachedDemographicIssue issue=new CachedDemographicIssue();
		issue.setFacilityDemographicIssuePk(pk1);
		issue.setIssueDescription("my issue description");
		issue.setResolved(false);
		cachedDemographicIssueDao.persist(issue);

		//--- check for it in group select ---
		boolean hasIssue=cachedDemographicIssueDao.hasUnresolvedIssueFromIssueGroup(facility1.getId(), demographicId, "INFLUENZA");
		assertTrue(hasIssue);

		hasIssue=cachedDemographicIssueDao.hasUnresolvedIssueFromIssueGroup(facility1.getId(), demographicId, "PNEUMONIA");
		assertFalse(hasIssue);
	}
}