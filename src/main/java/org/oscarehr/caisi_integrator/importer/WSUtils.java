package org.oscarehr.caisi_integrator.importer;

import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.ProgramWs;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.springframework.context.ApplicationContext;

public class WSUtils
{
	static ApplicationContext ctx;

	public static void setApplicationContext(ApplicationContext c)
	{
		ctx = c;
	}

	public static FacilityWs getFacilityWs()
	{
		return (FacilityWs) ctx.getBean("facilityWs");
	}

	public static ProviderWs getProviderWs()
	{
		return (ProviderWs) ctx.getBean("providerWs");
	}

	public static DemographicWs getDemographicWs()
	{
		return (DemographicWs) ctx.getBean("demographicWs");
	}

	public static ProgramWs getProgramWs()
	{
		return (ProgramWs) ctx.getBean("programWs");
	}

	public static FacilityDao getFacilityDao()
	{
		return (FacilityDao) ctx.getBean("facilityDao");
	}
}
