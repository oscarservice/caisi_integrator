package org.oscarehr.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedProvider;
import org.oscarehr.caisi_integrator.dao.CachedProviderDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.dao.ProviderCommunication;
import org.oscarehr.caisi_integrator.dao.ProviderCommunicationDao;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class ProviderWs extends AbstractWs
{
	private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	private CachedProviderDao cachedProviderDao;

	@Autowired
	private ProviderCommunicationDao providerCommunicationDao;

	/**
	 */
	public List<CachedProvider> getAllProviders()
	{
		try
		{
			List<CachedProvider> results=cachedProviderDao.findAll();
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
	public void setCachedProviders(List<ProviderTransfer> providerTransfers)
	{
		try
		{
			if (providerTransfers == null)
				return;

			Facility loggedInFacility = getLoggedInFacility();

			// add and update items
			for (ProviderTransfer providerTransfer : providerTransfers)
			{
				// store the provider
				CachedProvider cachedProvider = providerTransfer.getCachedProvider();
				cachedProvider.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedProviderDao.replace(cachedProvider);

				// store the providers roles
				cachedProviderDao.setProviderRoles(cachedProvider.getId(), providerTransfer.getRoles());
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}
	
	public Role[] getProviderRoles(FacilityIdStringCompositePk providerFacilityIdStringCompositePk)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			
			logger.debug("request for getProviderRoles : facilityId="+loggedInFacility.getId()+", providerId="+providerFacilityIdStringCompositePk.getCaisiItemId());
			HashSet<Role> tempResults=cachedProviderDao.getProviderRoles(providerFacilityIdStringCompositePk);
			logger.debug("retrieved provider roles="+tempResults);
			
			return(tempResults.toArray(new Role[0]));
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}
	
	public void addProviderComunication(ProviderCommunicationTransfer providerCommunicationTransfer)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			providerCommunicationTransfer.setSourceIntegratorFacilityId(loggedInFacility.getId());
			providerCommunicationTransfer.setId(null);
			
			ProviderCommunication providerCommunication=new ProviderCommunication();
			BeanUtils.copyProperties(providerCommunicationTransfer, providerCommunication);
			providerCommunication.setActive(true);
			providerCommunication.setSentDate(new Date());
			
			providerCommunicationDao.persist(providerCommunication);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}
	
	/**
	 * @param active can be null for both active and inactive
	 * @return
	 */
	public List<ProviderCommunicationTransfer> getProviderCommunications(String providerId, String type, Boolean active)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			
			logger.debug("request for provider communications : facilityId="+loggedInFacility.getId()+", providerId="+providerId+", type="+type+", active="+active);
			List<ProviderCommunication> tempResults=providerCommunicationDao.findByDestinationFacilityAndProviderIdAndType(loggedInFacility.getId(), providerId, type, active);
			logger.debug("retrieved provider communications, size="+tempResults.size());
			
			ArrayList<ProviderCommunicationTransfer> results=new ArrayList<ProviderCommunicationTransfer>();
			for (ProviderCommunication providerCommunication : tempResults)
			{
				ProviderCommunicationTransfer transfer=new ProviderCommunicationTransfer();
				BeanUtils.copyProperties(providerCommunication, transfer);
				results.add(transfer);
			}
			
			return(results);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}
	
	public void deactivateProviderCommunication(Integer id)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			
			ProviderCommunication result=providerCommunicationDao.find(id);
			
			if (!result.getDestinationIntegratorFacilityId().equals(loggedInFacility.getId()))
			{
				logger.error("Some one deactivating some one elses message. messageId="+id+", loggedInFacility="+loggedInFacility.getId());
				return;
			}
			
			result.setActive(false);
			providerCommunicationDao.merge(result);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return;
		}
	}
}
