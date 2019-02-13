package org.oscarehr.caisi_integrator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConfigXmlUtilsTest
{
	@Test
	public void getConfigTest()
	{
		System.setProperty("caisi_integrator_config", "/over_ride_config.xml");

		ConfigXmlUtils.reloadConfig();
		
		String temp=ConfigXmlUtils.getPropertyString("misc", "override_config_sytem_property_key");
		assertNotNull(temp);
		
		temp=ConfigXmlUtils.getPropertyString("misc", "build_date_time");
		assertEquals("foo", temp);
	}
}
