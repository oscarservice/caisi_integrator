package org.oscarehr.caisi_integrator.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Embeddable;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.MiscUtils;

@Embeddable
public class NoteIssue implements Serializable, Comparable<NoteIssue>
{
	private static final long serialVersionUID = 1130555404818346588L;

	private static final Logger logger=MiscUtils.getLogger();
	
	private CodeType codeType=null;

	private String issueCode = null;
	
	public NoteIssue()
	{		
		// empty constructor
	}
	
	private NoteIssue(CodeType codeType, String issueCode)
	{		
		this.codeType=codeType;
		this.issueCode=issueCode;
	}
	
	public CodeType getCodeType()
	{
		return codeType;
	}

	public void setCodeType(CodeType codeType)
	{
		this.codeType = codeType;
	}

	public String getIssueCode()
	{
		return issueCode;
	}

	public void setIssueCode(String issueCode)
	{
		this.issueCode = issueCode;
	}

	public static HashSet<String> toStrings(HashSet<NoteIssue> issues)
	{
		HashSet<String> tmp = new HashSet<String>();
		for (NoteIssue issue : issues)
		{
			tmp.add(issue.toString());
		}
		
		return(tmp);
	}

	public static HashSet<NoteIssue> fromStrings(List<String> strings)
	{
		HashSet<NoteIssue> tmp = new HashSet<NoteIssue>();
		for (String s : strings)
		{
			try
			{
				tmp.add(valueOf(s));
			}
			catch (Exception e)
			{
				logger.error("Unexpected error.", e);
			}
		}
		return(tmp);
	}
	
	public static NoteIssue valueOf(String s)
	{
		String[] tempSplit=s.split("\\.");
		if (tempSplit.length==2)
		{
			return(new NoteIssue(CodeType.valueOf(tempSplit[0]), tempSplit[1]));
		}
		else
		{
			throw(new IllegalArgumentException(s+", split="+tempSplit));
		}
	}
	
	@Override
	public String toString()
	{
		return(codeType.name()+'.'+issueCode);
	}

	@Override
	public int hashCode()
	{
		return(issueCode.hashCode());
	}

	@Override
    public boolean equals(Object o)
	{
		try
        {
			NoteIssue o1=(NoteIssue)o;
	        return(codeType.equals(o1.codeType) && issueCode.equals(o1.issueCode));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

	@Override
	public int compareTo(NoteIssue o)
	{
		return(toString().compareTo(o.toString()));
	}
}