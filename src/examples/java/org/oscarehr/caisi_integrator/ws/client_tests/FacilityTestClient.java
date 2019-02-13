package org.oscarehr.caisi_integrator.ws.client_tests;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.oscarehr.caisi_integrator.util.CxfClientUtils;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.FacilityWsService;

public class FacilityTestClient
{
	public static void main(String... argv) throws Exception
	{
		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();

		// URL url = new URL("http://127.0.0.1:8086/caisi_integrator/ws/FacilityService?wsdl");
		// FacilityWsService service=new FacilityWsService(url);		
		FacilityWsService service = new FacilityWsService();
		FacilityWs facilityPort = service.getFacilityWsPort();

		CxfClientUtils.configureClientConnection(facilityPort);
		CxfClientUtils.addWSS4JAuthentication("facility1", "password1", facilityPort);

		// print facility info
		for (CachedFacility cachedFacility : facilityPort.getAllFacility())
		{
			System.err.println(""+cachedFacility.getIntegratorFacilityId()+", "+cachedFacility.getName()+", "+cachedFacility.getDescription());
		}

		// update facility info
		{
			CachedFacility cachedFacility=new CachedFacility();
			cachedFacility.setName("my new facility name "+(new java.util.Date()));
			cachedFacility.setDescription("my new facility description "+(new java.util.Date()));
			facilityPort.setMyFacility(cachedFacility);
		}

		// print facility info
		for (CachedFacility cachedFacility : facilityPort.getAllFacility())
		{
			System.err.println(""+cachedFacility.getIntegratorFacilityId()+", "+cachedFacility.getName()+", "+cachedFacility.getDescription());
		}

	}

}