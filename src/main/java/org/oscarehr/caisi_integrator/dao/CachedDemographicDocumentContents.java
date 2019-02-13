package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CachedDemographicDocumentContents extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIntegerCompositePk;

	@Column(columnDefinition = "longblob")
	private byte[] fileContents;

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIntegerCompositePk;
	}

	public FacilityIdIntegerCompositePk getFacilityIntegerCompositePk()
	{
		return(facilityIntegerCompositePk);
	}

	public void setFacilityIntegerCompositePk(FacilityIdIntegerCompositePk facilityIntegerCompositePk)
	{
		this.facilityIntegerCompositePk = facilityIntegerCompositePk;
	}

	public byte[] getFileContents()
	{
		return fileContents;
	}

	public void setFileContents(byte[] fileContents)
	{
		this.fileContents = fileContents;
	}
}