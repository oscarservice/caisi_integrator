package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedDxresearch extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedDxresearch>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityDxresearchPk;

	@Column(nullable = false)
	@Index
	private Integer caisiDemographicId = null;

	@Temporal(TemporalType.DATE)
	private Date startDate = null;

	@Temporal(TemporalType.DATE)
	private Date updateDate = null;

	@Column(length = 1)
	private String status = null;

	@Column(length = 10)
	private String dxresearchCode = null;

	@Column(length = 20)
	private String codingSystem = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityDxresearchPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityDxresearchPk)
	{
		this.facilityDxresearchPk = facilityDxresearchPk;
	}

	public Integer getCaisiDemographicId()
	{
		return this.caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public Date getStartDate()
	{
		return this.startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getUpdateDate()
	{
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = StringUtils.trimToNull(status);
	}

	public String getDxresearchCode()
	{
		return this.dxresearchCode;
	}

	public void setDxresearchCode(String dxresearchCode)
	{
		this.dxresearchCode = StringUtils.trimToNull(dxresearchCode);
	}

	public String getCodingSystem()
	{
		return this.codingSystem;
	}

	public void setCodingSystem(String codingSystem)
	{
		this.codingSystem = StringUtils.trimToNull(codingSystem);
	}

	@Override
	public int compareTo(CachedDxresearch o)
	{
		return(facilityDxresearchPk.getCaisiItemId() - o.facilityDxresearchPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityDxresearchPk;
	}
}
