package org.oscarehr.caisi_integrator.site.ui.admin;

import javax.faces.bean.ManagedBean;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.SystemProperties;
import org.oscarehr.caisi_integrator.dao.SystemPropertiesDao;
import org.oscarehr.caisi_integrator.dao.schema.SchemaUpgradeManager;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class DatabaseUpgradeJsfBean
{
	private static final Logger logger=MiscUtils.getLogger();

	@Transient
	private SystemPropertiesDao systemPropertiesDao = (SystemPropertiesDao)SpringUtils.getBean("systemPropertiesDao");

	public int getCodeDataVersion()
	{
		return(SystemProperties.CODE_SCHEMA_VERSION);
	}

	public int getDatabaseDataVersion()
	{
		SystemProperties systemProperties=null;
		
		try
		{
			systemProperties=systemPropertiesDao.find();
			return(systemProperties.getSchemaVersion());
		}
		catch (Exception e)
		{
			// no row, i.e. version 0
			return(0);
		}		
	}
	
	public boolean isDisableUpgradeButton()
	{
		return(getCodeDataVersion()==getDatabaseDataVersion());
	}
	
	public String upgrade()
	{
		if (getCodeDataVersion()==getDatabaseDataVersion()) return(null);
		
		try
		{
			SchemaUpgradeManager.upgradeDatabase();
			
			JsfUtils.addInfoMessage("Upgrade successful");
			JsfUtils.addErrorMessage("*** YOU MUST RESTART THE WEBAPP NOW ***");

			return(null);
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
			JsfUtils.addErrorMessage("Error upgrading database : "+e.getMessage());
			return(null);
		}
	}
}
