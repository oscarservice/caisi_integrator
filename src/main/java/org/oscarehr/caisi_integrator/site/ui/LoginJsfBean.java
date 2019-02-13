package org.oscarehr.caisi_integrator.site.ui;

import java.util.GregorianCalendar;

import javax.faces.bean.ManagedBean;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.dao.SiteUser;
import org.oscarehr.caisi_integrator.dao.SiteUserDao;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.site.utils.ReCaptchaUtils;
import org.oscarehr.caisi_integrator.site.utils.SiteSecurity;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebSite;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class LoginJsfBean
{
	private static final Logger logger=MiscUtils.getLogger();
	
	@Transient
	private SiteUserDao siteUserDao=(SiteUserDao)SpringUtils.getBean("siteUserDao");

	@Transient
	private EventLogDao eventLogDao=(EventLogDao)SpringUtils.getBean("eventLogDao");
	
	private String username;
	private String password;
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username.trim();
	}

	public String getPassword()
	{
		// no getting the password
		return("");
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isRecaptchaEnbled()
	{
		return(ReCaptchaUtils.enabled());
	}
	
	public String getRecaptchaPublicKey()
	{
		return(ReCaptchaUtils.PUBLIC_KEY);
	}
	
	public String login()
	{
		HttpServletRequest request=JsfUtils.getHttpServletRequest();
		HttpSession session=request.getSession();
		String requestingIp=request.getRemoteAddr();
		
		LoggedInInfoWebSite.setLoggedInInfoAtStartOfLoginAttempt();

		try
		{
			if (ReCaptchaUtils.enabled())
			{
				boolean validCaptcha=ReCaptchaUtils.validate(request);
				if (!validCaptcha)
				{
					JsfUtils.addErrorMessage("Invalid captcha");
					logger.warn("Invalid login attempt, bad captcha. ip="+requestingIp+", username="+username+", password="+password);
					
					eventLogDao.createLogicEventEntry("LOGIN"+MiscUtils.ID_SEPARATOR+"INVALID_CAPTCHA", "IP:"+requestingIp);
	
					return("login.jsf");
				}
			}
			
			SiteUser siteUser=siteUserDao.findByName(username);
			if (siteUser!=null)
			{
				if (siteUser.checkPassword(password) && !siteUser.isDisabled())
				{
					SiteSecurity.setLoggedInSiteUser(session, siteUser);
					LoggedInInfoWebSite.setAuthenticatedSiteUser(siteUser);
					
					eventLogDao.createLogicEventEntry("LOGIN"+MiscUtils.ID_SEPARATOR+"SUCCESS", "IP:"+requestingIp);

					siteUser.setLastLogin(new GregorianCalendar());
					siteUserDao.merge(siteUser);
					
					return("summary.jsf?"+JsfUtils.REDIRECT_QUERY_STRING);
				}
			}
		}
		catch (Exception e)
		{
			logger.warn("Unexpected error.", e);
		}
		
		JsfUtils.addErrorMessage("Invalid username or password.");
		logger.warn("Invalid login attempt, bad user/password. ip="+requestingIp+", username="+username+", password="+password);		
		eventLogDao.createLogicEventEntry("LOGIN"+MiscUtils.ID_SEPARATOR+"INVALID_USER_PASSWORD", "IP:"+requestingIp);

		SiteSecurity.removeLoggedInUser(session);

		return("login.jsf");
	}
}
