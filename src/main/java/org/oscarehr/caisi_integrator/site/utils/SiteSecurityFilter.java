package org.oscarehr.caisi_integrator.site.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebSite;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class SiteSecurityFilter implements javax.servlet.Filter
{
	private static final Logger logger=MiscUtils.getLogger();
	private EventLogDao eventLogDao=(EventLogDao)SpringUtils.getBean("eventLogDao");
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// can't think of anything to do right now.
	}

	@Override
	public void doFilter(ServletRequest tmpRequest, ServletResponse tmpResponse, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request=(HttpServletRequest)tmpRequest;
		HttpServletResponse response=(HttpServletResponse)tmpResponse;		
		HttpSession session=request.getSession();
		
		boolean isLoggedIn=SiteSecurity.isLoggedIn(session);
		if (isLoggedIn || isAllowed(request.getServletPath()))
		{
			if (isLoggedIn)
			{
				LoggedInInfoWebSite.setLoggedInInfo(SiteSecurity.getLoggedInSiteUser(session));
				eventLogDao.createLogicEventEntry(request.getServletPath(), null);
			}
			
			try
			{
				chain.doFilter(tmpRequest, tmpResponse);
			}
			finally
			{				
				LoggedInInfo.loggedInInfo.remove();
			}
		}
		else
		{
			logger.warn("Request made for secure item while not logged in. "+request.getServletPath());
			response.sendRedirect(request.getContextPath()+"/site");
		}
	}
	
	@Override
	public void destroy()
	{
		// can't think of anything to do right now.
	}
		
	private static boolean isAllowed(String path)
	{
		// path = /site/login.jsf
		
		if (path.equals("/site/index.jsp")) return(true);
		else if (path.equals("/site/login.jsf")) return(true);
		else if (path.equals("/site/logout.jsp")) return(true);
		else if (path.startsWith("/site/templates/")) return(true);
		else if (path.equals("/site/test.jsf")) return(true);
		else if (path.startsWith("/site/public/")) return(true);
		else if (path.equals("/site/upgrade.jsf")) return(true);
		else return(false);
	}
}
