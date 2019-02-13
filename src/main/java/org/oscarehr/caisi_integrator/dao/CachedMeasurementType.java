package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedMeasurementType extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedMeasurementType>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityMeasurementTypePk;

	@Column(nullable = false, length = 4)
	@Index
	private String type = null;

	@Column(nullable = false, length = 255)
	private String typeDescription = null;

	@Column(nullable = false, length = 255)
	private String measuringInstruction = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityMeasurementTypePk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityMeasurementTypePk)
	{
		this.facilityMeasurementTypePk = facilityMeasurementTypePk;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = StringUtils.trimToEmpty(type);
	}

	public String getTypeDescription()
	{
		return this.typeDescription;
	}

	public void setTypeDescription(String typeDescription)
	{
		this.typeDescription = StringUtils.trimToEmpty(typeDescription);
	}

	public String getMeasuringInstruction()
	{
		return this.measuringInstruction;
	}

	public void setMeasuringInstruction(String measuringInstruction)
	{
		this.measuringInstruction = StringUtils.trimToEmpty(measuringInstruction);
	}

	@Override
	public int compareTo(CachedMeasurementType o)
	{
		return(facilityMeasurementTypePk.getCaisiItemId() - o.facilityMeasurementTypePk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityMeasurementTypePk;
	}
}
