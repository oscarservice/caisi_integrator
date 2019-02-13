package org.oscarehr.caisi_integrator.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;
import org.oscarehr.caisi_integrator.util.MiscUtils;

/**
 * We can't use the standard String composite key because this key is absurdly large
 */
@Embeddable
public class FacilityIdLabResultCompositePk implements Serializable
{
	/** this is the integrator facility id */
	@Index
	private Integer integratorFacilityId = null;
	@Column(length=64)
	@Index
	private String labResultId = null;

	public FacilityIdLabResultCompositePk()
	{
		// do nothing
	}
	
	public FacilityIdLabResultCompositePk(Integer integratorFacilityId, String labResultId)
	{
		this.integratorFacilityId = integratorFacilityId;
		this.labResultId = labResultId;
	}

	public Integer getIntegratorFacilityId()
	{
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId)
	{
		this.integratorFacilityId = integratorFacilityId;
	}

	public String getLabResultId()
	{
		return labResultId;
	}

	public void setLabResultId(String labResultId)
	{
		this.labResultId = StringUtils.trimToNull(labResultId);
	}

	@Override
	public String toString()
	{
		return("" + integratorFacilityId + MiscUtils.ID_SEPARATOR + labResultId);
	}

	@Override
	public int hashCode()
	{
		return(labResultId.hashCode());
	}

	@Override
    public boolean equals(Object o)
	{
		try
        {
			FacilityIdLabResultCompositePk o1=(FacilityIdLabResultCompositePk)o;
	        return(integratorFacilityId.equals(o1.integratorFacilityId) && labResultId.equals(o1.labResultId));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

}