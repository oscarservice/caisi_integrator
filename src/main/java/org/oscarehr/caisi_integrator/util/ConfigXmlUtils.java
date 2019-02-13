package org.oscarehr.caisi_integrator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility to read configuration from an xml file with the default being classpath:/config.xml
 * This class replaces the old Config Utils and config properties file. 
 * 
 * The structure of the xml config is expected to be like the following :
 * 
 * <server>
 * 	<misc>
 * 		<build_date_time>${build.dateTime}</build_date_time>
 * 		<vmstat_logging_period>900000</vmstat_logging_period>
 * 		<override_config_sytem_property_key>myoscar_server_config</override_config_sytem_property_key>
 * 		...
 * 	</misc>
 * 	<database>
 * 		<user>dbuser</user>
 * 		<password>dbpassword</password>
 * 		...
 * 	</database>
 * 	<myoscar_server>
 * 		<permission_set_class>org.oscarehr.myoscar_server.managers.SimpleRelationshipPermissionsSet</permission_set_class>
 * 
 * 		<after_event_listenter list_entry="true" clear_list="true">com.example.ListenerTest1</after_event_listenter>
 * 		<after_event_listenter list_entry="true">com.example.ListenerTest2</after_event_listenter>
 * 		<after_event_listenter list_entry="true">com.example.ListenerTest3</after_event_listenter>
 * 	</myoscar_server>
 * </server>
 * 
 * The first level should be just the <server> tag.
 * The second level nodes should not contain data, they should be categories for data only.
 * The third level is where data should be contained. i.e. server -> misc -> build_date_time.
 * There's no current support for items nested any further than the third level.
 * 
 * Most single entry properties can just be of that format and a convenience method is provided
 * in this class to directly access those values.
 * 
 * Sub categories and list of items can exist as per the <after_event_listenter list_entry="true"> tag example. 
 * A convenience method is provided to directly access a list of value too. A list is denoted via the list_entry="true"
 * attribute, otherwise an entry is assumed to be a single value entry and will replace the previous value.
 * 
 * Note the <override_config_sytem_property_key> tag, this value is used by this class itself to see if there
 * is a nother config to overlay onto the default config. The rules for overlaying is that any item entry 
 * at the third level will over-write the default values (excluding the override_config_sytem_property_key itself).
 * So as an example if you have another xml file that defines server -> database -> user, your value will be 
 * used instead of the default. But if you didn't specify the server -> database -> password, the default will 
 * still be used. Note the implications for lists of values though. In the above example <after_event_listenter> 
 * is a list of entries. If you declare more after_event_listenter tags in your override file they will append
 * to the default values. If you want to clear existing values put clear_list="true" and it will clear the list
 * from the point on. 
 * 
 * From a code point of view, parsing xml is relatively inefficient and these values maybe requested often, so
 * upon startup we will read the values into a HashMap instead to improve performance. The map will be 
 * HashMap<String1, HashMap<String2, Object1>> where String1 = category node name, String2=third level node name,
 * and Object1 is either the Node or ArrayList<Node>.
 */
public final class ConfigXmlUtils
{
	private static final Logger logger = MiscUtils.getLogger();

	private static final String DEFAULT_CONFIG_FILE = "/config.xml";

	private static HashMap<String, HashMap<String, Object>> config = getConfigMap();

	private static HashMap<String, HashMap<String, Object>> getConfigMap()
	{
		try
		{
			HashMap<String, HashMap<String, Object>> results = new HashMap<String, HashMap<String, Object>>();

			readFileIntoMap(DEFAULT_CONFIG_FILE, results);

			String overrideFilenameSystemPropertiesKey = ((Node)getProperty(results, "misc", "override_config_sytem_property_key")).getTextContent();
			if (overrideFilenameSystemPropertiesKey != null)
			{
				String overrideFilename = System.getProperty(overrideFilenameSystemPropertiesKey);
				if (overrideFilename != null) readFileIntoMap(overrideFilename, results);
			}

			return(results);
		}
		catch (Exception e)
		{
			logger.error("Error initialising ConfigXmlUtils", e);
			throw(new RuntimeException(e));
		}
	}

