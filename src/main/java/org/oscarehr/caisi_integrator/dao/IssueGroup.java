package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.Named;

@Entity
public class IssueGroup extends AbstractModel<Integer> implements Named
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(nullable = false, length = 32)
	private String name = null;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CodeType codeType = null;

	@Column(length = 64, nullable = false)
	private String issueCode = null;

	@Override
	public Integer getId()
	{
		return(id);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = MiscUtils.validateAndNormaliseUserName(name);
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

}
