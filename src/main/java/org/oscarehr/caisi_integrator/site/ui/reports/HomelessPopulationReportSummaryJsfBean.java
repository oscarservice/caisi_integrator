package org.oscarehr.caisi_integrator.site.ui.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.Transient;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReport;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReportDao;
import org.oscarehr.caisi_integrator.site.ui.reports.AllYearsPopulationAverages.YearPopulationAverages;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
@SessionScoped
// must be session scoped because selection list is generated dynamically
public class HomelessPopulationReportSummaryJsfBean implements Serializable
{
	@Transient
	private HomelessPopulationReportDao homelessPopulationReportDao = (HomelessPopulationReportDao)SpringUtils.getBean("homelessPopulationReportDao");

	private int selectedYear;
	private int selectedMonth;
	private int selectedReportId;
	private ArrayList<SelectItem> availableReports = null;
	private AllYearsPopulationAverages allYearsAvgs=null;
	private int firstYear=-1;

	public HomelessPopulationReportSummaryJsfBean()
	{
		HomelessPopulationReport report=homelessPopulationReportDao.findFirstReport();
		if (report!=null) firstYear=report.getReportTime().get(GregorianCalendar.YEAR);
		else firstYear=(new GregorianCalendar()).get(GregorianCalendar.YEAR);
		
		generateAllYearAvgsGenerated();
	}
	
	public int getSelectedYear()
	{
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear)
	{
		if (this.selectedYear != selectedYear)
		{
			availableReports = null;
			this.selectedYear = selectedYear;
		}
	}

	public int getSelectedMonth()
	{
		return selectedMonth;
	}

	public void setSelectedMonth(int selectedMonth)
	{
		if (this.selectedMonth != selectedMonth)
		{
			availableReports = null;
			this.selectedMonth = selectedMonth;
		}
	}

	public int getSelectedReportId()
	{
		return selectedReportId;
	}

	public void setSelectedReportId(int selectedReportId)
	{
		this.selectedReportId = selectedReportId;
	}

	public ArrayList<SelectItem> getAvailableYears()
	{
		ArrayList<SelectItem> availableYears = new ArrayList<SelectItem>();
		GregorianCalendar cal = new GregorianCalendar();

		for (int i = cal.get(GregorianCalendar.YEAR); i >= firstYear; i--)
		{
			availableYears.add(new SelectItem(i));
		}

		return(availableYears);
	}

	public ArrayList<SelectItem> getAvailableMonths()
	{
		ArrayList<SelectItem> availableMonths = new ArrayList<SelectItem>();

		for (int i = GregorianCalendar.JANUARY; i <= GregorianCalendar.DECEMBER; i++)
		{
			availableMonths.add(new SelectItem(i + 1));
		}

		return(availableMonths);
	}

	public ArrayList<SelectItem> getAvailableReports()
	{
		if (availableReports != null) return(availableReports);

		availableReports = new ArrayList<SelectItem>();

		if (selectedYear != 0 && selectedMonth != 0)
		{
			GregorianCalendar startDate = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
			GregorianCalendar endDate = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
			endDate.add(GregorianCalendar.MONTH, 1);

			List<HomelessPopulationReport> reports = homelessPopulationReportDao.findByDateRange(startDate, endDate);

			for (HomelessPopulationReport report : reports)
			{
				SelectItem selectItem = new SelectItem(report.getId(), DateFormatUtils.ISO_DATE_FORMAT.format(report.getReportTime()));
				selectItem.setEscape(false);
				availableReports.add(selectItem);
			}
		}

		if (availableReports.size() == 0)
		{
			SelectItem selectItem = new SelectItem("No Reports.");
			selectItem.setDisabled(true);
			availableReports.add(selectItem);
		}

		return(availableReports);
	}

	public String viewReport()
	{
		if (selectedReportId == 0) return(null);
		else return("homeless_population_report_single_results.jsf?" + JsfUtils.REDIRECT_QUERY_STRING + "&reportId=" + selectedReportId);
	}

	public AllYearsPopulationAverages getAllYearsPopulationAverages()
	{
		return(allYearsAvgs);
	}
	
	private void generateAllYearAvgsGenerated()
	{
		if (allYearsAvgs!=null) return;
		
		allYearsAvgs=new AllYearsPopulationAverages();
		
		GregorianCalendar cal = new GregorianCalendar();

		for (int i = cal.get(GregorianCalendar.YEAR); i >= firstYear; i--)
		{
			YearPopulationAverages yearAvgs=calculateYearPopulationAverages(i);
			
			allYearsAvgs.maxMonthlyAvg=Math.max(allYearsAvgs.maxMonthlyAvg, yearAvgs.getMaxMonthAvg());
			
			allYearsAvgs.allYearsPopulationAverages.add(yearAvgs);
		}
	}

	private YearPopulationAverages calculateYearPopulationAverages(int year)
	{
		YearPopulationAverages yearAvg=new YearPopulationAverages();
		yearAvg.setYear(year);

		int yearSampleTotal=0;
		int yearSampleCount=0;
		
		for (int month = GregorianCalendar.JANUARY; month <= GregorianCalendar.DECEMBER; month++)
		{
			GregorianCalendar startDate=new GregorianCalendar(year, month, 1);
			GregorianCalendar endDate=new GregorianCalendar(year, month, 1);
			endDate.add(GregorianCalendar.MONTH, 1);
			
			List<HomelessPopulationReport> monthResults=homelessPopulationReportDao.findByDateRange(startDate, endDate);
			int monthSampleTotal=0;
			int monthSampleCount=0;
			for (HomelessPopulationReport report : monthResults)
			{
				monthSampleCount++;
				monthSampleTotal=monthSampleTotal+report.getCurrentClientCount();
			}
			
			yearSampleTotal=yearSampleTotal+monthSampleTotal;
			yearSampleCount=yearSampleCount+monthSampleCount;
			
			yearAvg.getMonthAvg()[month]=(float)monthSampleTotal/monthSampleCount;
		}
		
		yearAvg.setYearAvg((float)yearSampleTotal/yearSampleCount);
		
		return(yearAvg);
	}
}