	private static void readFileIntoMap(String fileName, HashMap<String, HashMap<String, Object>> map) throws ParserConfigurationException, SAXException, IOException
	{
		logger.info("Reading config file into map : " + fileName);

		InputStream is = ConfigXmlUtils.class.getResourceAsStream(fileName);

		if (is == null) is = new FileInputStream(fileName);

		try
		{
			Document doc = XmlUtils.toDocument(is);

			Node rootNode = doc.getFirstChild();

			NodeList categories = rootNode.getChildNodes();
			for (int i = 0; i < categories.getLength(); i++)
			{
				putCatetoryIntoMap(categories.item(i), map);
			}
		}
		finally
		{
			is.close();
		}
	}

	private static void putCatetoryIntoMap(Node category, HashMap<String, HashMap<String, Object>> map)
	{
		String categoryName = StringUtils.trimToNull(category.getNodeName());
		if (categoryName == null) return;

		NodeList properties = category.getChildNodes();
		for (int i = 0; i < properties.getLength(); i++)
		{
			putPropertyIntoMap(categoryName, properties.item(i), map);
		}
	}

	private static void putPropertyIntoMap(String categoryName, Node property, HashMap<String, HashMap<String, Object>> map)
	{
		if (property.getNodeType()!=Node.ELEMENT_NODE) return;
		
		String propertyName = StringUtils.trimToNull(property.getNodeName());

		// make sure the category exists
		HashMap<String, Object> categoryMap = map.get(categoryName);
		if (categoryMap == null)
		{
			categoryMap = new HashMap<String, Object>();
			map.put(categoryName, categoryMap);
		}

		// get the attributes
		String tempString=XmlUtils.getAttributeValue(property, "list_entry");
		boolean isList = Boolean.parseBoolean(tempString);
		
		tempString=XmlUtils.getAttributeValue(property, "clear_list");
		boolean clearList = Boolean.parseBoolean(tempString);

		
		// clear value of it's specified 
		if (clearList) categoryMap.remove(propertyName);

		// add value
		if (isList)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Node> list = (ArrayList<Node>)categoryMap.get(propertyName);
			if (list == null)
			{
				list = new ArrayList<Node>();
				categoryMap.put(propertyName, list);
			}

			list.add(property);
		}
		else
		{
			categoryMap.put(propertyName, property);
		}
	}

	private static Object getProperty(HashMap<String, HashMap<String, Object>> map, String category, String property)
	{
		HashMap<String, Object> categoryMap = map.get(category);
		if (categoryMap == null) return(null);

		return(categoryMap.get(property));
	}

	/**
	 * This is a relatively expensive call, don't wear it out.
	 */
	public static void reloadConfig()
	{
		config = getConfigMap();
	}

	/**
	 * @return the string property or null if it doesn't exist.
	 */
	public static String getPropertyString(String category, String property)
	{
		Node node = (Node)getProperty(config, category, property);
		if (node != null) return(StringUtils.trimToNull(node.getTextContent()));
		else return(null);
	}

	/**
	 * @return an arraylist of string properties or null if the xml entry never existed at all.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPropertyStringList(String category, String property)
	{
		ArrayList<Node> nodeList = (ArrayList<Node>)getProperty(config, category, property);
		if (nodeList != null)
		{
			ArrayList<String> stringList = new ArrayList<String>();
			for (Node n : nodeList)
			{
				stringList.add(n.getTextContent());
			}
			return(stringList);
		}
		else return(null);
	}

	/**
	 * @return the Node or null if it doesn't exist.
	 */
	public static Node getPropertyNode(String category, String property)
	{
		Node node = (Node)getProperty(config, category, property);
		return(node);
	}

	/**
	 * @return an arraylist of Node properties or null if the xml entry never existed at all.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Node> getPropertyNodeList(String category, String property)
	{
		ArrayList<Node> nodeList = (ArrayList<Node>)getProperty(config, category, property);
		return(nodeList);
	}

	public static HashMap<String, HashMap<String, Object>> getConfig()
	{
		return config;
	}
}
