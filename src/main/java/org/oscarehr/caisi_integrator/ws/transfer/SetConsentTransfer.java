package org.oscarehr.caisi_integrator.ws.transfer;

import java.io.Serializable;
import java.util.Date;

public class SetConsentTransfer implements Serializable
{
	private static final long serialVersionUID = 4325515668690216108L;

	public static class FacilityConsentPair
	{
		public Integer remoteFacilityId = null;
		public boolean shareData = false;
	}

	public Integer demographicId = null;
	public String consentStatus = null;
	public Date createdDate = null;
	public boolean excludeMentalHealthData = false;
	public FacilityConsentPair[] consentToShareData = null;
	public Date expiry = null;
}
