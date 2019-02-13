package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedDemographicLabResult extends AbstractModel<FacilityIdLabResultCompositePk>
{
	@EmbeddedId
	private FacilityIdLabResultCompositePk facilityIdLabResultCompositePk;

	@Column(nullable = false)
	@Index
	private int caisiDemographicId = 0;

	@Column(length = 64)
	private String type;

	@Column(columnDefinition = "mediumblob")
	private String data;

	@Override
	public FacilityIdLabResultCompositePk getId()
	{
		return facilityIdLabResultCompositePk;
	}


	public FacilityIdLabResultCompositePk getFacilityIdLabResultCompositePk()
	{
		return(facilityIdLabResultCompositePk);
	}


	public void setFacilityIdLabResultCompositePk(FacilityIdLabResultCompositePk facilityIdLabResultCompositePk)
	{
		this.facilityIdLabResultCompositePk = facilityIdLabResultCompositePk;
	}


	public int getCaisiDemographicId()
	{
		return(caisiDemographicId);
	}

	public void setCaisiDemographicId(int caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getType()
	{
		return(type);
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getData()
	{
		return(data);
	}

	public void setData(String data)
	{
		this.data = data;
	}

}