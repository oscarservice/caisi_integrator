package org.oscarehr.caisi_integrator.site.ui.reports;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;
import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.EventLog;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class ViewAllLogsJsfBean
{
	private static final int MAX_RESULTS = 100;
	
	@Transient
	private static EventLogDao eventLogDao = (EventLogDao)SpringUtils.getBean("eventLogDao");
	
	private int pageNum = 0;
	private int allLogsCount=-1;

	public void setPageNum(Integer pageNum)
	{
		if (pageNum!=null)
		{
			if (pageNum<0) this.pageNum=0;
			else if (isPageNumTooBig(getLogCount())) this.pageNum = getMaxPages(getLogCount());
			else this.pageNum = pageNum;
		}
	}

	public int getPageNum()
	{
		return pageNum;
	}

	private boolean isPageNumTooBig(int totalLogs)
	{
		return(getPageNum() > getMaxPages(totalLogs));
	}

	private int getMaxPages(int totalLogs)
	{
		if (totalLogs == 0) return 0;
		else return (totalLogs - 1) / MAX_RESULTS;
	}

	public ListDataModel<EventLog> getEventLogs()
	{
		List<EventLog> list = eventLogDao.findAll(getPageNum() * MAX_RESULTS, MAX_RESULTS);
		ListDataModel<EventLog> results = new ListDataModel<EventLog>(list);
		return results;
	}

	public int getLogCount()
	{
		if (allLogsCount==-1)
		{
			allLogsCount=eventLogDao.getCountAll();
		}
		
		return(allLogsCount); 
	}
}
