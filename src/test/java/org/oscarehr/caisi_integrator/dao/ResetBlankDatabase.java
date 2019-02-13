package org.oscarehr.caisi_integrator.dao;

import java.io.IOException;

import org.junit.Test;

/**
 * This class is meant to reset the database to a blank state useful for inital setup.
 */
public class ResetBlankDatabase
{
	@Test
	public void resetblankState() throws IOException 
	{
		DaoTestFixtures.createBlankDatabase();
	}
}
