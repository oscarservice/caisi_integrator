package org.oscarehr.caisi_integrator.util;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.importer.ImportJob;
import org.oscarehr.caisi_integrator.importer.WSUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ContextStartupListener implements javax.servlet.ServletContextListener
{
	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void contextInitialized(javax.servlet.ServletContextEvent sce)
	{
		String contextPath = sce.getServletContext().getContextPath();
		logger.info("Server processes starting. context=" + contextPath);

		MiscUtils.addLoggingOverrideConfiguration(contextPath);
		VmStat.startContinuousLogging(Long.parseLong(ConfigXmlUtils.getPropertyString("misc", "vmstat_logging_period")));
		HomelessPopulationReportGeneratorTimerTask.startReportGeneration();

		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		WSUtils.setApplicationContext(applicationContext);

		ImportJob.start();

		logger.info("Server processes starting completed. context=" + contextPath);
	}

	@Override
	public void contextDestroyed(javax.servlet.ServletContextEvent sce)
	{
		logger.info("Server processes stopping. context=" + sce.getServletContext().getContextPath());

		HomelessPopulationReportGeneratorTimerTask.stopReportGeneration();
		ImportJob.stop();
		VmStat.stopContinuousLogging();
	}
}
