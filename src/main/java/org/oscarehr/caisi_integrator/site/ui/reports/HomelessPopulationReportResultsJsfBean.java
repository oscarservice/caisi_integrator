package org.oscarehr.caisi_integrator.site.ui.reports;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;
import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.CachedProgram;
import org.oscarehr.caisi_integrator.dao.CachedProgramDao;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReport;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReportDao;
import org.oscarehr.caisi_integrator.util.Named;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class HomelessPopulationReportResultsJsfBean implements Serializable
{
	@Transient
	private CachedFacilityDao cachedFacilityDao = (CachedFacilityDao)SpringUtils.getBean("cachedFacilityDao");
	
	@Transient
	private CachedProgramDao cachedProgramDao = (CachedProgramDao)SpringUtils.getBean("cachedProgramDao");

	@Transient
	private HomelessPopulationReportDao homelessPopulationReportDao = (HomelessPopulationReportDao)SpringUtils.getBean("homelessPopulationReportDao");

	private Integer reportId = null;
	private HomelessPopulationReport homelessPopulationReport = null;
	private ArrayList<ProgramDisplayRow> programsUsedInReport=null;
	private NumberFormat percentInstance = NumberFormat.getPercentInstance();

	public HomelessPopulationReportResultsJsfBean()
	{
		percentInstance.setMinimumFractionDigits(0);
		percentInstance.setMaximumFractionDigits(2);
	}

	public void setReportId(Integer reportId)
	{
		this.reportId = reportId;
	}

	public Integer getReportId()
	{
		return(reportId);
	}

	public HomelessPopulationReport getHomelessPopulationReport()
	{
		if (homelessPopulationReport == null)
		{
			if (reportId == null) homelessPopulationReport = homelessPopulationReportDao.findFirstReportBeforeThisTime(new GregorianCalendar());
			else homelessPopulationReport = homelessPopulationReportDao.find(reportId);
		}

		return(homelessPopulationReport);
	}

	private String getRateString(int numerator, int denominator)
	{
		float f = (float)Math.max(numerator, 5) / Math.max(denominator, 5);

		StringBuilder sb = new StringBuilder();
		if (numerator < 5 || denominator < 5) sb.append('<');
		sb.append(percentInstance.format(f));

		return(sb.toString());
	}

	public String getMortalityRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getMortalityInPastYear(), report.getClientCountInPastYear()));
	}

	public String getCurrentHivRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentHiv(), report.getCurrentClientCount()));
	}

	public String getCurrentCancerRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentCancer(), report.getCurrentClientCount()));
	}

	public String getCurrentDiabetesRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentDiabetes(), report.getCurrentClientCount()));
	}

	public String getCurrentSeizureRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentSeizure(), report.getCurrentClientCount()));
	}

	public String getCurrentLiverDiseaseRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentLiverDisease(), report.getCurrentClientCount()));
	}

	public String getCurrentSchizophreniaRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentSchizophrenia(), report.getCurrentClientCount()));
	}

	public String getCurrentBipolarRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentBipolar(), report.getCurrentClientCount()));
	}

	public String getCurrentDepressionRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentDepression(), report.getCurrentClientCount()));
	}

	public String getCurrentCognitiveDisabilityRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentCognitiveDisability(), report.getCurrentClientCount()));
	}

	public String getCurrentPneumoniaRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentPneumonia(), report.getCurrentClientCount()));
	}

	public String getCurrentInfluenzaRate()
	{
		HomelessPopulationReport report = getHomelessPopulationReport();
		return(getRateString(report.getCurrentInfluenza(), report.getCurrentClientCount()));
	}

	public static class ProgramDisplayRow implements Serializable, Named
	{
		private String facilityName = null;
		private String programName = null;

		public String getFacilityName()
		{
			return facilityName;
		}

		public void setFacilityName(String facilityName)
		{
			this.facilityName = facilityName;
		}

		public String getProgramName()
		{
			return programName;
		}

		public void setProgramName(String programName)
		{
			this.programName = programName;
		}

		@Override
		public String getName()
		{
			return(facilityName+'.'+programName);
		}
	}
	
	public ListDataModel<ProgramDisplayRow> getProgramsUsed()
	{
		if (programsUsedInReport == null)
		{
			programsUsedInReport = new ArrayList<ProgramDisplayRow>();
			
			HomelessPopulationReport report = getHomelessPopulationReport();

			ArrayList<FacilityIdIntegerCompositePk> selectedProgramIds = homelessPopulationReportDao.getProgramsUsedReport(report.getReportTime());

			for (FacilityIdIntegerCompositePk programPk : selectedProgramIds)
			{
				CachedFacility cachedFacility=cachedFacilityDao.find(programPk.getIntegratorFacilityId());				
				CachedProgram cachedProgram=cachedProgramDao.find(programPk);

				ProgramDisplayRow programDisplayRow=new ProgramDisplayRow();
				programDisplayRow.facilityName=cachedFacility.getName();
				programDisplayRow.programName=cachedProgram.getName();
				
				programsUsedInReport.add(programDisplayRow);
			}
						
			Collections.sort(programsUsedInReport, Named.NAME_COMPARATOR);
		}

		ListDataModel<ProgramDisplayRow> results = new ListDataModel<ProgramDisplayRow>(programsUsedInReport);
		return(results);
	}
}
