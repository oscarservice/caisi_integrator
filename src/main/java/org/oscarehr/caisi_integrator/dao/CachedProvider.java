package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

@Entity
public class CachedProvider extends AbstractModel<FacilityIdStringCompositePk> implements Comparable<CachedProvider>
{
	@EmbeddedId
	private FacilityIdStringCompositePk facilityIdStringCompositePk;

	@Column(length=64)
	private String firstName = null;

	@Column(length=64)
	private String lastName = null;

	@Column(length=64)
	private String specialty = null;

	@Column(length=64)
	private String workPhone = null;

	/**
	 * The same as get/set Id but needs to be here so web services wires it properly.
	 */
	public FacilityIdStringCompositePk getFacilityIdStringCompositePk()
	{
		return facilityIdStringCompositePk;
	}

	public void setFacilityIdStringCompositePk(FacilityIdStringCompositePk facilityIdStringCompositePk)
	{
		this.facilityIdStringCompositePk = facilityIdStringCompositePk;
	}

	@Override
	public int compareTo(CachedProvider o)
	{
		return(facilityIdStringCompositePk.getCaisiItemId().compareTo(o.facilityIdStringCompositePk.getCaisiItemId()));
	}

	@Override
	public FacilityIdStringCompositePk getId()
	{
		return facilityIdStringCompositePk;
	}

	public String getFirstName()
    {
    	return firstName;
    }

	public void setFirstName(String firstName)
    {
    	this.firstName = StringUtils.trimToNull(firstName);
    }

	public String getLastName()
    {
    	return lastName;
    }

	public void setLastName(String lastName)
    {
    	this.lastName = StringUtils.trimToNull(lastName);
    }

	public String getSpecialty()
    {
    	return specialty;
    }

	public void setSpecialty(String specialty)
    {
    	this.specialty = StringUtils.trimToNull(specialty);
    }

	public String getWorkPhone()
    {
    	return workPhone;
    }

	public void setWorkPhone(String workPhone)
    {
    	this.workPhone = StringUtils.trimToNull(workPhone);
    }
}
