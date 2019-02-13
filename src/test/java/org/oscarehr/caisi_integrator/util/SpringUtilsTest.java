package org.oscarehr.caisi_integrator.util;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;

public class SpringUtilsTest
{
	@Test
	public void dataSourceTest() throws SQLException
	{
		DataSource dataSource=(DataSource)SpringUtils.getBean("dataSource");

		Connection c=dataSource.getConnection();
		Statement s=c.createStatement();
		ResultSet rs=s.executeQuery("select now()");
		rs.next();
		
		String result=rs.getString(1);
		assertNotNull(result);
		
		rs.close();
		s.close();
		c.close();		
	}
}
