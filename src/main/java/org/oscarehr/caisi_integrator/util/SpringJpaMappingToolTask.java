package org.oscarehr.caisi_integrator.util;

import org.apache.openjpa.jdbc.ant.MappingToolTask;
import org.apache.openjpa.jdbc.conf.JDBCConfigurationImpl;
import org.apache.openjpa.lib.conf.ConfigurationImpl;

public class SpringJpaMappingToolTask extends MappingToolTask
{
	@Override
	public ConfigurationImpl newConfiguration()
	{
		System.setProperty("catalina.base", System.getProperty("java.io.tmpdir"));
		
		JDBCConfigurationImpl conf=(JDBCConfigurationImpl)super.newConfiguration();
		conf.setConnectionDriverName(ConfigXmlUtils.getPropertyString("database", "driver"));
		conf.setConnectionURL(ConfigXmlUtils.getPropertyString("database", "url_prefix")+ConfigXmlUtils.getPropertyString("database", "schema"));
		conf.setConnectionUserName(ConfigXmlUtils.getPropertyString("database", "user"));
		conf.setConnectionPassword(ConfigXmlUtils.getPropertyString("database", "password"));
		
		return(conf);
	}
}
