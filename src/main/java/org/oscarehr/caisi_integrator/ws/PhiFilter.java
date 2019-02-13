package org.oscarehr.caisi_integrator.ws;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.CachedProviderDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.util.ConfigXmlUtils;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebService;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.Role;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class PhiFilter
{
	private static final Logger logger = MiscUtils.getLogger();
	private static CachedFacilityDao cachedFacilityDao = (CachedFacilityDao) SpringUtils.getBean("cachedFacilityDao");
	private static CachedProviderDao cachedProviderDao = (CachedProviderDao) SpringUtils.getBean("cachedProviderDao");

	private static boolean requirePhiFilter = Boolean.parseBoolean(ConfigXmlUtils.getPropertyString("integrator_server", "require_phi_filter_to_read_data"));

	/**
	 * This method will determine if the currently logged in facility/provider
	 * is allowed to access data from the passed in (parameter) facility / provider.
	 */
	public static boolean isAllowedFromProvider(Integer dataCachedFacilityId, String dataProviderNo)
	{
		if (!requirePhiFilter) return(true);

		HashSet<Role> dataRoles = null;
		logger.debug("dataProviderNo : " + dataProviderNo);
		if (dataProviderNo != null)
		{
			FacilityIdStringCompositePk dataProviderPk = new FacilityIdStringCompositePk(dataCachedFacilityId, dataProviderNo);
			dataRoles = cachedProviderDao.getProviderRoles(dataProviderPk);
			logger.debug("data providerPk=" + dataProviderPk + ", Resulting roles=" + dataRoles);
		}

		return(isAllowed(dataCachedFacilityId, dataRoles));
	}

	/**
	 * This method will determine if the currently logged in facility/provider
	 * is allowed to access data from the passed in (parameter) facility, for the given role (ignoring the data writers role).
	 */
	public static boolean isAllowedForRole(Integer dataCachedFacilityId, Role role)
	{
		if (!requirePhiFilter) return(true);

		HashSet<Role> dataRoles = null;
		if (role != null)
		{
			dataRoles = new HashSet<Role>();
			dataRoles.add(role);
		}

		return(isAllowed(dataCachedFacilityId, dataRoles));
	}

	public static boolean isAllowed(Integer dataCachedFacilityId, Set<Role> dataRoles)
	{
		if (!requirePhiFilter) return(true);

		LoggedInInfoWebService loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
		Facility facility = loggedInInfo.getFacility();
		CachedFacility loggedInCachedFacility = cachedFacilityDao.find(facility.getId());

		HashSet<Role> requestingRoles = cachedProviderDao.getProviderRoles(loggedInInfo.getCachedProvider().getFacilityIdStringCompositePk());
		CachedFacility dataCachedFacility = cachedFacilityDao.find(dataCachedFacilityId);

		return(isAllowed(loggedInCachedFacility, dataCachedFacility, requestingRoles, dataRoles));
	}

	private static boolean isAllowed(CachedFacility loggedInCachedFacility, CachedFacility dataCachedFacility, Set<Role> requesterRoles, Set<Role> dataRoles)
	{
		if (!requirePhiFilter) return(true);

		// 4 main permutations
		// HIC reading HIC data
		// HIC reading non-HIC data
		// non-HIC reading HIC data 
		// non-HIC reading non-HIC data

		logger.debug("RequesterRoles:" + requesterRoles);
		logger.debug("DataRoles:" + dataRoles);

		//Support Staff
		if (requesterRoles.contains(Role.SECRETARY) || requesterRoles.contains(Role.RECEPTIONIST) || requesterRoles.contains(Role.SUPPORT_WORKER) || requesterRoles.contains(Role.CLIENT_SERVICE_WORKER) || requesterRoles.contains(Role.PROPERTY_STAFF) || requesterRoles.contains(Role.CSW) || requesterRoles.contains(Role.SUPPORT_COUNSELLOR))
		{
			if (dataRoles == null)
			{
				logger.debug("phi rejected condition 01");
				return(false);
			}
			//Support Staff can only see Support Staff's remote notes.
			else if (dataRoles.contains(Role.SECRETARY) || dataRoles.contains(Role.RECEPTIONIST) || dataRoles.contains(Role.SUPPORT_WORKER) || dataRoles.contains(Role.CLIENT_SERVICE_WORKER) || dataRoles.contains(Role.PROPERTY_STAFF) || dataRoles.contains(Role.CSW) || dataRoles.contains(Role.SUPPORT_COUNSELLOR))
			{
				logger.debug("phi accepted condition 02 " + dataRoles);
				return(true);
			}
			else
			{
				logger.debug("phi rejected condition 03");
				return(false);
			}
		}

		if (loggedInCachedFacility.isHic())
		{
			//HIC Health Care Team
			if (requesterRoles.contains(Role.DOCTOR) || requesterRoles.contains(Role.RN) || requesterRoles.contains(Role.PSYCHIATRIST) || requesterRoles.contains(Role.CLINICAL_SOCIAL_WORKER) || requesterRoles.contains(Role.RECREATION_THERAPIST) || requesterRoles.contains(Role.CLINICAL_CASE_MANAGER) || requesterRoles.contains(Role.NURSE_MANAGER) || requesterRoles.contains(Role.RPN))
			{
				logger.debug("phi accepted condition 1");
				return(true);
			}

			//HIC Clinical Support Staff and Social Services Staff
			if (requesterRoles.contains(Role.CLINICAL_ASSISTANT) || requesterRoles.contains(Role.HOUSING_WORKER) || requesterRoles.contains(Role.COUNSELLOR) || requesterRoles.contains(Role.CASE_MANAGER) || requesterRoles.contains(Role.MEDICAL_SECRETARY))
			{
				if (dataRoles == null)
				{
					logger.debug("phi rejected condition 3");
					return(false);
				}
				else if (dataRoles.contains(Role.DOCTOR) || dataRoles.contains(Role.RN) || requesterRoles.contains(Role.PSYCHIATRIST) || requesterRoles.contains(Role.CLINICAL_SOCIAL_WORKER) || requesterRoles.contains(Role.RECREATION_THERAPIST) || requesterRoles.contains(Role.CLINICAL_CASE_MANAGER) || requesterRoles.contains(Role.NURSE_MANAGER) || requesterRoles.contains(Role.RPN))
				{
					logger.debug("phi rejected condition 3a " + dataRoles);
					return(false);
				}
				else
				{
					logger.debug("phi accepted condition 4");
					return(true);
				}
			}
		}
		else
		// non-hic reading
		{
			if (dataCachedFacility.isHic()) // non-hic reading from hic
			{
				//Social Services Team
				if (requesterRoles.contains(Role.HOUSING_WORKER) || requesterRoles.contains(Role.COUNSELLOR) || requesterRoles.contains(Role.CASE_MANAGER))
				{
					if (dataRoles == null || dataRoles.contains(Role.DOCTOR) || dataRoles.contains(Role.RN) || dataRoles.contains(Role.PSYCHIATRIST) || dataRoles.contains(Role.CLINICAL_SOCIAL_WORKER) || dataRoles.contains(Role.RECREATION_THERAPIST) || dataRoles.contains(Role.CLINICAL_CASE_MANAGER) || dataRoles.contains(Role.NURSE_MANAGER) || dataRoles.contains(Role.RPN) || dataRoles.contains(Role.CLINICAL_ASSISTANT) || dataRoles.contains(Role.MEDICAL_SECRETARY))
					{
						logger.debug("phi rejected condition 5");
						return(false);
					}
					else
					{
						logger.debug("phi accepted condition 6");
						return(true);
					}
				}
			}
			else
			// non-hic reading from non-hic
			{
				logger.debug("phi accepted condition 7");
				return(true);
			}
		}

		logger.debug("phi rejected condition 8");
		return(false);
	}
}
