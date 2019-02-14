package org.oscarehr.caisi_integrator.ws.transfer;

import java.io.Serializable;
import java.util.Date;

import org.oscarehr.caisi_integrator.dao.CachedDemographic.Gender;

public class DemographicTransfer implements Serializable
{
	private static final long serialVersionUID = 570194986641348591L;

	private Integer integratorFacilityId = null;
	private int caisiDemographicId = 0;
	private String caisiProviderId = null;
	private String firstName = null;
	private String lastName = null;
	private Date birthDate = null;
	private Gender gender = null;
	private String hin = null;
	private String hinType = null;
	private String hinVersion = null;
	private Date hinValidStart = null;
	private Date hinValidEnd = null;
	private String sin = null;
	private String province = null;
	private String city = null;
	private String streetAddress;
	private String phone1;
	private String phone2;
	private String lastUpdateUser;
	private Date lastUpdateDate;

	private Date photoUpdateDate = null;
	private byte[] photo = null;
	private boolean removeId = false;

	public Integer getIntegratorFacilityId()
	{
		return integratorFacilityId;
	}

	public void setIntegratorFacilityId(Integer integratorFacilityId)
	{
		this.integratorFacilityId = integratorFacilityId;
	}

	public int getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(int caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getCaisiProviderId()
	{
		return caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId = caisiProviderId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
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
		this.hin = hin;
	}

	public String getHinVersion()
	{
		return hinVersion;
	}

	public void setHinVersion(String hinVersion)
	{
		this.hinVersion = hinVersion;
	}

	public String getSin()
	{
		return sin;
	}

	public void setSin(String sin)
	{
		this.sin = sin;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public Date getPhotoUpdateDate()
	{
		return photoUpdateDate;
	}

	public void setPhotoUpdateDate(Date photoUpdateDate)
	{
		this.photoUpdateDate = photoUpdateDate;
	}

	public byte[] getPhoto()
	{
		return photo;
	}

	public void setPhoto(byte[] photo)
	{
		this.photo = photo;
	}

	public String getHinType()
	{
		return hinType;
	}

	public void setHinType(String hinType)
	{
		this.hinType = hinType;
	}

	public boolean getRemoveId()
	{
		return removeId;
	}

	public void setRemoveId(boolean removeId)
	{
		this.removeId = removeId;
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
