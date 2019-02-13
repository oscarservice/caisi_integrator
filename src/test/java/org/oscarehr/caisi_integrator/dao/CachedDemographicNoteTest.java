package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicNoteTest extends DaoTestFixtures
{
    private CachedDemographicNoteDao cachedDemographicNoteDao=(CachedDemographicNoteDao)SpringUtils.getBean("cachedDemographicNoteDao");

    @Test
	public void demographicNoteTest()
	{
		// create
		CachedDemographic cachedDemographic=CachedDemographicTest.createPersistedCachedDemographic(1234);
		CachedDemographicNote note=new CachedDemographicNote();
		note.setCaisiDemographicId(cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId());
		note.setEncounterType("blah");
		
		CachedDemographicNoteCompositePk pk=new CachedDemographicNoteCompositePk();
		pk.setUuid("1234");
		pk.setIntegratorFacilityId(cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		note.setCachedDemographicNoteCompositePk(pk);
		
		note.setNote("my notes");
		note.setObservationDate(new Date());
		note.setUpdateDate(new Date());
		
		CachedProvider provider=CachedProviderTest.createPersistedProvider(44322);
		note.setObservationCaisiProviderId(provider.getFacilityIdStringCompositePk().getCaisiItemId());
		
		note.setRole("my role");
		
		CachedProgram program=CachedProgramTest.createPersistedProgram(4342);
		note.setCaisiProgramId(program.getFacilityIdIntegerCompositePk().getCaisiItemId());
		
		HashSet<NoteIssue> issues=new HashSet<NoteIssue>();
		
		NoteIssue issue1=new NoteIssue();
		issue1.setCodeType(CodeType.ICD10);
		issue1.setIssueCode("code1");
		issues.add(issue1);
		
		NoteIssue issue2=new NoteIssue();
		issue2.setCodeType(CodeType.ICD10);
		issue2.setIssueCode("code2");
		issues.add(issue2);
		
		note.setIssues(issues);
		
		cachedDemographicNoteDao.persist(note);
		
		//--- test retrieve ---
		CachedDemographicNote cachedDemographicNote2=cachedDemographicNoteDao.find(note.getId());
		assertEquals(2, cachedDemographicNote2.getIssues().size());
		Set<NoteIssue> issues2=cachedDemographicNote2.getIssues();
		for (NoteIssue issue : issues2)
		{
			assertEquals(CodeType.ICD10, issue.getCodeType());
			
			if (!issue.getIssueCode().equals("code1") && !issue.getIssueCode().equals("code2"))
			{
				fail("Wrong issue code type : "+issue.getIssueCode());
			}
		}
		
		//--- test find ---
		
		//create a second note
		CachedDemographicNote note2=new CachedDemographicNote();
		note2.setCaisiDemographicId(cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId());
		note2.setEncounterType("blah");
		
		CachedDemographicNoteCompositePk pk2=new CachedDemographicNoteCompositePk();
		pk2.setUuid("2345");
		pk2.setIntegratorFacilityId(cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		note2.setCachedDemographicNoteCompositePk(pk2);
		
		note2.setNote("my notes");
		note2.setObservationDate(new Date());
		note2.setUpdateDate(new Date());
		note2.setObservationCaisiProviderId(provider.getFacilityIdStringCompositePk().getCaisiItemId());
		note2.setRole("my role");
		note2.setCaisiProgramId(program.getFacilityIdIntegerCompositePk().getCaisiItemId());
		
		HashSet<NoteIssue> issues3=new HashSet<NoteIssue>();
		
		NoteIssue issue3=new NoteIssue();
		issue3.setCodeType(CodeType.ICD10);
		issue3.setIssueCode("code2");
		issues3.add(issue3);
		
		NoteIssue issue4=new NoteIssue();
		issue4.setCodeType(CodeType.ICD10);
		issue4.setIssueCode("code3");
		issues3.add(issue4);
		
		note2.setIssues(issues3);
		
		cachedDemographicNoteDao.persist(note2);
		
		//find
		
		List<CachedDemographicNote> results=cachedDemographicNoteDao.findByDemographic(cachedDemographic.getFacilityIdIntegerCompositePk());
		assertEquals(2, results.size());		
	}
    
    private CachedDemographicNote createFirstIssueTestNote(String uuid, CachedDemographic cachedDemographic, CachedProgram program, GregorianCalendar observationDate, CachedProvider cachedProvider, NoteIssue noteIssue)
    {
		CachedDemographicNoteCompositePk pk=new CachedDemographicNoteCompositePk();
		pk.setUuid(uuid);
		pk.setIntegratorFacilityId(cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
    	
		CachedDemographicNote note=new CachedDemographicNote();
		note.setCachedDemographicNoteCompositePk(pk);
		note.setCaisiDemographicId(cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId());
		note.setEncounterType("blah");
		note.setObservationDate(observationDate.getTime());		
		note.setUpdateDate(new Date());
		note.setObservationCaisiProviderId(cachedProvider.getFacilityIdStringCompositePk().getCaisiItemId());
		note.setRole("myRole");
		note.setCaisiProgramId(program.getFacilityIdIntegerCompositePk().getCaisiItemId());

		HashSet<NoteIssue> issues=new HashSet<NoteIssue>();
		issues.add(noteIssue);
		
		note.setIssues(issues);
		cachedDemographicNoteDao.persist(note);
		
		return(note);
    }
    
    @Test
	public void firstIssueTest()
    {
    	// must test the following cases
    	//
    	// - single note for an issue
    	//    - issue is the one we want
    	//    - issue is not the one we want
    	//
    	// - multiple notes for an issue
    	//    - where first occurance is out of the time frame
    	//    - where first occurance is within the time frame
    	
    	
    	GregorianCalendar startTimeFrame=new GregorianCalendar(2008, 1, 1);
    	GregorianCalendar endTimeFrame=new GregorianCalendar(2009, 1, 1);
    	GregorianCalendar betweenTime1=new GregorianCalendar(2008, 4, 1);
    	GregorianCalendar betweenTime2=new GregorianCalendar(2008, 8, 1);
    	GregorianCalendar beforeTime=new GregorianCalendar(2007, 1, 1);
    	
		CachedProvider provider=CachedProviderTest.createPersistedProvider(443221);
		
		CachedProgram program=CachedProgramTest.createPersistedProgram(43429);

		NoteIssue issue1=new NoteIssue();
		issue1.setCodeType(CodeType.ICD10);
		issue1.setIssueCode("code1x");
		
		NoteIssue issue2=new NoteIssue();
		issue2.setCodeType(CodeType.ICD10);
		issue2.setIssueCode("code2x");

		// person with code1 issue
		CachedDemographic cachedDemographic1=CachedDemographicTest.createPersistedCachedDemographic(44341);
		createFirstIssueTestNote("n1", cachedDemographic1, program, betweenTime1, provider, issue1);

    	// one person with code2 issue
		CachedDemographic cachedDemographic2=CachedDemographicTest.createPersistedCachedDemographic(44342);		
		CachedDemographicNote note2=createFirstIssueTestNote("n2", cachedDemographic2, program, betweenTime2, provider, issue2);

    	// another person with code2 issue with 2 notes with in the time 
		CachedDemographic cachedDemographic3=CachedDemographicTest.createPersistedCachedDemographic(44343);
		CachedDemographicNote note3=createFirstIssueTestNote("n3", cachedDemographic3, program, betweenTime1, provider, issue2);
		createFirstIssueTestNote("n4", cachedDemographic3, program, betweenTime2, provider, issue2);


		// another person with code2 issue with 2 notes where first time is before the time frame 
		CachedDemographic cachedDemographic5=CachedDemographicTest.createPersistedCachedDemographic(44344);
		createFirstIssueTestNote("n5", cachedDemographic5, program, beforeTime, provider, issue2);
		createFirstIssueTestNote("n6", cachedDemographic5, program, betweenTime1, provider, issue2);

		// run the search and check results
		ArrayList<CachedDemographicNote> results=cachedDemographicNoteDao.findFirstInstanceOfIssueByIssueCodeAndDate(issue2, startTimeFrame, endTimeFrame);
		assertEquals(2, results.size());
		// the results are sorted by time so 
		// [0] should be the 2 note within time frame person
		// [1] should be the 1 note within time frame person as we used the second time for that note.
		assertEquals(note3.getCachedDemographicNoteCompositePk(), results.get(0).getCachedDemographicNoteCompositePk());
		assertEquals(note2.getCachedDemographicNoteCompositePk(), results.get(1).getCachedDemographicNoteCompositePk());
		
    }
}

