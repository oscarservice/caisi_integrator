package org.oscarehr.caisi_integrator.ws;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.oscarehr.caisi_integrator.dao.CachedDemographicConsent;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsentDao;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsent.ConsentStatus;
import org.oscarehr.caisi_integrator.util.ConfigXmlUtils;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebService;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractWs
{
	@Resource
    protected WebServiceContext context;
	
	@Autowired
	protected EventLogDao eventLogDao;
	
	@Autowired
	private CachedDemographicConsentDao cachedDemographicConsentDao;

	protected static boolean requireConsentToRead=Boolean.parseBoolean(ConfigXmlUtils.getPropertyString("integrator_server", "require_consent_to_read_data"));
	
	protected HttpServletRequest getHttpServletRequest()
	{
		MessageContext messageContext=context.getMessageContext();
		HttpServletRequest request=(HttpServletRequest)messageContext.get(MessageContext.SERVLET_REQUEST);
		return(request);
	}
	
	protected Facility getLoggedInFacility()
	{
		LoggedInInfoWebService loggedInInfo=(LoggedInInfoWebService)LoggedInInfo.loggedInInfo.get();
		return(loggedInInfo.getFacility());
	}
	
	protected static boolean isAllowedToRead(CachedDemographicConsent cachedDemographicConsent, Integer integratorFacilityId)
	{
		if (!requireConsentToRead) return(true);
		
		if (cachedDemographicConsent==null) return(false);
		
		return(cachedDemographicConsent.allowedToShareData(integratorFacilityId));
	}
	
	protected static boolean isConsentGivenIfNeeded(CachedDemographicConsent cachedDemographicConsent)
	{
		if (!requireConsentToRead) return(true);
		
		return(cachedDemographicConsent!=null && cachedDemographicConsent.getClientConsentStatus() == ConsentStatus.GIVEN);
	}
	
	protected CachedDemographicConsent getCachedDemographicConsentIfNeeded(FacilityIdIntegerCompositePk pk)
	{
		if (!requireConsentToRead) return(null);
		
		return(cachedDemographicConsentDao.findLatestConsentFromAllLinkedClients(pk));
	}
}
