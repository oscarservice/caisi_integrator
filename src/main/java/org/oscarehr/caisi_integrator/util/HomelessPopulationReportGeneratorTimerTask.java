package org.oscarehr.caisi_integrator.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimerTask;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedAdmission;
import org.oscarehr.caisi_integrator.dao.CachedAdmissionDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssueDao;
import org.oscarehr.caisi_integrator.dao.CachedProgram;
import org.oscarehr.caisi_integrator.dao.CachedProgramDao;
import org.oscarehr.caisi_integrator.dao.DemographicLinkDao;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReport;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReportDao;

public final class HomelessPopulationReportGeneratorTimerTask extends TimerTask
{
	private static final Logger logger=MiscUtils.getLogger();
	private static final long RUN_PERIOD=DateUtils.MILLIS_PER_HOUR;
	private static HomelessPopulationReportGeneratorTimerTask populationReportsCacheTimerTask=null;

	private HomelessPopulationReportDao homelessPopulationReportDao=(HomelessPopulationReportDao)SpringUtils.getBean("homelessPopulationReportDao");
	private DemographicLinkDao demographicLinkDao=(DemographicLinkDao)SpringUtils.getBean("demographicLinkDao");
	private CachedAdmissionDao cachedAdmissionDao=(CachedAdmissionDao)SpringUtils.getBean("cachedAdmissionDao");
	private CachedProgramDao cachedProgramDao=(CachedProgramDao)SpringUtils.getBean("cachedProgramDao");
	private CachedDemographicIssueDao cachedDemographicIssueDao=(CachedDemographicIssueDao)SpringUtils.getBean("cachedDemographicIssueDao");
	
	public static synchronized void startReportGeneration()
	{
		if (populationReportsCacheTimerTask==null)
		{
			populationReportsCacheTimerTask=new HomelessPopulationReportGeneratorTimerTask();
			MiscUtils.sharedTimer.schedule(populationReportsCacheTimerTask, 10000, RUN_PERIOD);
			logger.info("Started Population Report Generator. CheckPeriod="+RUN_PERIOD);
		}
		else
		{
			logger.warn("startReportGeneration called but is already running.");
		}
	}
	
	public static synchronized void stopReportGeneration()
	{
		if (populationReportsCacheTimerTask!=null)
		{
			populationReportsCacheTimerTask.cancel();
			populationReportsCacheTimerTask=null;
			logger.info("Stopped Population Report Generator.");
		}
		else
		{
			logger.warn("stopReportGeneration called but is not currently running.");
		}		
	}
	
