package org.oscarehr.caisi_integrator.dao.schema;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.SiteUserDao;
import org.oscarehr.caisi_integrator.dao.SystemPropertiesDao;
import org.oscarehr.caisi_integrator.util.AdditionalSchemaGenerationSql;
import org.oscarehr.caisi_integrator.util.JpaUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InitialiseDataStore
{
	private static final Logger logger = MiscUtils.getLogger();

	private static void createDbItems() throws IOException
	{
		// get table generated sql
		@SuppressWarnings("unchecked")
		List<String> sqlLines = FileUtils.readLines(new File("target/create_tables.sql"));

		// --- add custom sql ---
		addAdditionalSql(sqlLines);

		// run sql
		EntityManager entityManager = JpaUtils.createEntityManager();
		try
		{
			EntityTransaction tx=entityManager.getTransaction();
			tx.begin();

			for (String sql : sqlLines)
			{
				logger.debug(sql);
				Query query = entityManager.createNativeQuery(sql);
				query.executeUpdate();
			}

			tx.commit();
		}
		finally
		{
			JpaUtils.close(entityManager);
		}
		
		SystemPropertiesDao systemPropertiesDao=(SystemPropertiesDao)SpringUtils.getBean("systemPropertiesDao");
		systemPropertiesDao.createInitialEntry();

		SiteUserDao siteUserDao=(SiteUserDao)SpringUtils.getBean("siteUserDao");
		siteUserDao.createInitialEntry();
	}

	private static void addAdditionalSql(List<String> sqlLines)
	{
		HashMap<Class<? extends Object>, Object> daoClasses = getDaoClassesFromSpring();

		for (Map.Entry<Class<? extends Object>, Object> entry : daoClasses.entrySet())
		{
			Class<? extends Object> c = entry.getKey();

			while (c != Object.class)
			{
				for (Method m : c.getDeclaredMethods())
				{
					if (m.isAnnotationPresent(AdditionalSchemaGenerationSql.class))
					{
						try
						{
							String[] sql = (String[])m.invoke(entry.getValue());
							if (sql != null)
							{
								logger.info("Found additional schema sql : " + c.getName() + "." + m.getName());
								for (String s : sql)
								{
									sqlLines.add(s);
								}
							}
						}
						catch (Exception e)
						{
							logger.error("Error calling annotated method : " + c.getName() + "." + m.getName(), e);
						}
					}
				}

				c = c.getSuperclass();
			}
		}
	}

	private static HashMap<Class<? extends Object>, Object> getDaoClassesFromSpring()
	{
		// make sure we only return 1 instance of a class even if it's spring loaded twice.
		HashMap<Class<? extends Object>, Object> classes = new HashMap<Class<? extends Object>, Object>();

		ClassPathXmlApplicationContext context = SpringUtils.getClassPathXmlApplicationContext();
		String[] allBeanNames = context.getBeanDefinitionNames();

		for (String beanName : allBeanNames)
		{
			if (beanName.endsWith("Dao"))
			{
				Object bean = SpringUtils.getBean(beanName);
				Class<? extends Object> c = bean.getClass();
				classes.put(c, bean);
			}
		}

		return(classes);
	}

	private static void dropCurrentAndCreateBlankDatabase()
	{
		EntityManager entityManager = JpaUtils.createEntityManager();
		try
		{
			EntityTransaction tx = entityManager.getTransaction();
			tx.begin();

			Query query = entityManager.createNativeQuery("select database()", String.class);
			String databaseName = (String)query.getSingleResult();

			query = entityManager.createNativeQuery("drop database if exists " + databaseName);
			query.executeUpdate();

			query = entityManager.createNativeQuery("create database " + databaseName);
			query.executeUpdate();

			query = entityManager.createNativeQuery("use " + databaseName);
			query.executeUpdate();

			tx.commit();
		}
		finally
		{	
			JpaUtils.close(entityManager);
		}
	}

	public static void recreateDatabase() throws IOException
	{
		dropCurrentAndCreateBlankDatabase();
		createDbItems();
	}

	public static void main(String... argv) throws Exception
	{
		recreateDatabase();
		logger.info("Create Database completed.");
	}

}
