package org.oscarehr.caisi_integrator.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.caisi_integrator.dao.CachedDemographic.Gender;

@Entity
public class CachedProgram extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedProgram>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;

	private String name = null;
	private String description = null;

	@Column(length = 32)
	private String type = null;

	@Column(length = 32)
	private String status = null;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Gender gender = null;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean firstNation = false;

	@Column(nullable = false)
	private int minAge = 0;

	@Column(nullable = false)
	private int maxAge = 200;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean bedProgramAffiliated = false;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean alcohol = false;

	@Column(length = 32)
	private String abstinenceSupport = null;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean physicalHealth = false;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean mentalHealth = false;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean housing = false;

	private String address = null;
	private String phone = null;
	private String fax = null;
	private String url = null;
	private String email = null;
	private String emergencyNumber = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityIdIntegerCompositePk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityIdIntegerCompositePk)
	{
		this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = StringUtils.trimToNull(name);
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = StringUtils.trimToNull(description);
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = StringUtils.trimToNull(type);
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = StringUtils.trimToNull(status);
	}

	public boolean isBedProgramAffiliated()
	{
		return bedProgramAffiliated;
	}

	public void setBedProgramAffiliated(boolean bedProgramAffiliated)
	{
		this.bedProgramAffiliated = bedProgramAffiliated;
	}

	public boolean isAlcohol()
	{
		return alcohol;
	}

	public void setAlcohol(boolean alcohol)
	{
		this.alcohol = alcohol;
	}

	public String getAbstinenceSupport()
	{
		return abstinenceSupport;
	}

	public void setAbstinenceSupport(String abstinenceSupport)
	{
		this.abstinenceSupport = StringUtils.trimToNull(abstinenceSupport);
	}

	public boolean isPhysicalHealth()
	{
		return physicalHealth;
	}

	public void setPhysicalHealth(boolean physicalHealth)
	{
		this.physicalHealth = physicalHealth;
	}

	public boolean isMentalHealth()
	{
		return mentalHealth;
	}

	public void setMentalHealth(boolean mentalHealth)
	{
		this.mentalHealth = mentalHealth;
	}

	public boolean isHousing()
	{
		return housing;
	}

	public void setHousing(boolean housing)
	{
		this.housing = housing;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setGender(Gender gender)
	{
		this.gender = gender;
	}

	public boolean isFirstNation()
	{
		return firstNation;
	}

	public void setFirstNation(boolean firstNation)
	{
		this.firstNation = firstNation;
	}

	public int getMinAge()
	{
		return minAge;
	}

	public void setMinAge(int minAge)
	{
		this.minAge = minAge;
	}

	public int getMaxAge()
	{
		return maxAge;
	}

	public void setMaxAge(int maxAge)
	{
		this.maxAge = maxAge;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getEmergencyNumber()
	{
		return emergencyNumber;
	}

	public void setEmergencyNumber(String emergencyNumber)
	{
		this.emergencyNumber = emergencyNumber;
	}

	@Override
	public int compareTo(CachedProgram o)
	{
		return(facilityIdIntegerCompositePk.getCaisiItemId() - o.facilityIdIntegerCompositePk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIdIntegerCompositePk;
	}
}
