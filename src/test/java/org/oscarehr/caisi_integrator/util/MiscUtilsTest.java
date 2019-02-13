package org.oscarehr.caisi_integrator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;

public class MiscUtilsTest
{
	@Test
	public void getLoggerTest()
	{
		Logger logger = MiscUtils.getLogger();
		assertEquals(MiscUtilsTest.class.getName(), logger.getName());
	}

	@Test
	public void getAsBracketedListTest()
	{
		String[] list =
		{
		        "a", "b", "c"
		};

		String result = MiscUtils.getAsBracketedList(list, false);
		assertEquals("(a,b,c)", result);

		result = MiscUtils.getAsBracketedList(list, true);
		assertEquals("('a','b','c')", result);
	}

	@Test
	public void validateAndNormaliseUserName()
	{
		String userName = "*&#$*&# *&$";
		try
		{
			MiscUtils.validateAndNormaliseUserName(userName);
			fail("invalid characters should have been caught");
		}
		catch (Exception e)
		{
			// this is the expected case
		}

		userName = null;
		try
		{
			MiscUtils.validateAndNormaliseUserName(userName);
			fail("null should have been caught");
		}
		catch (Exception e)
		{
			// this is the expected case
		}

		userName = "";
		try
		{
			MiscUtils.validateAndNormaliseUserName(userName);
			fail("blank should have been caught");
		}
		catch (Exception e)
		{
			// this is the expected case
		}

		userName = " bILl ";
		String userName1 = MiscUtils.validateAndNormaliseUserName(userName);
		assertEquals("bill", userName1);
	}

	@Test
	public void alphanumFilterTest()
	{
		String test="(a@bcd-ABC<D.1234)+#";
		String filtered = MiscUtils.alphanumFilter(test);
		assertEquals("abcdABCD1234", filtered);
	}
	
	@Test
	public void arbitraryFilterTest()
	{
		String filterMe = "acb^~\\&|123ABC'-";
		String filtered = MiscUtils.arbitraryFilter(filterMe, "[~\\\\&|^]", "");
		assertEquals("acb123ABC'-", filtered);
	}
}
