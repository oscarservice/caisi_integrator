package org.oscarehr.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsent;
import org.oscarehr.caisi_integrator.dao.DemographicLinkDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.Referral;
import org.oscarehr.caisi_integrator.dao.ReferralDao;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class ReferralWs extends AbstractWs
{
	private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	private ReferralDao referralDao;

	@Autowired
	private DemographicLinkDao demographicLinkDao;

	/**
	 * Get all referrals to the callers facility for the given program.
	 */
	public List<Referral> getReferralsToProgram(@WebParam(name="destinationCaisiProgramId") Integer destinationCaisiProgramId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			List<Referral> results=referralDao.findByDestinationFacilityAndProgramId(loggedInFacility.getId(), destinationCaisiProgramId);
			
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
	 * Get referral by referral id.
	 */
	public Referral getReferral(@WebParam(name="referralId") Integer referralId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			Referral referral = referralDao.find(referralId);
			if (referral != null)
			{
				if (loggedInFacility.getId().equals(referral.getDestinationIntegratorFacilityId()))
				{
					eventLogDao.createDataReadEventEntry(referral);
					return(referral);
				}

				logger.error("Some one tried to get a referral from some one elses facility. loggedInFacilityId="+loggedInFacility.getId()+", referralDestinationIntegratorFacilityId="+referral.getDestinationIntegratorFacilityId());			
			}
		}
		catch (Exception e)
		{
			logger.error("An unexpected error occurred.", e);
		}

		return(null);
	}
	/**
	 * Get all referrals from the callers facility for the given demographic.
	 */
	public List<Referral> getLinkedReferrals(@WebParam(name="sourceCaisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, false);
			ArrayList<Referral> filteredResults = new ArrayList<Referral>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			for (FacilityIdIntegerCompositePk tempPk : linkedIds)
			{
				List<Referral> tempResults = referralDao.findBySourceFacilityAndDemographicId(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId());

				for (Referral referral : tempResults)
				{
					// as per request, everyone can see all referrals
					// if (!PhiFilter.isAllowed(tempPk.getIntegratorFacilityId(), null)) continue;

					if (isAllowedToRead(cachedDemographicConsent, tempPk.getIntegratorFacilityId()))
					{
						filteredResults.add(referral);						
					}
				}
			}

			eventLogDao.createDataReadEventEntries(filteredResults);
			return(filteredResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}		
	}

	/**
	 * The sourceFacilityId will be set automatically. The referralDate will be set automatically.
	 */
	public void makeReferral(Referral referral)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			referral.setSourceIntegratorFacilityId(loggedInFacility.getId());
			referral.setReferralDate(new Date());

			referralDao.persist(referral);
		}
		catch (Exception e)
		{
			logger.error("An unexpected error occurred.", e);
		}
	}

	public void removeReferral(@WebParam(name="referralId") Integer referralId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			Referral referral = referralDao.find(referralId);
			if (referral == null)
			{
				// it's only a warning because 2 people can remove a referral at the same time.
				logger.warn("Request made to remove referral that doesn't exist. referralId=" + referralId);
				return;
			}

			if (!loggedInFacility.getId().equals(referral.getDestinationIntegratorFacilityId()))
			{
				logger.error("Some one tried to remove a referral from some one elses facility. loggedInFacilityId="+loggedInFacility.getId()+", referralDestinationIntegratorFacilityId="+referral.getDestinationIntegratorFacilityId());
				return;
			}
			
			referralDao.remove(referralId);
		}
		catch (Exception e)
		{
			logger.error("An unexpected error occurred.", e);
		}
	}
}
