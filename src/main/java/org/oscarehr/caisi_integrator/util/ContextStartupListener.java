package org.oscarehr.caisi_integrator.util;

import org.apache.log4j.Logger;

public class ContextStartupListener implements javax.servlet.ServletContextListener
{
	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void contextInitialized(javax.servlet.ServletContextEvent sce)
	{
		String contextPath=sce.getServletContext().getContextPath();
		logger.info("Server processes starting. context=" + contextPath);
		
		MiscUtils.addLoggingOverrideConfiguration(contextPath);		
		VmStat.startContinuousLogging(Long.parseLong(ConfigXmlUtils.getPropertyString("misc", "vmstat_logging_period")));
		HomelessPopulationReportGeneratorTimerTask.startReportGeneration();
		
		logger.info("Server processes starting completed. context=" + contextPath);
	}

	@Override
	public void contextDestroyed(javax.servlet.ServletContextEvent sce)
	{
		logger.info("Server processes stopping. context=" + sce.getServletContext().getContextPath());

		HomelessPopulationReportGeneratorTimerTask.stopReportGeneration();
		VmStat.stopContinuousLogging();
	}
}
