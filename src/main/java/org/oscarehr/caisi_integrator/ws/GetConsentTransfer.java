package org.oscarehr.caisi_integrator.ws;

import java.util.Date;

public class GetConsentTransfer
{
	public static enum ConsentState 
	{
		ALL, SOME, NONE;
	}
	
	public Integer integratorFacilityId=null;
	public ConsentState consentState=null;
	public Date consentDate=null;
}
