package org.oscarehr.caisi_integrator.site.ui.admin;

import javax.faces.bean.ManagedBean;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class CreateUpdateFacilityJsfBean
{
	private static final Logger logger=MiscUtils.getLogger();

	@Transient
	private FacilityDao facilityDao = (FacilityDao)SpringUtils.getBean("facilityDao");

	private Facility facility = new Facility(); 
	private String password = null;

	public void setFacilityId(Integer facilityId)
	{
		if (facilityId==null) return;
		
		facility = facilityDao.find(facilityId);
	}

	public Integer getFacilityId()
	{
		return(facility.getId());
	}

	public Facility getFacility()
	{
		return facility;
	}

	public void setFacility(Facility facility)
	{
		this.facility = facility;
	}

	public boolean getIsNew()
	{
		return(facility.getId()==null);
	}
	
	public String getCreateUpdateTitle()
	{
		if (getIsNew()) return("Add new facility");
		else return("Update facility");
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
				facility.setPassword(password);
			}
			
			// save the changes
			if (facility.getId()==null) facilityDao.persist(facility);
			else facilityDao.merge(facility);
		
			return("view_facilities.jsf?"+JsfUtils.REDIRECT_QUERY_STRING);
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
			JsfUtils.addErrorMessage("Error creating Facility : "+e.getMessage());
			return(null);
		}
	}
}
