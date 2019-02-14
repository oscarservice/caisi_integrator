package org.oscarehr.caisi_integrator.ws.transfer;

import java.io.Serializable;
import java.util.Date;

public class GetConsentTransfer implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3463614258318704132L;

	public static enum ConsentState
	{
		ALL, SOME, NONE;
	}

	public Integer integratorFacilityId = null;
	public ConsentState consentState = null;
	public Date consentDate = null;
}
