package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"integratorDemographicFacilityId1","caisiDemographicId1", "integratorDemographicFacilityId2","caisiDemographicId2"}))
public class DemographicLink extends AbstractModel<Integer>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(nullable=false)
	@Index
	private Integer integratorDemographicFacilityId1 = null;

	@Column(nullable=false)
	@Index
	private Integer caisiDemographicId1 = null;

	@Column(nullable=false)
	@Index
	private Integer integratorDemographicFacilityId2 = null;

	@Column(nullable=false)
	@Index
	private Integer caisiDemographicId2 = null;

	@Column(nullable=false)
	private Integer creatorIntegratorProviderFacilityId = null;

	@Column(nullable=false, length=16)
	private String creatorCaisiProviderId = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date createdDate = new Date();

	public Integer getIntegratorDemographicFacilityId1()
	{
		return integratorDemographicFacilityId1;
	}

	public void setIntegratorDemographicFacilityId1(Integer integratorDemographicFacilityId1)
	{
		this.integratorDemographicFacilityId1 = integratorDemographicFacilityId1;
	}

	public Integer getCaisiDemographicId1()
	{
		return caisiDemographicId1;
	}

	public void setCaisiDemographicId1(Integer caisiDemographicId1)
	{
		this.caisiDemographicId1 = caisiDemographicId1;
	}

	public Integer getIntegratorDemographicFacilityId2()
	{
		return integratorDemographicFacilityId2;
	}

	public void setIntegratorDemographicFacilityId2(Integer integratorDemographicFacilityId2)
	{
		this.integratorDemographicFacilityId2 = integratorDemographicFacilityId2;
	}

	public Integer getCaisiDemographicId2()
	{
		return caisiDemographicId2;
	}

	public void setCaisiDemographicId2(Integer caisiDemographicId2)
	{
		this.caisiDemographicId2 = caisiDemographicId2;
	}

	public Integer getCreatorIntegratorProviderFacilityId()
	{
		return creatorIntegratorProviderFacilityId;
	}

	public void setCreatorIntegratorProviderFacilityId(Integer creatorIntegratorProviderFacilityId)
	{
		this.creatorIntegratorProviderFacilityId = creatorIntegratorProviderFacilityId;
	}

	public String getCreatorCaisiProviderId()
	{
		return creatorCaisiProviderId;
	}

	public void setCreatorCaisiProviderId(String creatorCaisiProviderId)
	{
		this.creatorCaisiProviderId = creatorCaisiProviderId;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	@Override
	public int hashCode()
	{
		return(id.hashCode());
	}

	@Override
	public boolean equals(Object o)
	{
		return(id.equals(((DemographicLink)o).getId()));
	}

	@Override
	public Integer getId()
	{
		return id;
	}
}
