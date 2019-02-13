package org.oscarehr.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.CachedProgram;
import org.oscarehr.caisi_integrator.dao.CachedProgramDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class ProgramWs extends AbstractWs
{
	private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	private CachedFacilityDao cachedFacilityDao;

	@Autowired
	private CachedProgramDao cachedProgramDao;

	/**
	 * Get all programs including ones from the callers own system.
	 */
	public List<CachedProgram> getAllPrograms()
	{
		try
		{
			List<CachedProgram> results = cachedProgramDao.findAll();

			eventLogDao.createDataReadEventEntries(results);
			return(results);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	/**
	 * Get all programs from facilities accepting referrals except ones from the callers own system.
	 */
	public List<CachedProgram> getAllProgramsAllowingIntegratedReferrals()
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<CachedProgram> results = new ArrayList<CachedProgram>();
			for (CachedProgram cachedProgram : cachedProgramDao.findAll())
			{
				if (!"active".equals(cachedProgram.getStatus())) continue;

				FacilityIdIntegerCompositePk pk = cachedProgram.getFacilityIdIntegerCompositePk();
				if (!pk.getIntegratorFacilityId().equals(loggedInFacility.getId()))
				{
					// slightly inefficient to keep getting the facility but it's okay for now
					CachedFacility tempFacility = cachedFacilityDao.find(pk.getIntegratorFacilityId());
					if (tempFacility.isEnableIntegratedReferrals()) results.add(cachedProgram);
				}
			}

			eventLogDao.createDataReadEventEntries(results);
			return(results);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	/**
	 * When calling this method, the client doesn't need to fill in the facility ID, it will be sorted out by the server.
	 */
	public void setCachedPrograms(List<CachedProgram> cachedPrograms)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			// null should be treated as a blank list, i.e. no one available.
			if (cachedPrograms == null) cachedPrograms = new ArrayList<CachedProgram>();

			// add and update items
			for (CachedProgram cachedProgram : cachedPrograms)
			{
				cachedProgram.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedProgramDao.replace(cachedProgram);
				logger.debug("Added/Replaced Program " + cachedProgram.getFacilityIdIntegerCompositePk().getCaisiItemId() + ", " + cachedProgram.getName() + " to Facility " + cachedProgram.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
			}

		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public void deleteCachedProgramsMissingFromList(Integer[] cachedProgramIds)
	{
		//vals contains the list of ids from the server that exist, integrator may
		//have ones that are now deleted

		List<Integer> vals = Arrays.asList(cachedProgramIds);

		//previousPrograms is all programs currently in the integrator
		Facility loggedInFacility = getLoggedInFacility();
		List<CachedProgram> previousPrograms = cachedProgramDao.findByFacilityId(loggedInFacility.getId());

		for (CachedProgram x : previousPrograms)
		{
			if (!vals.contains(x.getFacilityIdIntegerCompositePk().getCaisiItemId()))
			{
				cachedProgramDao.remove(x.getFacilityIdIntegerCompositePk());
				logger.debug("Removed Program " + x.getFacilityIdIntegerCompositePk().getCaisiItemId() + ", " + x.getName() + " from Facility " + x.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
			}
		}
	}
}
