package org.oscarehr.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.ImportLog;
import org.oscarehr.caisi_integrator.dao.ImportLogDao;
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

	@Autowired
	private ImportLogDao importLogDao;

	public List<CachedFacility> getAllFacility()
	{
		try
		{
			List<CachedFacility> results = cachedFacilityDao.findAll();

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
			CachedFacility cachedFacility = cachedFacilityDao.find(loggedInFacility.getId());
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
		CachedFacility cachedFacility = cachedFacilityDao.findByFacilityId(loggedInFacility.getId());

		eventLogDao.createDataReadEventEntry(cachedFacility);
		return(cachedFacility);
	}

	public List<ImportLog> getImportLogByFilenameAndChecksum(String filename, String checksum)
	{
		Facility loggedInFacility = getLoggedInFacility();
		List<ImportLog> logs = importLogDao.findByFilenameAndChecksum(loggedInFacility.getId(), filename, checksum);
		return(logs);
	}

	public List<ImportLog> getImportLogsSince(Date startDate)
	{
		Facility loggedInFacility = getLoggedInFacility();
		List<ImportLog> logs = new ArrayList<ImportLog>();
		if (startDate == null)
		{
			logs = importLogDao.findByFacility(loggedInFacility.getId());
		}
		else
		{
			logs = importLogDao.findByFacilitySince(loggedInFacility.getId(), startDate);
		}

		//eventLogDao.createDataReadEventEntry(logs);
		return(logs);
	}

	public String canProcessFile(String filename, String checksum, Date startDate, Date endDate, String dependsOn)
	{
		ImportLog lastLog = importLogDao.findLastCompletedByFacility(getLoggedInFacility().getId());

		if (lastLog == null && dependsOn == null)
		{
			return "OK";
		}
		else if (lastLog == null && dependsOn != null)
		{
			return "DEPENDENCY_NOT_MET";
		}

		if (lastLog != null && lastLog.getChecksum().equals(checksum))
		{
			return "ALREADY_IMPORTED";
		}

		if (lastLog != null && lastLog.getDateIntervalEnd().getTime() > endDate.getTime())
		{
			return "INTEGRATOR_HAS_NEWER_DATA";
		}

		if (lastLog != null && dependsOn != null && !lastLog.getChecksum().equals(dependsOn))
		{
			return "DEPENDENCY_NOT_MET";
		}

		return "OK";
	}

	public ImportLog createImportLog(String filename, String checksum, Date startDate, Date endDate, String dependsOn)
	{
		return createImportLogWithStatus(filename, checksum, startDate, endDate, dependsOn, "PROCESSING");
	}

	//should be an enum for status
	public ImportLog createImportLogWithStatus(String filename, String checksum, Date startDate, Date endDate, String dependsOn, String status)
	{
		Facility loggedInFacility = getLoggedInFacility();

		ImportLog importLog = new ImportLog();
		importLog.setFacilityId(loggedInFacility.getId());
		importLog.setDateCreated(new Date());
		importLog.setFilename(filename);
		importLog.setChecksum(checksum);
		importLog.setDateIntervalStart(startDate);
		importLog.setDateIntervalEnd(endDate);
		importLog.setLastUpdatedDate(importLog.getDateCreated());
		importLog.setDependsOn(dependsOn);
		importLog.setStatus(status);
		importLogDao.persist(importLog);

		return importLog;
	}

	public ImportLog completeImportLog(Integer importLogId)
	{

		Facility loggedInFacility = getLoggedInFacility();

		ImportLog importLog = importLogDao.find(importLogId);

		if (importLog != null && loggedInFacility.getId().intValue() == importLog.getFacilityId().intValue())
		{
			importLog.setStatus("COMPLETED");
			importLog.setLastUpdatedDate(new Date());
			importLogDao.merge(importLog);
		}
		return importLog;
	}

	public ImportLog errorImportLog(Integer importLogId)
	{
		Facility loggedInFacility = getLoggedInFacility();

		ImportLog importLog = importLogDao.find(importLogId);

		if (importLog != null && loggedInFacility.getId().intValue() == importLog.getFacilityId().intValue())
		{
			importLog.setStatus("ERROR");
			importLog.setLastUpdatedDate(new Date());
			importLogDao.merge(importLog);
		}
		return importLog;
	}

}
