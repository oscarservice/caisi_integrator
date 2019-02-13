package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
public class CachedFacility extends AbstractModel<Integer>
{
	@Id
	private Integer integratorFacilityId = null;

	@Column(length=64)
	private String name = null;

	@Column(length=255)
	private String description = null;

	@Column(length=64)
	private String contactName = null;

	@Column(length=64)
	private String contactEmail = null;

	@Column(length=64)
	private String contactPhone = null;
	
	@Column(nullable=false, columnDefinition="tinyint(1)")
	private boolean hic=false;

	@Column(nullable=false, columnDefinition="tinyint(1)")
    private boolean enableIntegratedReferrals=true;

	@Column(nullable=false, columnDefinition="tinyint(1)")
    private boolean allowSims = true;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastDataUpdate = null;

	public Integer getIntegratorFacilityId()
	{
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId)
	{
		this.integratorFacilityId = integratorFacilityId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = StringUtils.trimToNull(name);
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = StringUtils.trimToNull(description);
	}

	public String getContactName()
	{
		return contactName;
	}

	public void setContactName(String contactName)
	{
		this.contactName = StringUtils.trimToNull(contactName);
	}

	public String getContactEmail()
	{
		return contactEmail;
	}

	public void setContactEmail(String contactEmail)
	{
		this.contactEmail = StringUtils.trimToNull(contactEmail);
	}

	public String getContactPhone()
	{
		return contactPhone;
	}

	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = StringUtils.trimToNull(contactPhone);
	}

	/**
	 * This date represents the last time the facility send the integrator data, not when this object was last updated.
	 */
	public Date getLastDataUpdate()
    {
    	return lastDataUpdate;
    }

	public void setLastDataUpdate(Date lastDataUpdate)
    {
    	this.lastDataUpdate = lastDataUpdate;
    }	
	
	public boolean isHic()
    {
    	return hic;
    }

	public void setHic(boolean hic)
    {
    	this.hic = hic;
    }

	
	public boolean isEnableIntegratedReferrals()
    {
    	return enableIntegratedReferrals;
    }

	public boolean isAllowSims() 
	{
		return allowSims;
	}

	public void setAllowSims(boolean allowSims) 
	{
		this.allowSims = allowSims;
	}

	public void setEnableIntegratedReferrals(boolean enableIntegratedReferrals)
    {
    	this.enableIntegratedReferrals = enableIntegratedReferrals;
    }

	@Override
	public Integer getId()
	{
		return(integratorFacilityId);
	}
}
