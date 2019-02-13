package org.oscarehr.caisi_integrator.dao.schema.upgrade_patches;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.oscarehr.caisi_integrator.dao.schema.UpgradePatchInterface;
import org.oscarehr.caisi_integrator.util.EncryptionUtils;

/**
 * Version 0 is before we did schema revisioning.
 * - need to create initial System Properties record
 * - need to encrypt Facility passwords
 */
public class UpgradeFrom0To1 implements UpgradePatchInterface
{
	@Override
	public void upgrade(EntityManager entityManager) throws Exception
	{
		upgradeSystemProperties(entityManager);
		upgradeFacility(entityManager);
		upgradeSiteUser(entityManager);
	}

	private static void upgradeSiteUser(EntityManager entityManager)
	{
		Query query;
		query=entityManager.createNativeQuery("select id,password from SiteUser");
		
		// [0] = Integer
		// [1] = byte[]
		@SuppressWarnings("unchecked")
		List<Object[]> pws=query.getResultList();
		for (Object[] pw : pws)
		{
			query=entityManager.createNativeQuery("update SiteUser set password=?1 where id=?2");
			
			byte[] encryptedPw=EncryptionUtils.getSha1(new String((byte[])pw[1]));
			query.setParameter(1, encryptedPw);
			query.setParameter(2, pw[0]);
			
			query.executeUpdate();
		}
	}

	private static void upgradeFacility(EntityManager entityManager)
	{
		Query query;
		query=entityManager.createNativeQuery("select id,password from Facility");
		
		// [0] = Integer
		// [1] = byte[]
		@SuppressWarnings("unchecked")
		List<Object[]> pws=query.getResultList();
		for (Object[] pw : pws)
		{
			query=entityManager.createNativeQuery("update Facility set password=?1 where id=?2");
			
			byte[] encryptedPw=EncryptionUtils.getSha1(new String((byte[])pw[1]));
			query.setParameter(1, encryptedPw);
			query.setParameter(2, pw[0]);
			
			query.executeUpdate();
		}
	}

	private static void upgradeSystemProperties(EntityManager entityManager)
	{
		Query query=entityManager.createNativeQuery("insert into SystemProperties (id, schemaVersion) values (1,1)");
		query.executeUpdate();
	}
}
