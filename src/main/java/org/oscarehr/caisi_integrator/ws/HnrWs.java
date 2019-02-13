package org.oscarehr.caisi_integrator.ws;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.util.ConfigXmlUtils;
import org.oscarehr.caisi_integrator.util.CxfClientUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.hnr.ws.Client;
import org.oscarehr.hnr.ws.ClientWs;
import org.oscarehr.hnr.ws.ClientWsService;
import org.oscarehr.hnr.ws.DuplicateHinException_Exception;
import org.oscarehr.hnr.ws.InvalidHinException_Exception;
import org.oscarehr.hnr.ws.MatchingClientParameters;
import org.oscarehr.hnr.ws.MatchingClientScore;
import org.springframework.stereotype.Component;

@WebService
@Component
public class HnrWs extends AbstractWs
{
	private static final Logger logger = MiscUtils.getLogger();

	private static final String hnrEndPointUrl = ConfigXmlUtils.getPropertyString("hnr_server", "endpoint_url_base");
	private static final String hnrUser = ConfigXmlUtils.getPropertyString("hnr_server", "user");
	private static final String hnrPassword = ConfigXmlUtils.getPropertyString("hnr_server", "password");
	private static final long MAX_CONNECTION_EXCEPTION_LOG_PERIOD = 600000;
	private static long lastConnectionExceptionLogMessageTime = 0;

	private ClientWs getClientWs() throws MalformedURLException
	{
		logger.debug("Contacting HNR with " + hnrEndPointUrl + ", " + hnrUser + ", " + hnrPassword);

		URL url = CxfClientUtils.buildURL(hnrEndPointUrl, "ClientService");
		ClientWsService service = new ClientWsService(url);
		ClientWs port = service.getClientWsPort();

		CxfClientUtils.configureClientConnection(port);
		CxfClientUtils.addWSS4JAuthentication(hnrUser, hnrPassword, port);

		return(port);
	}

	public List<MatchingClientScore> getMatchingHnrClients(MatchingClientParameters parameters) throws ConnectException
	{
		try
		{
			ClientWs clientWs = getClientWs();
			List<MatchingClientScore> results = clientWs.getMatchingClients(parameters);

			if (results != null)
				return(results);
		}
		catch (Exception e)
		{
			logAndRethrowConnectionException(e);
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	public Integer setHnrClientData(Client client) throws DuplicateHinException_Exception, InvalidHinException_Exception, ConnectException
	{
		try
		{
			ClientWs clientWs = getClientWs();
			return(clientWs.setClientData(client));
		}
		catch (InvalidHinException_Exception e)
		{
			throw(e);
		}
		catch (DuplicateHinException_Exception e)
		{
			throw(e);
		}
		catch (Exception e)
		{
			logAndRethrowConnectionException(e);
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	public Client getHnrClient(@WebParam(name = "linkingId") Integer linkingId) throws ConnectException
	{
		try
		{
			ClientWs clientWs = getClientWs();
			Client client = clientWs.getClientData(linkingId);
			return(client);
		}
		catch (Exception e)
		{
			logAndRethrowConnectionException(e);
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	public void setHnrClientHidden(Integer clientLinkingId, boolean hidden, Date hiddenChangeDate) throws ConnectException
	{
		try
		{
			ClientWs clientWs = getClientWs();
			clientWs.setClientHidden(clientLinkingId, hidden, hiddenChangeDate);
		}
		catch (Exception e)
		{
			logAndRethrowConnectionException(e);
			logger.error("Unexpected Error.", e);
		}
	}

	/**
	 * @return true if it was a connection exception.
	 * @throws ConnectException 
	 * @throws Exception 
	 */
	private static void logAndRethrowConnectionException(Throwable t) throws ConnectException
	{
		if (CxfClientUtils.isConnectionException(t))
		{
			synchronized (HnrWs.class)
			{
				if (System.currentTimeMillis() - lastConnectionExceptionLogMessageTime > MAX_CONNECTION_EXCEPTION_LOG_PERIOD)
				{
					logger.warn("Error connecting to HNR. " + t.getCause().getCause().getMessage());
					logger.warn("Connection exception will be squelched for the next " + MAX_CONNECTION_EXCEPTION_LOG_PERIOD + "ms.");
					lastConnectionExceptionLogMessageTime = System.currentTimeMillis();
				}
			}

			throw(new java.net.ConnectException("HNR Server unavailable."));
		}
	}
}