	@Override
	public void run()
	{
		LoggedInInfoInternalThread.setLoggedInInfoToCurrentClassAndThreadName();
		try
		{
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			logger.debug("Checking for need to generate population report.");
			
			// only run report if one hasn't already been run today.
			
			HomelessPopulationReport previousPopulationReport=homelessPopulationReportDao.findFirstReportBeforeThisTime(new GregorianCalendar());

			if (previousPopulationReport==null || !DateUtils.isSameDay(new GregorianCalendar(), previousPopulationReport.getReportTime()))
			{
				long timer=System.currentTimeMillis();
				
				generatePopulationReport();
				
				logger.debug("HomelessPopulationReport runtime : "+((System.currentTimeMillis()-timer)/1000)+" seconds");
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
		}
		finally
		{
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
			LoggedInInfo.loggedInInfo.remove();
		}
	}

	private void generatePopulationReport()
	{
		logger.debug("Generating population report.");

		HashMap<FacilityIdIntegerCompositePk,FacilityIdIntegerCompositePk> masterClientLinkIdCache=new HashMap<FacilityIdIntegerCompositePk, FacilityIdIntegerCompositePk>();		
		HomelessPopulationReport homelessPopulationReport=new HomelessPopulationReport();
		homelessPopulationReport.setReportTime(new GregorianCalendar());
				
		//--- Get usage in last year ---
		// get every admission that was discharged in last year or not discharged yet
		// then group the clients according to their links.
		GregorianCalendar oneYearAgo=new GregorianCalendar();
		oneYearAgo.add(GregorianCalendar.YEAR, -1);
		ArrayList<CachedAdmission> wereInProgramWithinLastYear=getDischargeToReportProgramsAfterDate(oneYearAgo);
		HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>> admissionsGroupedByLinkedClientsInProgramsWithinLastYear=getAdmissionsInLinkedGroups(masterClientLinkIdCache, wereInProgramWithinLastYear);
		homelessPopulationReport.setClientCountInPastYear(admissionsGroupedByLinkedClientsInProgramsWithinLastYear.size());
		
		//--- Get current usage ---
		ArrayList<CachedAdmission> currentlyInPrograms=getStillInPrograms(wereInProgramWithinLastYear);
		HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>> admissionsGroupedByLinkedClientsCurrentlyInPrograms=getAdmissionsInLinkedGroups(masterClientLinkIdCache, currentlyInPrograms);
		homelessPopulationReport.setCurrentClientCount(admissionsGroupedByLinkedClientsCurrentlyInPrograms.size());		
		
		//--- get intensity of use ---
		GregorianCalendar fourYearsAgo=new GregorianCalendar();
		fourYearsAgo.add(GregorianCalendar.YEAR, -4);
		ArrayList<CachedAdmission> wereInProgramWithinLast4Years=getDischargeToReportProgramsAfterDate(fourYearsAgo);
		HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>> admissionsGroupedByLinkedClientsInLast4Years=getAdmissionsInLinkedGroups(masterClientLinkIdCache, wereInProgramWithinLast4Years);
		HashMap<FacilityIdIntegerCompositePk,Integer> usageCounts=getUsageCounts(admissionsGroupedByLinkedClientsInLast4Years);
		homelessPopulationReport.setUsage1To10DaysInPast4Years(getUsageRange(1,10,usageCounts));
		homelessPopulationReport.setUsage11To179DaysInPast4Years(getUsageRange(11,179,usageCounts));
		homelessPopulationReport.setUsage180To730DaysInPast4Years(getUsageRange(179,730,usageCounts));
		
		//--- mortality ---
		ArrayList<CachedAdmission> deceasedWithinLast4Years=getAdmittedToDeceasedProgramsAfterDate(oneYearAgo);
		HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>> deceasedByLinkedGroups=getAdmissionsInLinkedGroups(masterClientLinkIdCache, deceasedWithinLast4Years);
		homelessPopulationReport.setMortalityInPastYear(deceasedByLinkedGroups.size());
		
		//--- issues ---
		ArrayList<HashSet<FacilityIdIntegerCompositePk>> clientsGrouped=getClientsGrouped(admissionsGroupedByLinkedClientsInProgramsWithinLastYear);
		homelessPopulationReport.setCurrentHiv(getCurrentUnresolvedIssueCount(clientsGrouped, "HIV"));
		homelessPopulationReport.setCurrentCancer(getCurrentUnresolvedIssueCount(clientsGrouped, "CANCER"));
		homelessPopulationReport.setCurrentDiabetes(getCurrentUnresolvedIssueCount(clientsGrouped, "DIABETES"));
		homelessPopulationReport.setCurrentSeizure(getCurrentUnresolvedIssueCount(clientsGrouped, "SEIZURE"));
		homelessPopulationReport.setCurrentLiverDisease(getCurrentUnresolvedIssueCount(clientsGrouped, "LIVER_DISEASE"));
		homelessPopulationReport.setCurrentSchizophrenia(getCurrentUnresolvedIssueCount(clientsGrouped, "SCHIZOPHRENIA"));
		homelessPopulationReport.setCurrentBipolar(getCurrentUnresolvedIssueCount(clientsGrouped, "BIPOLAR"));
		homelessPopulationReport.setCurrentDepression(getCurrentUnresolvedIssueCount(clientsGrouped, "DEPRESSION"));
		homelessPopulationReport.setCurrentCognitiveDisability(getCurrentUnresolvedIssueCount(clientsGrouped, "COGNITIVE_DISABILITY"));
		homelessPopulationReport.setCurrentPneumonia(getCurrentUnresolvedIssueCount(clientsGrouped, "PNEUMONIA"));
		homelessPopulationReport.setCurrentInfluenza(getCurrentUnresolvedIssueCount(clientsGrouped, "INFLUENZA"));
		
		homelessPopulationReportDao.persist(homelessPopulationReport);

		logger.debug("Population report generation complete.");
	}
	
	private int getCurrentUnresolvedIssueCount(ArrayList<HashSet<FacilityIdIntegerCompositePk>> clientsGrouped, String issueGroupName)
	{
		int counter=0;
		
		for (HashSet<FacilityIdIntegerCompositePk> clientGroup : clientsGrouped)
		{
			if (hasUnresolvedIssue(clientGroup, issueGroupName)) counter++;
		}
		
		return(counter);
	}

	/**
	 * Check if any of the clients in the given group has any of the issues.
	 * Can do short circuit evaluation.
	 */
	private boolean hasUnresolvedIssue(HashSet<FacilityIdIntegerCompositePk> clientGroup, String issueGroupName)
	{
		for (FacilityIdIntegerCompositePk clientPk : clientGroup)
		{
			boolean hasUnresolvedIssue=cachedDemographicIssueDao.hasUnresolvedIssueFromIssueGroup(clientPk.getIntegratorFacilityId(), clientPk.getCaisiItemId(), issueGroupName);
			if (hasUnresolvedIssue) return(true);
		}
		
		return(false);
	}


	private ArrayList<HashSet<FacilityIdIntegerCompositePk>> getClientsGrouped(HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>> admissionsGroupedByLinkedClientsInProgramsWithinLastYear)
	{
		ArrayList<HashSet<FacilityIdIntegerCompositePk>> results=new ArrayList<HashSet<FacilityIdIntegerCompositePk>>();
		
		for (ArrayList<CachedAdmission> cachedAdmissions : admissionsGroupedByLinkedClientsInProgramsWithinLastYear.values())
		{
			results.add(getUniqueClientIds(cachedAdmissions));
		}
		
		return(results);
	}

	private HashSet<FacilityIdIntegerCompositePk> getUniqueClientIds(ArrayList<CachedAdmission> cachedAdmissions)
	{
		HashSet<FacilityIdIntegerCompositePk> results=new HashSet<FacilityIdIntegerCompositePk>();
		
		for (CachedAdmission admission : cachedAdmissions)
		{
			results.add(new FacilityIdIntegerCompositePk(admission.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), admission.getCaisiDemographicId()));
		}
		
		return(results);
	}

