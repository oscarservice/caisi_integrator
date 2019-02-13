package org.oscarehr.caisi_integrator.ws;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class FacilityWs extends AbstractWs
{
	private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	private CachedFacilityDao cachedFacilityDao;

	public List<CachedFacility> getAllFacility()
	{
		try
		{
			List<CachedFacility> results=cachedFacilityDao.findAll();
			
			eventLogDao.createDataReadEventEntries(results);
			return(results);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public void setMyFacility(CachedFacility cachedFacility)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			cachedFacility.setIntegratorFacilityId(loggedInFacility.getId());
			cachedFacilityDao.replace(cachedFacility);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}
	
	public void updateMyFacilityLastUpdateDate(Date currentUpdateDate)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			CachedFacility cachedFacility=cachedFacilityDao.find(loggedInFacility.getId());
			cachedFacility.setLastDataUpdate(currentUpdateDate);
			cachedFacilityDao.merge(cachedFacility);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}
	
	public CachedFacility getMyFacility()
	{
		Facility loggedInFacility = getLoggedInFacility();
		CachedFacility cachedFacility=cachedFacilityDao.findByFacilityId(loggedInFacility.getId());
		
		eventLogDao.createDataReadEventEntry(cachedFacility);
		return(cachedFacility);
	}
}
