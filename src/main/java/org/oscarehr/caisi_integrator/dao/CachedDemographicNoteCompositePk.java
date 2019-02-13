package org.oscarehr.caisi_integrator.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;
import org.oscarehr.caisi_integrator.util.MiscUtils;

@Embeddable
public class CachedDemographicNoteCompositePk implements Serializable
{
	/** this is the integrator facility id */
	@Index
	private Integer integratorFacilityId = null;
	/** this is the oscar/caisi uuid */
	@Column(length = 50)
	@Index
	private String uuid = null;

	public CachedDemographicNoteCompositePk()
	{

	}

	public CachedDemographicNoteCompositePk(Integer integratorFacilityId, String uuid)
	{
		this.integratorFacilityId = integratorFacilityId;
		this.uuid = uuid;
	}

	public Integer getIntegratorFacilityId()
	{
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId)
	{
		this.integratorFacilityId = integratorFacilityId;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = StringUtils.trimToNull(uuid);
	}

	@Override
	public String toString()
	{
		return("" + integratorFacilityId + MiscUtils.ID_SEPARATOR + uuid);
	}

	@Override
	public int hashCode()
	{
		return(uuid.hashCode());
	}

	@Override
	public boolean equals(Object o)
	{
		try
		{
			CachedDemographicNoteCompositePk o1 = (CachedDemographicNoteCompositePk) o;
			return(integratorFacilityId.equals(o1.integratorFacilityId) && uuid.equals(o1.uuid));
		}
		catch (RuntimeException e)
		{
			return(false);
		}
	}

}