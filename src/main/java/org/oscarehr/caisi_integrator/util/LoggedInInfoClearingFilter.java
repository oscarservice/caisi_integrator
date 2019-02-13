/*
* Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
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
* CAISI, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.caisi_integrator.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoggedInInfoClearingFilter implements javax.servlet.Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// nothing
	}

	@Override
	public void doFilter(ServletRequest tmpRequest, ServletResponse tmpResponse, FilterChain chain) throws IOException, ServletException
	{
		try
		{
			// check to see if for some reason some one left one lingering
			LoggedInInfo.checkForLingeringData();
			
			chain.doFilter(tmpRequest, tmpResponse);
		}
		finally
		{
			LoggedInInfo.loggedInInfo.remove();
		}
	}

	@Override
	public void destroy()
	{
		// can't think of anything to do right now.
	}
}
