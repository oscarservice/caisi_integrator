package org.oscarehr.caisi_integrator.util;

import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.w3c.dom.Node;

public class SpringPropertyConfigurer extends PropertyPlaceholderConfigurer
{
	public SpringPropertyConfigurer()
	{
		HashMap<String, HashMap<String, Object>> configMap = ConfigXmlUtils.getConfig();

		Properties p = new Properties();

		for (String category : configMap.keySet())
		{
			HashMap<String, Object> categoryMap = configMap.get(category);

			for (String key : categoryMap.keySet())
			{
				Object value = categoryMap.get(key);

				// single entry adverse to list
				if (value instanceof Node)
				{
					String categoryKey = category + '.' + key;
					String valueString = ((Node)value).getTextContent();
					p.put(categoryKey, valueString);
					logger.debug("Setting " + categoryKey + '=' + valueString);
				}
			}
		}

		setProperties(p);
	}
}