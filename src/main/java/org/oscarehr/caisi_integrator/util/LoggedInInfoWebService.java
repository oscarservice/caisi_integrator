package org.oscarehr.caisi_integrator.util;

import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.CachedProvider;
import org.oscarehr.caisi_integrator.dao.CachedProviderDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdStringCompositePk;

public class LoggedInInfoWebService extends LoggedInInfo
{
	@Transient
	private CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");

	private Facility facility = null;
	private CachedProvider cachedProvider = null;
	private boolean duringAuthentication=false;
	
	/** To be used internally only because of the flow of control in WS's */
	private String caisiProviderId=null;
	/** To be used internally only because of the flow of control in WS's */
	private String requestingIp=null;
	
	public Facility getFacility()
	{
		return facility;
	}

	public CachedProvider getCachedProvider()
	{
		return cachedProvider;
	}

	public String getRequestingIp()
	{
		return requestingIp;
	}

	/**
	 * This method is intended to be used by authentication filter of the web services.
	 * Needs to set facility manually.
	 */
	public static void setLoggedInInfo(String caisiProviderId, String requestingIp)
	{
		checkForLingeringData();

		// create and set new thread local
		LoggedInInfoWebService x = new LoggedInInfoWebService();
		x.caisiProviderId=caisiProviderId;
		x.requestingIp=requestingIp;
		x.duringAuthentication=true;
		loggedInInfo.set(x);
	}

	public void setAuthenticatedFacility(Facility facility)
	{
		duringAuthentication=false;
		this.facility=facility;
		
		if (caisiProviderId!=null)
		{
			FacilityIdStringCompositePk id=new FacilityIdStringCompositePk(facility.getId(), caisiProviderId);
			cachedProvider=cachedProviderDao.find(id);
		}
	}
	
	@Override
	public String getSourceString()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append(getSourcePrefix());
		sb.append(MiscUtils.ID_SEPARATOR);
		
		if (duringAuthentication) sb.append("DURING_AUTHENTICATION");
		else sb.append(facility.getId());
		
		sb.append(MiscUtils.ID_SEPARATOR);
		
		// cached provider can be null on data pushes
		if (cachedProvider!=null) sb.append(cachedProvider.getFacilityIdStringCompositePk().getCaisiItemId());
		else sb.append(MiscUtils.NULL_ID_PLACEHOLDER);
		
		return(sb.toString());
	}

	@Override
	public String getSourcePrefix()
	{
		return("WS");
	}
}
