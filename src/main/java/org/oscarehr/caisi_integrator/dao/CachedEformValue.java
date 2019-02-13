package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedEformValue extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedEformValue>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityEformValuesPk;

	@Column(nullable = false)
	private Integer formDataId = null;

	private Integer formId = null;
	@Index
	private Integer caisiDemographicId = null;

	@Column(nullable = false, length = 30)
	private String varName = null;

	@Column(columnDefinition = "text")
	private String varValue = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityEformValuesPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityEformValuesPk)
	{
		this.facilityEformValuesPk = facilityEformValuesPk;
	}

	public Integer getFormDataId()
	{
		return this.formDataId;
	}

	public void setFormDataId(Integer formDataId)
	{
		this.formDataId = formDataId;
	}

	public Integer getFormId()
	{
		return this.formId;
	}

	public void setFormId(Integer formId)
	{
		this.formId = formId;
	}

	public Integer getCaisiDemographicId()
	{
		return this.caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getVarName()
	{
		return this.varName;
	}

	public void setVarName(String varName)
	{
		this.varName = StringUtils.trimToEmpty(varName);
	}

	public String getVarValue()
	{
		return this.varValue;
	}

	public void setVarValue(String varValue)
	{
		this.varValue = StringUtils.trimToNull(varValue);
	}

	@Override
	public int compareTo(CachedEformValue o)
	{
		return(facilityEformValuesPk.getCaisiItemId() - o.facilityEformValuesPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityEformValuesPk;
	}
}
