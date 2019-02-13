package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedAdmissionTest extends DaoTestFixtures
{
    private static CachedAdmissionDao cachedAdmissionDao=(CachedAdmissionDao)SpringUtils.getBean("cachedAdmissionDao");
	
	public static CachedAdmission createPersistedAdmission(int admissionId, int demographicId)
	{
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(admissionId);

		CachedAdmission cachedAdmission=new CachedAdmission();
		cachedAdmission.setFacilityIdIntegerCompositePk(pk);
		cachedAdmission.setAdmissionDate(new Date());
		cachedAdmission.setAdmissionNotes("admission note");
		cachedAdmission.setCaisiDemographicId(demographicId);
		cachedAdmission.setCaisiProgramId(5678);
		cachedAdmissionDao.persist(cachedAdmission);
		
		return(cachedAdmission);
	}
	
	@Test
	public void admissionTest()
	{
		// create
		CachedAdmission cachedAdmission=createPersistedAdmission(4432, 8872);
		
	    // read 
		cachedAdmission=cachedAdmissionDao.find(cachedAdmission.getFacilityIdIntegerCompositePk());
		assertEquals("admission note",cachedAdmission.getAdmissionNotes());
		assertNotNull(cachedAdmission.getAdmissionDate());
		assertEquals(8872,cachedAdmission.getCaisiDemographicId());
		assertEquals(5678,cachedAdmission.getCaisiProgramId());
		assertNull(cachedAdmission.getDischargeDate());
		assertNull(cachedAdmission.getDischargeNotes());
		
	    // update
		cachedAdmission.setDischargeDate(new Date());
		cachedAdmission.setDischargeNotes("discharge notes");
		cachedAdmissionDao.merge(cachedAdmission);

		cachedAdmission=cachedAdmissionDao.find(cachedAdmission.getFacilityIdIntegerCompositePk());
		assertEquals("discharge notes",cachedAdmission.getDischargeNotes());
		assertNotNull(cachedAdmission.getDischargeDate());
	    
	    // delete
		cachedAdmissionDao.remove(cachedAdmission.getFacilityIdIntegerCompositePk());
		cachedAdmission=cachedAdmissionDao.find(cachedAdmission.getFacilityIdIntegerCompositePk());
	    assertNull(cachedAdmission);
	}
	
	@Test
	public void findDischargeTest()
	{
		createPersistedAdmission(5432, 8879);
		createPersistedAdmission(6432, 8880);
		createPersistedAdmission(7432, 8880);

		List<CachedAdmission> results=cachedAdmissionDao.findByFacilityIdAndDemographicId(facility1.getId(), 8879);
		assertEquals(1, results.size());
		
		results=cachedAdmissionDao.findByFacilityIdAndDemographicId(facility1.getId(), 8880);
		assertEquals(2, results.size());
		
		//--- after a specified date
		GregorianCalendar future1Year=new GregorianCalendar();
		future1Year.add(Calendar.YEAR, 1);

		GregorianCalendar future6Months=new GregorianCalendar();
		future6Months.add(Calendar.MONTH, 6);
		
		GregorianCalendar future3Months=new GregorianCalendar();
		future3Months.add(Calendar.MONTH, 3);

		CachedAdmission cachedAdmission=createPersistedAdmission(7711, 8879);
		cachedAdmission.setAdmissionDate(future3Months.getTime());
		cachedAdmission.setDischargeDate(future6Months.getTime());
		cachedAdmissionDao.merge(cachedAdmission);
		
		cachedAdmission=createPersistedAdmission(7712, 8880);
		cachedAdmission.setAdmissionDate(future3Months.getTime());
		cachedAdmission.setDischargeDate(future6Months.getTime());
		cachedAdmissionDao.merge(cachedAdmission);

		results=cachedAdmissionDao.findDischargesToProgramAfterDate(facility1.getId(), cachedAdmission.getCaisiProgramId(), new GregorianCalendar());
		int allNotDischargedCountRightNow=results.size();
		assertTrue(allNotDischargedCountRightNow>=5);
		
		results=cachedAdmissionDao.findDischargesToProgramAfterDate(facility1.getId(), cachedAdmission.getCaisiProgramId(), future1Year);
		// we manually discharged 2 people 6 months in the future
		assertEquals(allNotDischargedCountRightNow-2, results.size());
		
		// admitted after a certain time
		results=cachedAdmissionDao.findAdmissionsToProgramAfterDate(facility1.getId(), cachedAdmission.getCaisiProgramId(), new GregorianCalendar());
		assertEquals(2, results.size());
	}
}
