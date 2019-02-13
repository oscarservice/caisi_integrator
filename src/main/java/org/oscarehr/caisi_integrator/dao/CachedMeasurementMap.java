package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedMeasurementMap extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedMeasurementMap>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityMeasurementMapPk;

	@Column(length = 20)
	@Index
	private String loincCode = null;

	@Column(nullable = false, length = 20)
	@Index
	private String identCode = null;

	@Column(nullable = false, length = 255)
	private String name = null;

	@Column(nullable = false, length = 10)
	private String labType = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityMeasurementMapPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityMeasurementMapPk)
	{
		this.facilityMeasurementMapPk = facilityMeasurementMapPk;
	}

	public String getLoincCode()
	{
		return this.loincCode;
	}

	public void setLoincCode(String loincCode)
	{
		this.loincCode = StringUtils.trimToEmpty(loincCode);
	}

	public String getIdentCode()
	{
		return this.identCode;
	}

	public void setIdentCode(String identCode)
	{
		this.identCode = StringUtils.trimToEmpty(identCode);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = StringUtils.trimToEmpty(name);
	}

	public String getLabType()
	{
		return this.labType;
	}

	public void setLabType(String labType)
	{
		this.labType = StringUtils.trimToEmpty(labType);
	}

	@Override
	public int compareTo(CachedMeasurementMap o)
	{
		return(facilityMeasurementMapPk.getCaisiItemId() - o.facilityMeasurementMapPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityMeasurementMapPk;
	}
}
