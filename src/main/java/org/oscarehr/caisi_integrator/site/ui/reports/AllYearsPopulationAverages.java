/**
 * 
 */
package org.oscarehr.caisi_integrator.site.ui.reports;

import java.util.ArrayList;

public class AllYearsPopulationAverages
{
	public static class YearPopulationAverages
	{
		private int year;
		private float yearAvg;
		private float[] monthAvg = new float[12]; // 0-11 represents each month

		public int getYear()
		{
			return year;
		}

		public void setYear(int year)
		{
			this.year = year;
		}

		public float getYearAvg()
		{
			return yearAvg;
		}

		public void setYearAvg(float yearAvg)
		{
			this.yearAvg = yearAvg;
		}

		public float[] getMonthAvg()
		{
			return monthAvg;
		}

		public void setMonthAvg(float[] monthAvg)
		{
			this.monthAvg = monthAvg;
		}

		public float getMaxMonthAvg()
		{
			float max=0;
			
			for (float avg : monthAvg)
			{
				if (!Float.isNaN(avg)) max=Math.max(max, avg);
			}

			return(max);
		}
	}

	float maxMonthlyAvg=0;
	ArrayList<YearPopulationAverages> allYearsPopulationAverages = new ArrayList<YearPopulationAverages>();

	public float getMaxMonthlyAvg()
	{
		return maxMonthlyAvg;
	}

	public void setMaxMonthlyAvg(float maxMonthlyAvg)
	{
		this.maxMonthlyAvg = maxMonthlyAvg;
	}

	public ArrayList<YearPopulationAverages> getAllYearsPopulationAverages()
	{
		return allYearsPopulationAverages;
	}

	public void setAllYearsPopulationAverages(ArrayList<YearPopulationAverages> allYearsPopulationAverages)
	{
		this.allYearsPopulationAverages = allYearsPopulationAverages;
	}
}