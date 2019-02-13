package org.oscarehr.caisi_integrator.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;
import org.oscarehr.caisi_integrator.util.MiscUtils;

@Embeddable
public class FacilityIdStringCompositePk implements Serializable
{
	/** this is the integrator facility id */
	@Index
	private Integer integratorFacilityId = null;
	/** this is the oscar/caisi id */
	@Column(length=16)
	@Index
	private String caisiItemId = null;

	public FacilityIdStringCompositePk()
	{
		// do nothing
	}
	
	public FacilityIdStringCompositePk(Integer integratorFacilityId, String caisiItemId)
	{
		this.integratorFacilityId = integratorFacilityId;
		this.caisiItemId = caisiItemId;
	}

	public Integer getIntegratorFacilityId()
	{
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId)
	{
		this.integratorFacilityId = integratorFacilityId;
	}

	public String getCaisiItemId()
	{
		return caisiItemId;
	}

	public void setCaisiItemId(String caisiItemId)
	{
		this.caisiItemId = StringUtils.trimToNull(caisiItemId);
	}

	@Override
	public String toString()
	{
		return("" + integratorFacilityId + MiscUtils.ID_SEPARATOR + caisiItemId);
	}

	@Override
	public int hashCode()
	{
		return(caisiItemId.hashCode());
	}

	@Override
    public boolean equals(Object o)
	{
		try
        {
			FacilityIdStringCompositePk o1=(FacilityIdStringCompositePk)o;
	        return(integratorFacilityId.equals(o1.integratorFacilityId) && caisiItemId.equals(o1.caisiItemId));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

}