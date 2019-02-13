package org.oscarehr.caisi_integrator.ws;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebService;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class OscarUsernameTokenValidator extends UsernameTokenValidator
{

	private static Logger logger = MiscUtils.getLogger();
	private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
	private EventLogDao eventLogDao = (EventLogDao) SpringUtils.getBean("eventLogDao");

	@Override
	protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException
	{
		SoapMessage soapMessage = (SoapMessage) data.getMsgContext();
		HttpServletRequest request = (HttpServletRequest) soapMessage.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ipPort = request.getRemoteAddr() + ':' + request.getRemotePort();

		Facility facility = facilityDao.findByName(usernameToken.getName());

		if (facility == null)
		{
			eventLogDao.createLogicEventEntry("LOGIN" + MiscUtils.ID_SEPARATOR + "INVALID_USER_PASSWORD", "IP:" + ipPort);
			throw new WSSecurityException("Invalid Username/Password");
		}

		if (!facility.checkPassword(usernameToken.getPassword()))
		{
			eventLogDao.createLogicEventEntry("LOGIN" + MiscUtils.ID_SEPARATOR + "INVALID_USER_PASSWORD", "IP:" + ipPort);
			throw new WSSecurityException("Invalid Username/Password");
		}

		LoggedInInfoWebService loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();

		loggedInInfo.setAuthenticatedFacility(facility);

		logger.debug("authenticated "  + usernameToken.getName());
	}
}
