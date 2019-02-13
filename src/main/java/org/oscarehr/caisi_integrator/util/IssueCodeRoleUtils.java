package org.oscarehr.caisi_integrator.util;

public final class IssueCodeRoleUtils
{
	public static Role getRole(CodeType codeType, String issueCode)
	{
		if (issueCode.startsWith("CTCMM")) return(Role.COUNSELLOR);
		if (codeType==CodeType.ICD10 || codeType==CodeType.ICD9) return(Role.DOCTOR);
		else return(null);
	}
}
