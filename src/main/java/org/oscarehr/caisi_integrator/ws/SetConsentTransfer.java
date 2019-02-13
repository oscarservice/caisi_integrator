package org.oscarehr.caisi_integrator.ws;

import java.util.Date;

public class SetConsentTransfer
{
	public static class FacilityConsentPair
	{
		public Integer remoteFacilityId=null;
		public boolean shareData=false;
	}
	
	public Integer demographicId=null;
	public String consentStatus=null;
	public Date createdDate=null;
	public boolean excludeMentalHealthData=false;
	public FacilityConsentPair[] consentToShareData=null;
	public Date expiry=null;
}
