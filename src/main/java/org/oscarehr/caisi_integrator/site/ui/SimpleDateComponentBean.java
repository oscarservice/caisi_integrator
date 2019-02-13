package org.oscarehr.caisi_integrator.site.ui;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean
public class SimpleDateComponentBean
{
	public ArrayList<SelectItem> getListOfYears()
	{
		ArrayList<SelectItem> selectItems = new ArrayList<SelectItem>();

		GregorianCalendar cal = new GregorianCalendar();

		for (int i = cal.get(GregorianCalendar.YEAR); i >= 2008; i--)
		{
			String value = String.valueOf(i);
			selectItems.add(new SelectItem(value, value));
		}

		return(selectItems);
	}

	public ArrayList<SelectItem> getListOfMonths()
	{
		ArrayList<SelectItem> selectItems = new ArrayList<SelectItem>();

		DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance();
		String[] months = dateFormatSymbols.getShortMonths();

		for (int i = 1; i <= 12; i++)
		{
			String id = String.valueOf(i);
			String value = id + " (" + months[i - 1] + ')';
			selectItems.add(new SelectItem(id, value));
		}

		return(selectItems);
	}

	public ArrayList<SelectItem> getListOfDays()
	{
		ArrayList<SelectItem> selectItems = new ArrayList<SelectItem>();

		for (int i = 1; i <= 31; i++)
		{
			String value = String.valueOf(i);
			selectItems.add(new SelectItem(value, value));
		}

		return(selectItems);
	}
}