	private int getUsageRange(int min, int max, HashMap<FacilityIdIntegerCompositePk, Integer> usageCounts)
	{
		int counter=0;

		for (Integer value : usageCounts.values())
		{
			if (value>=min && value<=max) counter++;
		}

		return(counter);
	}

	private HashMap<FacilityIdIntegerCompositePk,Integer> getUsageCounts(HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>> admissionsGroupedByLinkedClientsInLast4Years)
	{
		HashMap<FacilityIdIntegerCompositePk,Integer> results=new HashMap<FacilityIdIntegerCompositePk,Integer>();
		GregorianCalendar fourYearsAgo=new GregorianCalendar();
		fourYearsAgo.add(GregorianCalendar.YEAR, -4);
		
		for (FacilityIdIntegerCompositePk pk : admissionsGroupedByLinkedClientsInLast4Years.keySet())
		{
			results.put(pk, getTotalDays(fourYearsAgo, admissionsGroupedByLinkedClientsInLast4Years.get(pk)));
		}
		
		return(results);
	}

	private static Integer getTotalDays(GregorianCalendar maxTimeToLookBack, ArrayList<CachedAdmission> admissions)
	{
		int dayCounter=0;		
		
		for (CachedAdmission admission : admissions)
		{
			GregorianCalendar calculationStartDate=new GregorianCalendar();
			calculationStartDate.setTimeInMillis(Math.max(maxTimeToLookBack.getTimeInMillis(), admission.getAdmissionDate().getTime()));
			
			GregorianCalendar calculationEndDate=new GregorianCalendar();
			if (admission.getDischargeDate()!=null) calculationEndDate.setTimeInMillis(admission.getDischargeDate().getTime());
			
			// add half a day so it rounds up or down instead of always down.
			long ms=calculationEndDate.getTimeInMillis()-calculationStartDate.getTimeInMillis()+(DateUtils.MILLIS_PER_HOUR*12);
			
			// days are calculated per admission so 2 different 6 hours admissions is considered 2 days, not 1
			dayCounter=(int)(dayCounter+(ms/DateUtils.MILLIS_PER_DAY));
		}
		
		return(dayCounter);
	}

