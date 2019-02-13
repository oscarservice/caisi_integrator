package org.oscarehr.caisi_integrator.site.ui.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Transient;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssueDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNote;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNoteDao;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.NoteIssue;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
@SessionScoped
public class IssueReportJsfBean implements Serializable
{
	@Transient
	private CachedFacilityDao cachedFacilityDao = (CachedFacilityDao)SpringUtils.getBean("cachedFacilityDao");

	@Transient
	private CachedDemographicNoteDao cachedDemographicNoteDao = (CachedDemographicNoteDao)SpringUtils.getBean("cachedDemographicNoteDao");

	@Transient
	private CachedDemographicIssueDao cachedDemographicIssueDao = (CachedDemographicIssueDao)SpringUtils.getBean("cachedDemographicIssueDao");

	private String[] selectedIssues = null;
	private String startYear = null;
	private String startMonth = null;
	private String startDay = null;
	private String endYear = null;
	private String endMonth = null;
	private String endDay = null;

	private ArrayList<IssueDisplayRow> resultRows=null;
	private TreeMap<NoteIssue, String> reportedIssues=null;
	
	private void populateReportedIssuesIfNotPopulated()
	{
		if (reportedIssues==null)
		{
			reportedIssues=cachedDemographicIssueDao.findReportedIssues();
		}
	}
	
	public ArrayList<SelectItem> getListOfReportedIssueCodes()
	{
		ArrayList<SelectItem> selectItemIssues = new ArrayList<SelectItem>();

		populateReportedIssuesIfNotPopulated();
		
		for (NoteIssue issue : reportedIssues.keySet())
		{
			selectItemIssues.add(new SelectItem(issue.toString(), issue.toString() + " (" + reportedIssues.get(issue) + ')'));
		}

		return(selectItemIssues);
	}

	public void setSelectedIssues(String[] selected)
	{
		selectedIssues = selected;
	}

	public String[] getSelectedIssues()
	{
		return(selectedIssues);
	}

	public String getStartYear()
	{
		return startYear;
	}

	public void setStartYear(String startYear)
	{
		this.startYear = startYear;
	}

	public String getStartMonth()
	{
		return startMonth;
	}

	public void setStartMonth(String startMonth)
	{
		this.startMonth = startMonth;
	}

	public String getStartDay()
	{
		return startDay;
	}

	public void setStartDay(String startDay)
	{
		this.startDay = startDay;
	}

	public String getEndYear()
	{
		return endYear;
	}

	public void setEndYear(String endYear)
	{
		this.endYear = endYear;
	}

	public String getEndMonth()
	{
		return endMonth;
	}

	public void setEndMonth(String endMonth)
	{
		this.endMonth = endMonth;
	}

	public String getEndDay()
	{
		return endDay;
	}

	public void setEndDay(String endDay)
	{
		this.endDay = endDay;
	}

	private GregorianCalendar getStartCal()
	{
		return(new GregorianCalendar(Integer.parseInt(startYear), Integer.parseInt(startMonth), Integer.parseInt(startDay)));
	}

	private GregorianCalendar getEndCal()
	{
		return(new GregorianCalendar(Integer.parseInt(endYear), Integer.parseInt(endMonth), Integer.parseInt(endDay)));
	}

	public String getReportParameters()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(getStartCal()));
		sb.append(" to ");
		sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(getEndCal()));
		sb.append(" (Excusive) ");
		sb.append(Arrays.toString(selectedIssues));

		return(sb.toString());
	}

	private static final Comparator<IssueDisplayRow> ISSUE_DISPLAY_ROW_DATE_COMPARATOR=new Comparator<IssueDisplayRow>()
	{
		@Override
		public int compare(IssueDisplayRow o1, IssueDisplayRow o2)
		{
			return(o1.getOccuranceDate().compareTo(o2.getOccuranceDate()));
		}
	};
	
	public static class IssueDisplayRow implements Serializable
	{
		private String occuranceDate = null;
		private String facility = null;
		private String issueTypeCode = null;
		private String issueDescription;

		public IssueDisplayRow(String occuranceDate, String facility, String issueTypeCode, String issueDescription)
		{
			this.occuranceDate = occuranceDate;
			this.facility = facility;
			this.issueTypeCode = issueTypeCode;
			this.issueDescription = issueDescription;
		}

		public String getOccuranceDate()
		{
			return occuranceDate;
		}

		public String getFacility()
		{
			return facility;
		}

		public String getIssueTypeCode()
		{
			return issueTypeCode;
		}

		public String getIssueDescription()
		{
			return issueDescription;
		}
	}

	
	public String generateReportResults()
	{
		GregorianCalendar startCal=getStartCal();
		GregorianCalendar endCal=getEndCal();
		
		populateReportedIssuesIfNotPopulated();
		resultRows=new ArrayList<IssueDisplayRow>();
		
		for (String selectedIssue : selectedIssues)
		{
			NoteIssue noteIssue=NoteIssue.valueOf(selectedIssue);
			ArrayList<CachedDemographicNote> tempResults=cachedDemographicNoteDao.findFirstInstanceOfIssueByIssueCodeAndDate(noteIssue, startCal, endCal);

			for (CachedDemographicNote note : tempResults)
			{
				String occurranceDate=DateFormatUtils.ISO_DATE_FORMAT.format(note.getObservationDate());
				CachedFacility cachedFacility=cachedFacilityDao.find(note.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
				
				IssueDisplayRow issueDisplayRow=new IssueDisplayRow(occurranceDate, cachedFacility.getName(), selectedIssue, reportedIssues.get(noteIssue));
				resultRows.add(issueDisplayRow);
			}
		}
		
		Collections.sort(resultRows, ISSUE_DISPLAY_ROW_DATE_COMPARATOR);
		
		return("issue_report_results.jsf?"+JsfUtils.REDIRECT_QUERY_STRING);
	}
	
	public ListDataModel<IssueDisplayRow> getReportResultsForDisplay()
	{
		ListDataModel<IssueDisplayRow> results = new ListDataModel<IssueDisplayRow>(resultRows);
		return(results);
	}

	public ArrayList<IssueDisplayRow> getReportResultsForDownload()
	{
		return(resultRows);
	}
	
	public String getReportDownloadFilename()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("issues_report_");
		sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(getStartCal()));
		sb.append("_to_");
		sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(getEndCal()));
		
		sb.append(".csv");
		
		return(sb.toString());
	}

}
