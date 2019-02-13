package org.oscarehr.caisi_integrator.site.utils;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.SiteUser;
import org.oscarehr.caisi_integrator.util.MiscUtils;

public final class SiteSecurity
{
	private static final String LOGGED_IN_SITE_USER_SESSION_KEY = "LOGGED_IN_SITE_USER";
	private static final Logger logger = MiscUtils.getLogger();

	private SiteSecurity()
	{
		// no instances, utility class
	}

	public static void setLoggedInSiteUser(HttpSession session, SiteUser siteUser)
	{
		SiteUser existingSiteUser=(SiteUser)session.getAttribute(LOGGED_IN_SITE_USER_SESSION_KEY);
		if (existingSiteUser != null && existingSiteUser.getId().intValue()!=siteUser.getId().intValue())
		{
			logger.warn("Setting logged in user when already exists. previous user=" + session.getAttribute(LOGGED_IN_SITE_USER_SESSION_KEY), new Exception());
		}

		session.setAttribute(LOGGED_IN_SITE_USER_SESSION_KEY, siteUser);		
	}
		
	public static SiteUser getLoggedInSiteUser(HttpSession session)
	{
		SiteUser siteUser = (SiteUser)session.getAttribute(LOGGED_IN_SITE_USER_SESSION_KEY);
		return(siteUser);
	}

	public static boolean isLoggedIn(HttpSession session)
	{
		return(getLoggedInSiteUser(session) != null);
	}

	public static void removeLoggedInUser(HttpSession session)
	{
		session.removeAttribute(LOGGED_IN_SITE_USER_SESSION_KEY);
	}
}
