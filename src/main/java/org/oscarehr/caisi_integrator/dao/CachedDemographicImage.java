package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.caisi_integrator.util.ImageIoUtils;

@Entity
public class CachedDemographicImage extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityDemographicPk;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate=null;
	@Column(columnDefinition="mediumblob")
	private byte[] image = null;	
	
	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
    {
    	return facilityDemographicPk;
    }

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityDemographicPk)
    {
    	this.facilityDemographicPk = facilityDemographicPk;
    }

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate=updateDate;
	}

	public byte[] getImage()
	{
		return image;
	}

	public void setImage(byte[] original)
	{
		this.image = ImageIoUtils.scaleJpgSmallerProportionally(original, 200, 200, .90f);
	}

	@Override
    public FacilityIdIntegerCompositePk getId()
    {
	    return facilityDemographicPk;
    }
}
