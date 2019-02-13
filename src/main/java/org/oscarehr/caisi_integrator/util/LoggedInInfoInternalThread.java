package org.oscarehr.caisi_integrator.util;

import org.apache.log4j.Logger;

public class LoggedInInfoInternalThread extends LoggedInInfo
{
	private static final Logger logger=MiscUtils.getLogger();
	
	private String threadName = null;
	private String className = null;

	public String getThreadName()
	{
		return threadName;
	}

	public String getClassName()
	{
		return className;
	}

	/**
	 * This method is intended to be used by timer task or background threads to 
	 * setup the thread local loggedInInfo.
	 */
	public static void setLoggedInInfoToCurrentClassAndThreadName()
	{
		checkForLingeringData();

		// get caller
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		// create and set new thread local
		LoggedInInfoInternalThread x = new LoggedInInfoInternalThread();
		try
		{
			x.className = Class.forName(ste[2].getClassName()).getSimpleName();
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Serious error, the class should always be found.");
			throw(new RuntimeException(e));
		}
		
		x.threadName = Thread.currentThread().getName();
		loggedInInfo.set(x);
	}

	@Override
	public String getSourceString()
	{
		return(getSourcePrefix()+MiscUtils.ID_SEPARATOR+threadName+MiscUtils.ID_SEPARATOR+className);
	}

	@Override
	public String getSourcePrefix()
	{
		return("THREAD");
	}
}
