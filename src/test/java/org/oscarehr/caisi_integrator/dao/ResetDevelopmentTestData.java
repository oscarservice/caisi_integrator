package org.oscarehr.caisi_integrator.dao;

import org.junit.Test;

/**
 * This class is meant to reset the database to a state which is useful
 * for development purposes and manual testing. This is not actually a junit test
 * but we're using the framework to bootstrap running the code.
 * <br /><br />
 * It seems like this class isn't run with the default mvn tests because the class
 * name doesn't end in "Test" so it doesn't find it. This is a good side effect
 * since we don't want it to. In the future if it does start running with all tests,
 * we will need to add a "if system.getpropert("test") equals this class name" 
 * then run the methods, otherwise skip it. This would ensure only explicit calls
 * to this one class would cause this resetting to be run.
 */
public class ResetDevelopmentTestData extends DaoTestFixtures
{
	@Test
	public void resetDatabaseToDevelopmentState() 
	{
		// should be good enough :), uses the DaoTestFixtures
	}
}
