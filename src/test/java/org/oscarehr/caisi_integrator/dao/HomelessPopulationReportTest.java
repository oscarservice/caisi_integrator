package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class HomelessPopulationReportTest extends DaoTestFixtures
{
	private static HomelessPopulationReportDao homelessPopulationReportDao=(HomelessPopulationReportDao)SpringUtils.getBean("homelessPopulationReportDao");
	
	@Test
	public void populationReportTest() throws InterruptedException
	{
		GregorianCalendar cal1=new GregorianCalendar(2009, 1, 1);
		HomelessPopulationReport populationReport1=new HomelessPopulationReport();
		populationReport1.setReportTime(cal1);
		homelessPopulationReportDao.persist(populationReport1);
				
		GregorianCalendar cal3=new GregorianCalendar(2009, 3, 3);
		HomelessPopulationReport populationReport3=new HomelessPopulationReport();
		populationReport3.setReportTime(cal3);
		homelessPopulationReportDao.persist(populationReport3);
		
		GregorianCalendar cal2=new GregorianCalendar(2009, 2, 2);
		HomelessPopulationReport populationReport2=new HomelessPopulationReport();
		populationReport2.setReportTime(cal2);
		homelessPopulationReportDao.persist(populationReport2);
		
		HomelessPopulationReport latest=homelessPopulationReportDao.findFirstReportBeforeThisTime(new GregorianCalendar());
		assertEquals(populationReport3.getId(), latest.getId());

		//---
		// test configuring programs
		
		ArrayList<FacilityIdIntegerCompositePk> programsInReport=new ArrayList<FacilityIdIntegerCompositePk>();
		programsInReport.add(new FacilityIdIntegerCompositePk(4,5));
		programsInReport.add(new FacilityIdIntegerCompositePk(5,6));
		homelessPopulationReportDao.setProgramsToUseInReports(programsInReport);
		
		GregorianCalendar betweenTime=new GregorianCalendar();
		Thread.sleep(1000);
		
		ArrayList<FacilityIdIntegerCompositePk> programsInReport2=new ArrayList<FacilityIdIntegerCompositePk>();
		programsInReport2.add(new FacilityIdIntegerCompositePk(8,9));
		programsInReport2.add(new FacilityIdIntegerCompositePk(9,10));
		programsInReport2.add(new FacilityIdIntegerCompositePk(9,14));
		homelessPopulationReportDao.setProgramsToUseInReports(programsInReport2);
		
		ArrayList<FacilityIdIntegerCompositePk> results=homelessPopulationReportDao.getProgramsUsedReport(betweenTime);
		assertEquals(2, results.size());

		ArrayList<FacilityIdIntegerCompositePk> results2=homelessPopulationReportDao.getProgramsUsedReport(new GregorianCalendar());
		assertEquals(3, results2.size());
		
		//---
		// test find data range

		// should exclude the last one so it should return the first 2 only 
		List<HomelessPopulationReport> rangeResults=homelessPopulationReportDao.findByDateRange(cal1, cal3);
		assertEquals(2, rangeResults.size());
		
		//--- find first 
		HomelessPopulationReport first=homelessPopulationReportDao.findFirstReport();
		assertEquals(populationReport1.getId(), first.getId());
	}
}
