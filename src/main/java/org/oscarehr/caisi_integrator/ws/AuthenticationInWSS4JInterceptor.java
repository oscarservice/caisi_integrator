package org.oscarehr.caisi_integrator.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebService;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;
import org.w3c.dom.Element;

public class AuthenticationInWSS4JInterceptor extends WSS4JInInterceptor implements CallbackHandler
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final QName REQUESTING_CAISI_PROVIDER_NO_QNAME = new QName("http://oscarehr.org/caisi", "requestingCaisiProviderNo", "caisi");

	// the interceptor is not in a context so we can't use autowire
	private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
	private EventLogDao eventLogDao = (EventLogDao) SpringUtils.getBean("eventLogDao");

	private ArrayList<String> excludes = null;

	public AuthenticationInWSS4JInterceptor()
	{
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		properties.put(WSHandlerConstants.PW_CALLBACK_REF, this);

		setProperties(properties);
	}

	@Override
	public void handleMessage(SoapMessage message)
	{
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);

		String caisiProviderId = getRequestingCaisiProviderId(message);
		LoggedInInfoWebService.setLoggedInInfo(caisiProviderId, request.getRemoteAddr());

		// if excluded from authentication
		String basePath = (String) message.get(SoapMessage.BASE_PATH);
		if (isExcluded(basePath)) return;

		super.handleMessage(message);
	}

	public void setExcludes(ArrayList<String> excludes)
	{
		this.excludes = excludes;
	}

	private boolean isExcluded(String s)
	{
		// this means it's a response to my request - still registers as in bound even though it's just an inbound response.  
		if (s == null) return(true);

		for (String x : excludes)
		{
			if (s.endsWith(x)) return(true);
		}

		return(false);
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
	{
		for (Callback callback : callbacks)
		{
			if (callback instanceof WSPasswordCallback)
			{
				WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callback;

				String username = wsPasswordCallback.getIdentifier();
				String password = wsPasswordCallback.getPassword();

				if (logger.isDebugEnabled())
				{
					logger.debug("WSPasswordCallback: u=" + username + ", p=" + password);
				}

				Facility facility = facilityDao.findByName(username);

				LoggedInInfoWebService loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
				//Facility facility = getByUserPasswordAndActive(username, password);
				if (facility == null)
				{
					String errorString = "Invalid username/password attempted. username=" + username + ", pw=" + password;
					logger.warn(errorString);

					eventLogDao.createLogicEventEntry("LOGIN" + MiscUtils.ID_SEPARATOR + "INVALID_USER_PASSWORD", "IP:" + loggedInInfo.getRequestingIp());

					throw(new SecurityException(errorString));
				}
				else
				{
					if (facility != null && !facility.isDisabled())
					{
						wsPasswordCallback.setPassword(facility.getPasswordAsBase64());

						//this won't be used if the password doesn't authenticate
						loggedInInfo.setAuthenticatedFacility(facility);

						return;
					}
				}
			}
		}

		throw(new SecurityException("Odd, no password check call back made."));
	}

	/**
	 * @return facility if valid or null if invalid
	 */
	/*
	private Facility getByUserPasswordAndActive(String username, String password)
	{
		if (username == null || password == null) return(null);

		Facility facility = facilityDao.findByName(username);
		if (facility != null && !facility.isDisabled() && facility.checkPassword(password)) return(facility);
		else return(null);
	}
	*/

	private String getRequestingCaisiProviderId(SoapMessage message)
	{
		String caisiProviderId = null;

		Header header = message.getHeader(REQUESTING_CAISI_PROVIDER_NO_QNAME);
		if (header != null)
		{
			Element element = (Element) header.getObject();
			if (element != null)
			{
				caisiProviderId = element.getTextContent();
			}
		}

		logger.debug("Using caisiProviderId : " + caisiProviderId);
		return(caisiProviderId);
	}
}
