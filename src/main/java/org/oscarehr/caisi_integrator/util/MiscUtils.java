/*
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. 
 * 
 * This software was written for 
 * MB Software, margaritabowl.com
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.caisi_integrator.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class MiscUtils
{
	public static final char ID_SEPARATOR=':';
	public static final Integer NULL_ID_PLACEHOLDER=-1;

	private static final String VALID_USERNAME_CHARACTERS = "abcdefghijklmnopqrstuvwxyz1234567890-_.";

	/** This is a shared timer, please use responsibly, if you're a hog, make your own */
	public static final Timer sharedTimer = new Timer("Shared Generic Timer", true);

	/**
	 * This method should only really be called once per context in the context startup listener.	 * @param contextPath
	 */
	protected static void addLoggingOverrideConfiguration(String contextPath)
	{
		String configLocation = System.getProperty("log4j.override.configuration");
		if (configLocation != null)
		{
			if (contextPath != null)
			{
				if (contextPath.length() > 0 && contextPath.charAt(0) == '/') contextPath = contextPath.substring(1);
				if (contextPath.length() > 0 && contextPath.charAt(contextPath.length() - 1) == '/')
					contextPath = contextPath.substring(0, contextPath.length() - 2);
			}

			String resolvedLocation = configLocation.replace("${contextName}", contextPath);
			getLogger().info("loading additional override logging configuration from : "+resolvedLocation);
			DOMConfigurator.configureAndWatch(resolvedLocation);
		}
	}

	/**
	 * This method will return a logger instance which has the name based on the class that's calling this method.
	 */
	public static Logger getLogger()
	{
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		String caller = ste[2].getClassName();
		return(Logger.getLogger(caller));
	}
	
	/**
	 * This method returns the list like (a,b,c) where a,b,c are items in the list[].
	 */
	public static String getAsBracketedList(Object[] list, boolean singleQuoteItems)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');

		for (Object item : list)
		{
			if (sb.length() > 1) sb.append(',');
			if (singleQuoteItems) sb.append('\'');
			sb.append(item.toString());
			if (singleQuoteItems) sb.append('\'');
		}

		sb.append(')');
		return(sb.toString());
	}

	public static String getBuildDateTime()
	{
		return(ConfigXmlUtils.getPropertyString("misc", "build_date_time"));
	}	
	
	/**
	 * This method will check if the username provided is valid or not. If it is not it will throw an IllegalArgumentException with the message set as to why.
	 * It will also normalise the username, i.e. lower case only, trim spaces etc. <br />
	 */
	public static String validateAndNormaliseUserName(String userName)
	{
		userName = StringUtils.trimToNull(userName);

		if (userName == null) throw(new IllegalArgumentException("usernames can not be blank"));
		else userName = userName.toLowerCase();

		if (!StringUtils.containsOnly(userName, VALID_USERNAME_CHARACTERS)) throw(new IllegalArgumentException(
		        "username are only allowed to have letters, numbers, dashes, underscores, and periods : " + userName));

		return(userName);
	}
	
	public static byte[] propertiesToXmlByteArray(Properties p) throws IOException
	{
		ByteArrayOutputStream os=new ByteArrayOutputStream();
        p.storeToXML(os, null);
        return(os.toByteArray());
	}
	
	public static Properties xmlByteArrayToProperties(byte[] b) throws IOException
	{
		Properties p=new Properties();
		
		ByteArrayInputStream is=new ByteArrayInputStream(b);
		p.loadFromXML(is);
		
		return(p);
	}
	
	public static String trimToNullLowerCase(String s)
	{
		s=StringUtils.trimToNull(s);

		if (s!=null) s=s.toLowerCase();
		
		return(s);
	}
	
	/**
	 * filters out any non-alphanumeric characters in a String
	 * @param filterMe
	 */
	public static String alphanumFilter(String filterMe)
	{
		return filterMe.replaceAll("[^a-zA-Z0-9]", "");	
	}
	
	public static String arbitraryFilter(String filterMe, String replaceThese, String withThis)
	{
		
		return filterMe.replaceAll(replaceThese, withThis);
	}
}
