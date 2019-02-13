package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedDemographicPrevention extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityPreventionPk = null;

	@Column(nullable = false)
	@Index
	private Integer caisiDemographicId = null;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date preventionDate = null;

	@Column(nullable = false, length = 16)
	private String caisiProviderId = null;

	@Column(nullable = false, length = 32)
	private String preventionType = null;

	@Temporal(TemporalType.DATE)
	private Date nextDate = null;

	@Column(columnDefinition = "tinyint(1)")
	private boolean refused = false;

	@Column(columnDefinition = "tinyint(1)")
	private boolean never = false;

	@Column(columnDefinition = "mediumblob")
	private String attributes = null;

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityPreventionPk;
	}

	public FacilityIdIntegerCompositePk getFacilityPreventionPk()
	{
		return facilityPreventionPk;
	}

	public void setFacilityPreventionPk(FacilityIdIntegerCompositePk facilityPreventionPk)
	{
		this.facilityPreventionPk = facilityPreventionPk;
	}

	public Integer getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public Date getPreventionDate()
	{
		return preventionDate;
	}

	public void setPreventionDate(Date preventionDate)
	{
		this.preventionDate = preventionDate;
	}

	public String getCaisiProviderId()
	{
		return caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId = caisiProviderId;
	}

	public String getPreventionType()
	{
		return preventionType;
	}

	public void setPreventionType(String preventionType)
	{
		this.preventionType = preventionType;
	}

	public Date getNextDate()
	{
		return nextDate;
	}

	public void setNextDate(Date nextDate)
	{
		this.nextDate = nextDate;
	}

	public String getAttributes()
	{
		return(attributes);
	}

	public void setAttributes(String attributes)
	{
		this.attributes = attributes;
	}

	public boolean isRefused()
	{
		return(refused);
	}

	public void setRefused(boolean refused)
	{
		this.refused = refused;
	}

	public boolean isNever()
	{
		return(never);
	}

	public void setNever(boolean never)
	{
		this.never = never;
	}

}
