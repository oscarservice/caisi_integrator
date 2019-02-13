package org.oscarehr.caisi_integrator.site.ui;

import javax.faces.bean.ManagedBean;
import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.CachedDemographicDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDrugDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssueDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNoteDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicPreventionDao;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.CachedProgramDao;
import org.oscarehr.caisi_integrator.dao.CachedProviderDao;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class SummaryJsfBean
{
	@Transient
	private CachedFacilityDao cachedFacilityDao=(CachedFacilityDao)SpringUtils.getBean("cachedFacilityDao");

	@Transient
	private CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");
	
	@Transient
	private CachedDemographicDao cachedDemographicDao=(CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");
	
	@Transient
	private CachedProgramDao cachedProgramDao=(CachedProgramDao)SpringUtils.getBean("cachedProgramDao");
	
	@Transient
	private CachedDemographicNoteDao cachedDemographicNoteDao=(CachedDemographicNoteDao)SpringUtils.getBean("cachedDemographicNoteDao");
	
	@Transient
	private CachedDemographicIssueDao cachedDemographicIssueDao=(CachedDemographicIssueDao)SpringUtils.getBean("cachedDemographicIssueDao");
	
	@Transient
	private CachedDemographicPreventionDao cachedDemographicPreventionDao=(CachedDemographicPreventionDao)SpringUtils.getBean("cachedDemographicPreventionDao");

	@Transient
	private CachedDemographicDrugDao cachedDemographicDrugDao=(CachedDemographicDrugDao)SpringUtils.getBean("cachedDemographicDrugDao");
	
	public int getFacilityCount()
	{
		return(cachedFacilityDao.getCountAll());
	}

	public int getProviderCount()
	{
		return(cachedProviderDao.getCountAll());
	}

	public int getProgramCount()
	{
		return(cachedProgramDao.getCountAll());
	}

	public int getClientCount()
	{
		return(cachedDemographicDao.getCountAll());
	}

	public int getNoteCount()
	{
		return(cachedDemographicNoteDao.getCountAll());
	}

	public int getIssueCount()
	{
		return(cachedDemographicIssueDao.getCountAll());
	}

	public int getPreventionCount()
	{
		return(cachedDemographicPreventionDao.getCountAll());
	}

	public int getDrugCount()
	{
		return(cachedDemographicDrugDao.getCountAll());
	}
}
