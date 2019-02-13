package org.oscarehr.caisi_integrator.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.openjpa.persistence.jdbc.Index;
import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.MiscUtils;

@SuppressWarnings("serial")
@Embeddable
public class FacilityIdDemographicIssueCompositePk implements Serializable
{
	/** this is the integrator facility id */
	@Index
	private Integer integratorFacilityId = null;
	/** this is the oscar/caisi demographic id */
	@Index
	private Integer caisiDemographicId = null;

	@Enumerated(EnumType.STRING)
	@Column(length=64, nullable=false)
	private CodeType codeType=null;
	
	@Column(length=64, nullable=false)
	private String issueCode=null;

	public Integer getIntegratorFacilityId()
	{
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId)
	{
		this.integratorFacilityId = integratorFacilityId;
	}

	public Integer getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getIssueCode()
    {
    	return issueCode;
    }

	public void setIssueCode(String issueCode)
    {
    	this.issueCode = issueCode;
    }

	public CodeType getCodeType()
	{
		return codeType;
	}

	public void setCodeType(CodeType codeType)
	{
		this.codeType = codeType;
	}

	@Override
	public String toString()
	{
		return("" + integratorFacilityId + MiscUtils.ID_SEPARATOR + caisiDemographicId+MiscUtils.ID_SEPARATOR+codeType+MiscUtils.ID_SEPARATOR+issueCode);
	}

	@Override
	public int hashCode()
	{
		return(caisiDemographicId);
	}

	@Override
    public boolean equals(Object o)
	{
		try
        {
	        FacilityIdDemographicIssueCompositePk o1=(FacilityIdDemographicIssueCompositePk)o;
	        return((integratorFacilityId.equals(o1.integratorFacilityId)) && (caisiDemographicId.equals(o1.caisiDemographicId)) && issueCode.equals(o1.issueCode) && codeType==o1.codeType);
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

}