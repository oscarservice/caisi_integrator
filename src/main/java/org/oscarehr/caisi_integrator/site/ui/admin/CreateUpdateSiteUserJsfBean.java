package org.oscarehr.caisi_integrator.site.ui.admin;

import javax.faces.bean.ManagedBean;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.SiteUser;
import org.oscarehr.caisi_integrator.dao.SiteUserDao;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class CreateUpdateSiteUserJsfBean
{
	private static final Logger logger=MiscUtils.getLogger();

	@Transient
	private SiteUserDao siteUserDao = (SiteUserDao)SpringUtils.getBean("siteUserDao");

	private SiteUser siteUser = new SiteUser(); 
	private String password = null;

	public void setSiteUserId(Integer siteUserId)
	{
		if (siteUserId==null) return;
		
		siteUser = siteUserDao.find(siteUserId);
	}

	public Integer getSiteUserId()
	{
		return(siteUser.getId());
	}

	public SiteUser getSiteUser()
	{
		return siteUser;
	}

	public void setSiteUser(SiteUser siteUser)
	{
		this.siteUser = siteUser;
	}

	public boolean getIsNew()
	{
		return(siteUser.getId()==null);
	}
	
	public String getCreateUpdateTitle()
	{
		if (getIsNew()) return("Create new site user");
		else return("Update site user");
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String save()
	{
		try
		{
			// save password
			if (password!=null && password.length()>0)
			{
				siteUser.setPassword(password);
			}
			
			// save the changes
			if (siteUser.getId()==null) siteUserDao.persist(siteUser);
			else siteUserDao.merge(siteUser);
		
			return("view_site_users.jsf?"+JsfUtils.REDIRECT_QUERY_STRING);
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
			JsfUtils.addErrorMessage("Error creating SiteUser : "+e.getMessage());
			return(null);
		}
	}
}
