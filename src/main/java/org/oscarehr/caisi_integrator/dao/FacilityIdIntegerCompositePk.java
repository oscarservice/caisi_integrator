package org.oscarehr.caisi_integrator.dao;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.openjpa.persistence.jdbc.Index;
import org.oscarehr.caisi_integrator.util.MiscUtils;

@Embeddable
public class FacilityIdIntegerCompositePk implements Serializable, Comparable<FacilityIdIntegerCompositePk>
{
	/** this is the integrators facility id */
	@Index
	private Integer integratorFacilityId = null;

	/** this is the id of the item in oscar/caisi (not integrator) system */
	@Index
	private Integer caisiItemId = null;
	
	public FacilityIdIntegerCompositePk()
	{
		// empty constructor
	}

	public FacilityIdIntegerCompositePk(Integer integratorFacilityId, Integer caisiItemId)
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

	public Integer getCaisiItemId()
    {
    	return caisiItemId;
    }

	public void setCaisiItemId(Integer caisiItemId)
    {
    	this.caisiItemId = caisiItemId;
    }

	@Override
	public String toString()
	{
		return(String.valueOf(integratorFacilityId) + MiscUtils.ID_SEPARATOR + caisiItemId);
	}

	@Override
	public int hashCode()
	{
		return(caisiItemId);
	}

	@Override
    public boolean equals(Object o)
	{
		try
        {
			FacilityIdIntegerCompositePk o1=(FacilityIdIntegerCompositePk)o;
	        return(integratorFacilityId.equals(o1.integratorFacilityId) && caisiItemId.equals(o1.caisiItemId));
        }
        catch (RuntimeException e)
        {
	        return(false);
        }
	}

	@Override
	public int compareTo(FacilityIdIntegerCompositePk o)
	{
		return(toString().compareTo(o.toString()));
	}

}