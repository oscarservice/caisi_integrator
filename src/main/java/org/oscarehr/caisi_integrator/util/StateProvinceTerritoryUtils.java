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

public class StateProvinceTerritoryUtils
{
	public static String[] getRegions(String countryCode)
	{
		if ("AU".equals(countryCode)) return(AU_REGIONS);
		if ("CA".equals(countryCode)) return(CA_REGIONS);
		if ("US".equals(countryCode)) return(US_REGIONS);
		else return(new String[0]);
	}
	
	public static final String[] AU_REGIONS=
	{
        "ACT",
        "JBT",
        "NSW",
        "NT",
        "QLD",
        "SA",
        "TAS",
        "VIC",
        "WA"
	};
	
	public static final String[] CA_REGIONS=
	{
        "AB",
        "BC",
        "MB",
        "NB",
        "NL",
        "NS",
        "NT",
        "NU",
        "ON",
        "PE",
        "QC",
        "SK",
        "YT"
	};
	
	public static final String[] US_REGIONS=
	{
		"AK",
		"AL",
		"AR",
		"AZ",
		"CA",
		"CO",
		"CT",
		"DE",
		"FL",
		"GA",
		"HI",
		"IA",
		"ID",
		"IL",
		"IN",
		"KS",
		"KY",
		"LA",
		"MA",
		"MD",
		"ME",
		"MI",
		"MN",
		"MO",
		"MS",
		"MT",
		"NC",
		"ND",
		"NE",
		"NH",
		"NJ",
		"NM",
		"NV",
		"NY",
		"OH",
		"OK",
		"OR",
		"PA",
		"RI",
		"SC",
		"SD",
		"TN",
		"TX",
		"UT",
		"VA",
		"VT",
		"WA",
		"WI",
		"WV",
		"WY"
	};
}