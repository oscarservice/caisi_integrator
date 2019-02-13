package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.caisi_integrator.util.Role;

@Entity
public class CachedDemographicIssue extends AbstractModel<FacilityIdDemographicIssueCompositePk>
{
	@EmbeddedId
	private FacilityIdDemographicIssueCompositePk facilityDemographicIssuePk;

	@Column(nullable=false, length=128)
	private String issueDescription = null;

	@Enumerated(EnumType.STRING)
	@Column(length=64)
	private Role issueRole = null;	
	
	@Column(columnDefinition="tinyint(1)")
	private Boolean acute = null;

	@Column(columnDefinition="tinyint(1)")
	private Boolean certain = null;

	@Column(columnDefinition="tinyint(1)")
	private Boolean major = null;

	@Column(columnDefinition="tinyint(1)")
	private Boolean resolved = null;

	public FacilityIdDemographicIssueCompositePk getFacilityDemographicIssuePk()
    {
    	return facilityDemographicIssuePk;
    }

	public void setFacilityDemographicIssuePk(FacilityIdDemographicIssueCompositePk facilityDemographicIssuePk)
    {
    	this.facilityDemographicIssuePk = facilityDemographicIssuePk;
    }

	public String getIssueDescription()
	{
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription)
	{
		this.issueDescription = StringUtils.trimToNull(issueDescription);
	}

	public Boolean getAcute()
	{
		return acute;
	}

	public void setAcute(Boolean acute)
	{
		this.acute = acute;
	}

	public Role getIssueRole()
	{
		return issueRole;
	}

	/**
	 * This should be one of the set Roles in the Role Enum. It can be set to null if the role does not exist in the RoleEnum, or the Role can be added to the Enum if necessary.
	 */
	public void setIssueRole(Role issueRole)
	{
		this.issueRole = issueRole;
	}

	public Boolean getCertain()
	{
		return certain;
	}

	public void setCertain(Boolean certain)
	{
		this.certain = certain;
	}

	public Boolean getMajor()
	{
		return major;
	}

	public void setMajor(Boolean major)
	{
		this.major = major;
	}

	public Boolean getResolved()
	{
		return resolved;
	}

	public void setResolved(Boolean resolved)
	{
		this.resolved = resolved;
	}

	@Override
    public FacilityIdDemographicIssueCompositePk getId()
    {
	    return facilityDemographicIssuePk;
    }
}
