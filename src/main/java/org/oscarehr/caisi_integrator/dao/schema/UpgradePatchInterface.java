package org.oscarehr.caisi_integrator.dao.schema;

import javax.persistence.EntityManager;

public interface UpgradePatchInterface
{
	public void upgrade(EntityManager entityManager) throws Exception;
}
