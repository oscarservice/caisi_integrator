package org.oscarehr.caisi_integrator.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

/**
 * LoggedInInfo is a thread local variable letting any/all code know who initiated this code, ie. internal thread, web site, or web service.
 * 
 * It is the responsibility of the start of each running of code to set the logged in info, and to clear it at the end of the running of code.
 * As an example for web site requests that can be done as a filter, before chaining set the logged in info, then after chaining clear it in a finally block.
 * For threads, set it at the beginning of the run method, and clear it in a finally block that surrounds all the logic.
 * For web services either a filter or an intercetor should be setup to acompolish the same thing as the web site routine.
 * 
 */
public abstract class LoggedInInfo
{
	private static final Logger logger=MiscUtils.getLogger();
	public static final ThreadLocal<LoggedInInfo> loggedInInfo = new ThreadLocal<LoggedInInfo>();
	public static final String PUBLIC_SOURCE_STRING="PUBLIC_ACCESS";
	
	/**
	 * Source String should be something like "SITE:52" etc.
	 */
	public abstract String getSourceString();
	
	/**
	 * As an example source prefixes might be "THREAD" or "SITE" or "WS"
	 * indicating the source of the request.
	 */
	public abstract String getSourcePrefix();
	
	@Override
	public String toString()
	{
		return(ReflectionToStringBuilder.toString(this));
	}
	
	public static void checkForLingeringData()
	{
		LoggedInInfo x = loggedInInfo.get();
		if (x != null) logger.warn("Logged in info should be null on new requests but it wasn't. oldUser=" + x);				
	}
}