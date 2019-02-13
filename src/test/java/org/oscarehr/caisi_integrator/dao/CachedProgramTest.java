package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.dao.CachedDemographic.Gender;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedProgramTest extends DaoTestFixtures
{
    private static CachedProgramDao cachedProgramDao=(CachedProgramDao)SpringUtils.getBean("cachedProgramDao");
	
	public static CachedProgram createPersistedProgram(int seed)
	{
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(seed);

		CachedProgram cachedProgram=new CachedProgram();
		cachedProgram.setFacilityIdIntegerCompositePk(pk);
		cachedProgram.setDescription("the desc");
		cachedProgram.setFirstNation(true);
		cachedProgram.setGender(Gender.T);
		cachedProgram.setMaxAge(35);
		cachedProgram.setMinAge(17);
		cachedProgram.setName("the name");
		cachedProgram.setStatus("active");
		cachedProgram.setType("bed");
		cachedProgramDao.persist(cachedProgram);
		
		return(cachedProgram);
	}
	
	@Test
	public void programTest()
	{
		// create
		
		CachedProgram cachedProgram=createPersistedProgram(4432);
		
	    // read 
		cachedProgram=cachedProgramDao.find(cachedProgram.getFacilityIdIntegerCompositePk());
		assertEquals("the desc",cachedProgram.getDescription());
		assertTrue(cachedProgram.isFirstNation());
		assertEquals(Gender.T,cachedProgram.getGender());
		assertEquals(35,cachedProgram.getMaxAge());
		assertEquals(17,cachedProgram.getMinAge());
		assertEquals("the name",cachedProgram.getName());
		assertEquals("active",cachedProgram.getStatus());
		assertEquals("bed",cachedProgram.getType());
		
	    // update
		cachedProgram.setDescription("new descroption");
		cachedProgramDao.merge(cachedProgram);

		cachedProgram=cachedProgramDao.find(cachedProgram.getFacilityIdIntegerCompositePk());
		assertEquals("new descroption",cachedProgram.getDescription());
	    
	    // delete
		cachedProgramDao.remove(cachedProgram.getFacilityIdIntegerCompositePk());
		cachedProgram=cachedProgramDao.find(cachedProgram.getFacilityIdIntegerCompositePk());
	    assertNull(cachedProgram);
	}


	public void findByNameTest()
	{
		CachedProgram cachedProgram=createPersistedProgram(4432);
		cachedProgram.setName("foo1");
		cachedProgramDao.merge(cachedProgram);
		
		CachedProgram cachedProgram1=createPersistedProgram(4432);
		cachedProgram1.setName("foo1");
		cachedProgramDao.merge(cachedProgram1);
		
		List<CachedProgram> results=cachedProgramDao.findByName("foo1");
		assertEquals(2, results.size());
	}
}
