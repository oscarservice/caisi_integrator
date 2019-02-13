package org.oscarehr.caisi_integrator.util;

import org.apache.openjpa.ant.PCEnhancerTask;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.lib.conf.ConfigurationImpl;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.log.LogFactoryImpl;

public class LoggingPcEnhancerTask extends PCEnhancerTask
{
	@Override
	protected ConfigurationImpl newConfiguration()
	{
		OpenJPAConfigurationImpl conf = new OpenJPAConfigurationImpl();

		LogFactoryImpl.LogImpl log = (LogFactoryImpl.LogImpl)conf.getLog(OpenJPAConfiguration.LOG_ENHANCE);
		log.setLevel(Log.TRACE);

		return conf;
	}
}
