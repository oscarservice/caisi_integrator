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
public class CachedEformData extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedEformData>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityEformDataPk;

	@Column(nullable = false)
	private Integer formId = null;

	@Column(nullable = false)
	@Index
	private Integer caisiDemographicId = null;

	@Column(nullable = false)
	private Boolean status = null;

	@Column(length = 255)
	private String formName = null;

	@Column(length = 255)
	private String formProvider = null;

	@Column(length = 255)
	private String subject = null;

	@Temporal(TemporalType.DATE)
	private Date formDate = null;

	@Temporal(TemporalType.TIME)
	private Date formTime = null;

	@Column(columnDefinition = "text")
	private String formData = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityEformDataPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityEformDataPk)
	{
		this.facilityEformDataPk = facilityEformDataPk;
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

	public Boolean getStatus()
	{
		return this.status;
	}

	public void setStatus(Boolean status)
	{
		this.status = status;
	}

	public String getFormName()
	{
		return this.formName;
	}

	public void setFormName(String formName)
	{
		this.formName = StringUtils.trimToNull(formName);
	}

	public String getFormProvider()
	{
		return this.formProvider;
	}

	public void setFormProvider(String formProvider)
	{
		this.formProvider = StringUtils.trimToNull(formProvider);
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = StringUtils.trimToNull(subject);
	}

	public Date getFormDate()
	{
		return this.formDate;
	}

	public void setFormDate(Date formDate)
	{
		this.formDate = formDate;
	}

	public Date getFormTime()
	{
		return this.formTime;
	}

	public void setFormTime(Date formTime)
	{
		this.formTime = formTime;
	}

	public String getFormData()
	{
		return this.formData;
	}

	public void setFormData(String formData)
	{
		this.formData = StringUtils.trimToNull(formData);
	}

	@Override
	public int compareTo(CachedEformData o)
	{
		return(facilityEformDataPk.getCaisiItemId() - o.facilityEformDataPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityEformDataPk;
	}
}
