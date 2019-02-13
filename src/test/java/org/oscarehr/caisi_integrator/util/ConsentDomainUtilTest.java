package org.oscarehr.caisi_integrator.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.ConsentDomainUtil.CodeGroup;

public class ConsentDomainUtilTest
{
	@Test
	public void testDomains()
	{
		// check icd10 mental health 
		assertFalse(ConsentDomainUtil.isInDomain(CodeType.ICD10, CodeGroup.MENTAL_HEALTH, "A0001"));
		assertTrue(ConsentDomainUtil.isInDomain(CodeType.ICD10, CodeGroup.MENTAL_HEALTH, "F0001"));
	}
}
