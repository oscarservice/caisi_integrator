package org.oscarehr.caisi_integrator.util;

public final class ConsentDomainUtil
{
	public enum CodeGroup
	{
		MENTAL_HEALTH
	}

	/**
	 * @param codeType the type corresponding to the code you want to check.
	 * @param codeGroup the group you're checking to see if the code is in this group.
	 * @param code the code you're checking.
	 * @return true if the code of that codeType is in the codeGroup indicated, false otherwise.
	 */
	public static boolean isInDomain(CodeType codeType, CodeGroup codeGroup, String code)
	{
		switch (codeType)
		{
			case ICD9:
				return(isInDomainIcd9(codeGroup, code));
			case ICD10:
				return(isInDomainIcd10(codeGroup, code));
			case CUSTOM_ISSUE:
				return(isInDomainCustomIssue(codeGroup, code));
			case DRUG:
				return(isInDomainDrugs(codeGroup, code));
			case PREVENTION:
				return(isInDomainPreventions(codeGroup, code));
			default:
				throw(new IllegalStateException("I've missed a case..."));
		}
	}

	/**
	 * @param codeGroup 
	 * @param code  
	 */
	@SuppressWarnings("PMD.UnusedFormalParameter")
	private static boolean isInDomainIcd9(CodeGroup codeGroup, String code)
	{
		// have not received a list from anyone yet
		return(false);
	}

	private static boolean isInDomainIcd10(CodeGroup codeGroup, String code)
	{
		if (codeGroup == CodeGroup.MENTAL_HEALTH)
		{
			return(code.matches("F[0123456789].*"));
		}
		else
		{
			return(false);
		}
	}

	/**
	 * @param codeGroup 
	 * @param code  
	 */
	@SuppressWarnings("PMD.UnusedFormalParameter")
	private static boolean isInDomainDrugs(CodeGroup codeGroup, String code)
	{
		// have not received a list from anyone yet
		return(false);
	}

	/**
	 * @param codeGroup 
	 * @param code  
	 */
	@SuppressWarnings("PMD.UnusedFormalParameter")
	private static boolean isInDomainPreventions(CodeGroup codeGroup, String code)
	{
		// have not received a list from anyone yet
		return(false);
	}

	/**
	 * @param codeGroup 
	 * @param code  
	 */
	@SuppressWarnings("PMD.UnusedFormalParameter")
	private static boolean isInDomainCustomIssue(CodeGroup codeGroup, String code)
	{
		// have not received a list from anyone yet
		return(false);
	}
	
	
}