	private static ArrayList<CachedAdmission> getStillInPrograms(ArrayList<CachedAdmission> wereInProgramWithinLastYear)
	{
		ArrayList<CachedAdmission> results=new ArrayList<CachedAdmission>();
		
		for (CachedAdmission admission : wereInProgramWithinLastYear)
		{
			if (admission.getDischargeDate()==null) results.add(admission);
		}
		
		return(results);
	}

	private ArrayList<CachedAdmission> getAdmittedToDeceasedProgramsAfterDate(GregorianCalendar afterThisDate)
	{
		List<CachedProgram> programs=cachedProgramDao.findByName("Deceased");

		ArrayList<CachedAdmission> allAdmissions=new ArrayList<CachedAdmission>();
		
		for (CachedProgram program : programs)
		{
			List<CachedAdmission> tempAdmissions=cachedAdmissionDao.findAdmissionsToProgramAfterDate(program.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), program.getFacilityIdIntegerCompositePk().getCaisiItemId(), afterThisDate);
			allAdmissions.addAll(tempAdmissions);
		}
		
		return(allAdmissions);
	}

	private ArrayList<CachedAdmission> getDischargeToReportProgramsAfterDate(GregorianCalendar afterThisDate)
	{
		ArrayList<FacilityIdIntegerCompositePk> programIds=homelessPopulationReportDao.getProgramsUsedReport(new GregorianCalendar());
		
		ArrayList<CachedAdmission> allAdmissions=new ArrayList<CachedAdmission>();
		
		for (FacilityIdIntegerCompositePk programId : programIds)
		{
			List<CachedAdmission> tempAdmissions=cachedAdmissionDao.findDischargesToProgramAfterDate(programId.getIntegratorFacilityId(), programId.getCaisiItemId(), afterThisDate);
			allAdmissions.addAll(tempAdmissions);
		}
		
		return(allAdmissions);
	}

	private HashMap<FacilityIdIntegerCompositePk,ArrayList<CachedAdmission>> getAdmissionsInLinkedGroups(HashMap<FacilityIdIntegerCompositePk,FacilityIdIntegerCompositePk> masterClientLinkIdCache, ArrayList<CachedAdmission> admissions)
	{
		HashMap<FacilityIdIntegerCompositePk,ArrayList<CachedAdmission>> results=new HashMap<FacilityIdIntegerCompositePk, ArrayList<CachedAdmission>>();
		
		for (CachedAdmission admission : admissions)
		{
			// get master client link id
			// add admission to the master client link
			
			//--- find master link ---
			FacilityIdIntegerCompositePk currentClientId=new FacilityIdIntegerCompositePk(admission.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), admission.getCaisiDemographicId());
			FacilityIdIntegerCompositePk masterClientLinkId=masterClientLinkIdCache.get(currentClientId);
			if (masterClientLinkId==null)
			{
				TreeSet<FacilityIdIntegerCompositePk> linkedIds=demographicLinkDao.getAllLinkedDemographics(currentClientId, false);
				masterClientLinkId=linkedIds.first();
				masterClientLinkIdCache.put(currentClientId, masterClientLinkId);
			}
			
			//--- get the bucket for the master link ---
			ArrayList<CachedAdmission> resultBucket=results.get(masterClientLinkId);
			if (resultBucket==null)
			{
				resultBucket=new ArrayList<CachedAdmission>();
				results.put(masterClientLinkId, resultBucket);
			}
			
			resultBucket.add(admission);
		}
		
		return(results);
	}
}
