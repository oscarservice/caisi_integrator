package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedDemographicForm extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;

	@Column(nullable=false, length=16)
	private String caisiProviderId=null;

	@Column(nullable=false)
	@Index
	private Integer caisiDemographicId=null;
	
	@Temporal(TemporalType.DATE)
	private Date editDate=null;
	
	@Column(nullable=false, length=128)
	private String formName;
	
	@Column(columnDefinition = "mediumblob")
	private String formData;

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIdIntegerCompositePk;
	}

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityIdIntegerCompositePk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityIdIntegerCompositePk)
	{
		this.facilityIdIntegerCompositePk=facilityIdIntegerCompositePk;
	}

	public String getCaisiProviderId()
	{
		return caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId=caisiProviderId;
	}

	public Integer getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId=caisiDemographicId;
	}

	public Date getEditDate()
	{
		return(editDate);
	}

	public void setEditDate(Date editDate)
	{
		this.editDate = editDate;
	}

	public String getFormData()
	{
		return(formData);
	}

	public void setFormData(String formData)
	{
		this.formData = formData;
	}

	public String getFormName()
	{
		return(formName);
	}

	public void setFormName(String formName)
	{
		this.formName = formName;
	}
	
	
}
