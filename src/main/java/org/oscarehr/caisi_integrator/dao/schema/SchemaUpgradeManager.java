package org.oscarehr.caisi_integrator.dao.schema;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.SystemProperties;
import org.oscarehr.caisi_integrator.dao.SystemPropertiesDao;
import org.oscarehr.caisi_integrator.util.JpaUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.SpringUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public final class SchemaUpgradeManager
{
	private static final Logger logger=MiscUtils.getLogger();

	private static SystemPropertiesDao systemPropertiesDao = (SystemPropertiesDao)SpringUtils.getBean("systemPropertiesDao");

	/**
	 * Intended for existing installations.
	 * @throws Exception 
	 * @throws exception on error, partial upgrade maybe completed, error upgrade should be rolled back.
	 */
	public static void upgradeDatabase() throws Exception
	{
		int currentDatabaseVersion=getDatabaseDataVersion();
		int codeVersion=SystemProperties.CODE_SCHEMA_VERSION;

		for (int i=currentDatabaseVersion; i<codeVersion; i++)
		{
			upgradeDatabase(i);
		}
	}

	/**
	 * @throws Exception 
	 * @throws exception on error, transaction will be rolled back on error, commited on success.
	 */
	private static void upgradeDatabase(int version) throws Exception
	{
		logger.info("Start upgrading database from "+version+" to "+(version+1));		

		JpaTransactionManager txManager=(JpaTransactionManager)SpringUtils.getBean("txManager");
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		EntityManager entityManager=JpaUtils.createEntityManager();
		entityManager.joinTransaction();
		try 
		{
			@SuppressWarnings("unchecked")
			Class<UpgradePatchInterface> c=(Class<UpgradePatchInterface>)Class.forName("org.oscarehr.caisi_integrator.dao.schema.upgrade_patches.UpgradeFrom"+version+"To"+(version+1));
			UpgradePatchInterface upgradePatch=c.newInstance();

			upgradePatch.upgrade(entityManager);
			
			txManager.commit(status);
		}
		finally 
		{
		    if (!status.isCompleted()) txManager.rollback(status);
		}
		
		logger.info("Finished upgrading database from "+version+" to "+(version+1));		
	}

	private static int getDatabaseDataVersion()
	{
		SystemProperties systemProperties=null;
		
		try
		{
			systemProperties=systemPropertiesDao.find();
			return(systemProperties.getSchemaVersion());
		}
		catch (Exception e)
		{
			// no row, i.e. version 0
			return(0);
		}		
	}
}