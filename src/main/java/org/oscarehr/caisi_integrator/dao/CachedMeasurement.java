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
public class CachedMeasurement extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedMeasurement>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityMeasurementPk;

	@Column(nullable = false, length = 50)
	private String type = null;

	@Column(nullable = false)
	@Index
	private Integer caisiDemographicId = null;

	@Column(nullable = false, length = 16)
	private String caisiProviderId = null;

	private String dataField = null;

	private String measuringInstruction = null;

	private String comments = null;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateObserved = null;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntered = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityMeasurementPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityMeasurementPk)
	{
		this.facilityMeasurementPk = facilityMeasurementPk;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = StringUtils.trimToEmpty(type);
	}

	public Integer getCaisiDemographicId()
	{
		return this.caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getCaisiProviderId()
	{
		return this.caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId = StringUtils.trimToEmpty(caisiProviderId);
	}

	public String getDataField()
	{
		return this.dataField;
	}

	public void setDataField(String dataField)
	{
		this.dataField = StringUtils.trimToEmpty(dataField);
	}

	public String getMeasuringInstruction()
	{
		return this.measuringInstruction;
	}

	public void setMeasuringInstruction(String measuringInstruction)
	{
		this.measuringInstruction = StringUtils.trimToEmpty(measuringInstruction);
	}

	public String getComments()
	{
		return this.comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public Date getDateObserved()
	{
		return this.dateObserved;
	}

	public void setDateObserved(Date dateObserved)
	{
		this.dateObserved = dateObserved;
	}

	public Date getDateEntered()
	{
		return this.dateEntered;
	}

	public void setDateEntered(Date dateEntered)
	{
		this.dateEntered = dateEntered;
	}

	@Override
	public int compareTo(CachedMeasurement o)
	{
		return(facilityMeasurementPk.getCaisiItemId() - o.facilityMeasurementPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityMeasurementPk;
	}
}
