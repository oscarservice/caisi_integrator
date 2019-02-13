package org.oscarehr.caisi_integrator.dao;

import java.io.IOException;

import org.junit.Test;
import org.oscarehr.caisi_integrator.dao.schema.InitialiseDataStore;

/**
 * The purpose of this class is not for testing but to allow simple creation of a clean database.
 * This is no different than calling the initialisDataStore method but by putting it 
 * into maven, you don't have to setup your java classpath when running it like java -classpath ...... InitialisDataStore
 * cuz that can be a pain in the butt. This way maven sorts out the classpath for you. 
 */
public class MakeCleanDatabase
{
	@Test
	public void makeCleanDatabase() throws IOException
	{
		InitialiseDataStore.recreateDatabase();		
	}
}