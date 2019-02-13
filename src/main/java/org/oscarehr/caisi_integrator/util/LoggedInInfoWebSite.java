package org.oscarehr.caisi_integrator.util;

import org.oscarehr.caisi_integrator.dao.SiteUser;

public class LoggedInInfoWebSite extends LoggedInInfo
{
	private SiteUser siteUser = null;
	private boolean duringLogin = false;
	
	public SiteUser getSiteUser()
	{
		return siteUser;
	}

	/**
	 * This method is intended to be used by the loginJsfBean class before a valid login exists, i.e. during the login attempt period.
	 */
	public static void setLoggedInInfoAtStartOfLoginAttempt()
	{
		checkForLingeringData();

		// create and set new thread local
		LoggedInInfoWebSite x = new LoggedInInfoWebSite();
		x.duringLogin = true;
		loggedInInfo.set(x);
	}

	/**
	 * This method is intended to be used by the loginJsfBean class after it has authenticated the user, it needs to set it.
	 */
	public static void setAuthenticatedSiteUser(SiteUser siteUser)
	{
		LoggedInInfoWebSite x = new LoggedInInfoWebSite();
		x.siteUser = siteUser;
		x.duringLogin=false;
	}
	
	/**
	 * This method is intended to be used by the security filter on subsequent http requests only
	 */
	public static void setLoggedInInfo(SiteUser siteUser)
	{
		checkForLingeringData();

		// create and set new thread local
		LoggedInInfoWebSite x = new LoggedInInfoWebSite();
		x.siteUser = siteUser;
		loggedInInfo.set(x);
	}

	
	@Override
	public String getSourceString()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append(getSourcePrefix());
		sb.append(MiscUtils.ID_SEPARATOR);
		
		if (duringLogin) sb.append("DURING_LOGIN");
		else sb.append(siteUser.getId());
		
		return(sb.toString());
	}

	@Override
	public String getSourcePrefix()
	{
		return("SITE");
	}
}
