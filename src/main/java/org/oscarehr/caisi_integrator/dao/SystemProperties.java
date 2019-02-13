package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PreRemove;

@Entity
public class SystemProperties extends AbstractModel<Integer>
{
	public static final int SYSTEM_PROPERTY_ID = 1;
	
	/**
	 * This value should be incremented every time a schema change is made.
	 */
	public static final int CODE_SCHEMA_VERSION=1; 
	
	/**
	 * There's only 1 row for system properties so we'll hard code the id column to 1. 
	 */
	@Id
	private Integer id = new Integer(SYSTEM_PROPERTY_ID);

	/**
	 * This number is hard coded to the source code revision.
	 * Every time the schema changes / model objects change, this number should
	 * be incremented. Subsequently an upgrade script should be written.
	 * The expectation is that there will be an upgrade script between every previous version to the current version.
	 * i.e. there will be upgrade scripts from 0->1, 1->2, 2->3. As such to upgrade from 0 to 3, you just run all
	 * the upgrades in sequence.
	 */
	private int schemaVersion = CODE_SCHEMA_VERSION;

	@Override
	public Integer getId()
	{
		return id;
	}

	public int getSchemaVersion()
	{
		return schemaVersion;
	}
	
	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}
}
