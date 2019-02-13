<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.caisi_integrator.site.ui.reports.IssueReportJsfBean"%>

<%@page contentType="text/csv"%>
<%
	IssueReportJsfBean issueReportJsfBean=(IssueReportJsfBean)session.getAttribute("issueReportJsfBean");
	String filename=issueReportJsfBean.getReportDownloadFilename();
	response.setHeader("Content-Disposition", "attachment; filename="+filename);

	ArrayList<IssueReportJsfBean.IssueDisplayRow> rows=issueReportJsfBean.getReportResultsForDownload();
	
	PrintWriter writer=response.getWriter();

	for (IssueReportJsfBean.IssueDisplayRow row : rows)
	{
		String line=row.getOccuranceDate()+','+StringEscapeUtils.escapeCsv(row.getFacility())+','+StringEscapeUtils.escapeCsv(row.getIssueTypeCode())+','+StringEscapeUtils.escapeCsv(row.getIssueDescription());
		writer.println(line);
	}
	
	writer.flush();
%>
