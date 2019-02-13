package org.oscarehr.caisi_integrator.dao.schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.oscarehr.caisi_integrator.util.ConfigXmlUtils;

/**
 * This class is a utility class to create and drop the database schema and all it's bits.
 * This class is highly database specific and alternate versions of this class will have to be 
 * written for other databases.
 */
public class SchemaUtils
{
	public static final String DEFAULT_ADMIN_USER="admin";
	public static final String DEFAULT_ADMIN_PASSWORD="admin";
	
	public static HashMap<String, String> getDbInfoAsMap()
	{
		HashMap<String,String> map=new HashMap<String,String>();
		
		String temp=ConfigXmlUtils.getPropertyString("database", "driver");
		map.put("driver", temp);
		
		temp=ConfigXmlUtils.getPropertyString("database", "user");
		map.put("user", temp);
		
		temp=ConfigXmlUtils.getPropertyString("database", "password");
		map.put("password", temp);
		
		temp=ConfigXmlUtils.getPropertyString("database", "url_prefix");
		map.put("url_prefix", temp);
		
		temp=ConfigXmlUtils.getPropertyString("database", "schema");
		map.put("schema", temp);
		
		return(map);
	}
	
	private static Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		String driver=ConfigXmlUtils.getPropertyString("database", "driver");
		String user=ConfigXmlUtils.getPropertyString("database", "user");
		String password=ConfigXmlUtils.getPropertyString("database", "password");
		String urlPrefix=ConfigXmlUtils.getPropertyString("database", "url_prefix");
		
		Class.forName(driver).newInstance();
		
		return(DriverManager.getConnection(urlPrefix, user, password));
	}
	
	public static boolean existsDatabase() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Connection c=getConnection();
		try
		{
			Statement s=c.createStatement();
			ResultSet rs=s.executeQuery("select count(*) from information_schema.schemata where schema_name='"+ConfigXmlUtils.getPropertyString("database", "schema")+'\'');
			if (rs.next()) return(rs.getInt(1)>=1);
			else throw(new RuntimeException("Unexpected null result set."));
		}
		finally
		{
			c.close();
		}
	}
}
