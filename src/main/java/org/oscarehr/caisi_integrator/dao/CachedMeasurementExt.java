package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedMeasurementExt extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedMeasurementExt>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityMeasurementExtPk;

	@Column(nullable = false)
	@Index
	private Integer measurementId = null;

	@Column(nullable = false, length = 20)
	private String keyval = null;

	@Column(nullable = false, columnDefinition = "text")
	private String val = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityMeasurementExtPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityMeasurementExtPk)
	{
		this.facilityMeasurementExtPk = facilityMeasurementExtPk;
	}

	public Integer getMeasurementId()
	{
		return this.measurementId;
	}

	public void setMeasurementId(Integer measurementId)
	{
		this.measurementId = measurementId;
	}

	public String getKeyval()
	{
		return this.keyval;
	}

	public void setKeyval(String keyval)
	{
		this.keyval = StringUtils.trimToEmpty(keyval);
	}

	public String getVal()
	{
		return this.val;
	}

	public void setVal(String val)
	{
		this.val = StringUtils.trimToEmpty(val);
	}

	@Override
	public int compareTo(CachedMeasurementExt o)
	{
		return(facilityMeasurementExtPk.getCaisiItemId() - o.facilityMeasurementExtPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityMeasurementExtPk;
	}
}
