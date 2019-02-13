package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;

@Entity
public class CachedDemographic extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedDemographic>
{
	/**
	 * Male, female, transgendered, other, undefined, null is unknown/unspecified.
	 */
	public enum Gender
	{
		M, F, T, O, U
	}

	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityDemographicPk;
	private String firstName = null;
	private String lastName = null;
	@Temporal(TemporalType.DATE)
	private Date birthDate = null;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Gender gender = null;

	@Column(length = 32)
	private String hin = null;

	@Column(length = 32)
	private String hinType = null;

	@Column(length = 8)
	private String hinVersion = null;

	@Temporal(TemporalType.DATE)
	private Date hinValidStart = null;

	@Temporal(TemporalType.DATE)
	private Date hinValidEnd = null;

	@Column(length = 32)
	private String sin = null;

	@Column(length = 4)
	private String province = null;

	@Column(length = 128)
	private String city = null;

	@Column(length = 16)
	private String caisiProviderId = null;

	@Column(length = 32)
	private String idHash = null;

	@Column(length = 128)
	private String streetAddress;

	@Column(length = 64)
	private String phone1;

	@Column(length = 64)
	private String phone2;
	
	@Column(length = 16)
	private String lastUpdateUser;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityDemographicPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityDemographicPk)
	{
		this.facilityDemographicPk = facilityDemographicPk;
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

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setGender(Gender gender)
	{
		this.gender = gender;
	}

	public String getHin()
	{
		return hin;
	}

	public void setHin(String hin)
	{
		this.hin = MiscUtils.trimToNullLowerCase(hin);
	}

	public String getHinVersion()
	{
		return hinVersion;
	}

	public void setHinVersion(String hinVersion)
	{
		this.hinVersion = StringUtils.trimToNull(hinVersion);
	}

	public String getSin()
	{
		return sin;
	}

	public void setSin(String sin)
	{
		this.sin = MiscUtils.trimToNullLowerCase(sin);
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = MiscUtils.trimToNullLowerCase(province);
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = MiscUtils.trimToNullLowerCase(city);
	}

	public String getHinType()
	{
		return hinType;
	}

	public void setHinType(String hinType)
	{
		this.hinType = StringUtils.trimToNull(hinType);
	}

	public String getCaisiProviderId()
	{
		return caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId = StringUtils.trimToNull(caisiProviderId);
	}

	public String getIdHash()
	{
		return idHash;
	}

	public void setIdHash(String idHash)
	{
		this.idHash = StringUtils.trimToNull(idHash);
	}

	public String getStreetAddress()
	{
		return(streetAddress);
	}

	public void setStreetAddress(String streetAddress)
	{
		this.streetAddress = streetAddress;
	}

	public String getPhone1()
	{
		return(phone1);
	}

	public void setPhone1(String phone1)
	{
		this.phone1 = phone1;
	}

	public String getPhone2()
	{
		return(phone2);
	}

	public void setPhone2(String phone2)
	{
		this.phone2 = phone2;
	}

	public Date getHinValidStart()
	{
		return(hinValidStart);
	}

	public void setHinValidStart(Date hinValidStart)
	{
		this.hinValidStart = hinValidStart;
	}

	public Date getHinValidEnd()
	{
		return(hinValidEnd);
	}

	public void setHinValidEnd(Date hinValidEnd)
	{
		this.hinValidEnd = hinValidEnd;
	}

	@Override
	public int compareTo(CachedDemographic o)
	{
		return(facilityDemographicPk.getCaisiItemId() - o.facilityDemographicPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityDemographicPk;
	}

	public String getLastUpdateUser() 
	{
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) 
	{
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateDate() 
	{
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) 
	{
		this.lastUpdateDate = lastUpdateDate;
	}
}
