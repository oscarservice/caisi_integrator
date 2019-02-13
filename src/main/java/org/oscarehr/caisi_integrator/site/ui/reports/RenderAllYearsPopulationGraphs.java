package org.oscarehr.caisi_integrator.site.ui.reports;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.GregorianCalendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.oscarehr.caisi_integrator.site.ui.reports.AllYearsPopulationAverages.YearPopulationAverages;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;

@FacesComponent(value="renderAllYearsPopulationGraphs")
public class RenderAllYearsPopulationGraphs extends UIComponentBase
{
	private static final int MAX_GRAPH_BAR_HEIGHT=40;
	private static final int MAX_GRAPH_BAR_WIDTH=16;
	
	private AllYearsPopulationAverages allYearsPopulationAverages;
	private NumberFormat numberFormatter = NumberFormat.getInstance();

	public RenderAllYearsPopulationGraphs()
	{
		numberFormatter.setMinimumFractionDigits(1);
		numberFormatter.setMaximumFractionDigits(1);
	}
	
	@Override
	public String getFamily()
	{
		return null;
	}
	
	@SuppressWarnings("deprecation") // it's deprecated but it's replacement doesn't exist yet... 2010-01-15
	@Override
    public void encodeEnd(FacesContext context) throws IOException
	{
		ValueBinding v=getValueBinding("allYearsPopulationAverages");
		allYearsPopulationAverages=(AllYearsPopulationAverages)v.getValue(context);
		
		ResponseWriter writer=context.getResponseWriter();
		writer.write(getAveragesHtmlTable());
	}

	private String getAveragesHtmlTable()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append("<table class=\"borderedCollapsedTable\">");		
		
		sb.append(generateHeaderRow());
		
		for (YearPopulationAverages yearAvg : allYearsPopulationAverages.allYearsPopulationAverages)
		{
			sb.append(generateYearRow(yearAvg, allYearsPopulationAverages.maxMonthlyAvg));
		}
		
		sb.append("</table>");
		
		return(sb.toString());
	}
	
	private String generateYearRow(YearPopulationAverages yearAvg, float totalMaxMonthlyAvg)
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append("<tr>");
		String contextPath=JsfUtils.getContextPath();
		sb.append(generateTd("tableHeader", "text-align:right", generateBarForGraph(contextPath, 0, MAX_GRAPH_BAR_HEIGHT)+"<br />"+yearAvg.getYear()));
		
		for (int i = GregorianCalendar.JANUARY; i <= GregorianCalendar.DECEMBER; i++)
		{
			sb.append(generateAvgTd(yearAvg.getMonthAvg()[i], totalMaxMonthlyAvg, contextPath));
		}		
		
		sb.append(generateAvgTd(yearAvg.getYearAvg(), totalMaxMonthlyAvg, contextPath));

		sb.append("</tr>");

		return(sb.toString());
	}

	private static String generateHeaderRow()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append("<tr>");
		sb.append(generateTd(null, null, ""));
		
		for (int i = 1; i <= 12; i++)
		{
			String month;
			if (i<=9) month="0"+i;
			else month=String.valueOf(i);
			
			sb.append(generateTd("tableHeader", null, month));
		}		
		
		sb.append(generateTd("tableHeader", null, "Year Avg"));

		sb.append("</tr>");

		return(sb.toString());
	}
	
	private String generateAvgTd(float contents, float allYearsMaxMonth, String contextPath)
	{
		String formattedContents;
		
		if (Float.isNaN(contents)) formattedContents="-";
		else
		{
			int imageHeight=(int)(contents*MAX_GRAPH_BAR_HEIGHT/allYearsMaxMonth);
			formattedContents=generateBarForGraph(contextPath, MAX_GRAPH_BAR_WIDTH, imageHeight)+"<br />"+numberFormatter.format(contents);
		}
		
		return(generateTd("showBorder", "text-align:right;vertical-align:bottom", formattedContents));
	}
	
	private static String generateBarForGraph(String contextPath, int width, int height)
	{
		String image="<img src=\""+contextPath+"/resources/images/purple_1x1.png\" alt=\"\" style=\"width:"+width+"px;height:"+height+"px\" />";
		return(image);
	}
	
	private static String generateTd(String styleClass, String style, String contents)
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append("<td");
		if (styleClass!=null)
		{
			sb.append(" class=\"");
			sb.append(styleClass);
			sb.append('"');
		}
		if (style!=null)
		{
			sb.append(" style=\"");
			sb.append(style);
			sb.append('"');
		}
		sb.append('>');

		sb.append(contents);
		
		sb.append("</td>");

		return(sb.toString());
	}
}
