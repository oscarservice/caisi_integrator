package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class Referral extends AbstractModel<Integer>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(nullable=false)
	@Index
	private Integer sourceIntegratorFacilityId = null;

	@Column(nullable=false)
	@Index
	private Integer sourceCaisiDemographicId = null;

	@Column(nullable=false, length=32)
	private String sourceCaisiProviderId = null;

	@Column(nullable=false)
	@Index
	private Integer destinationIntegratorFacilityId = null;

	@Column(nullable=false)
	@Index
	private Integer destinationCaisiProgramId = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date referralDate = null;

	@Column(columnDefinition="text")
	private String reasonForReferral = null;

	@Column(columnDefinition="text")
	private String presentingProblem = null;

	@Override
	public Integer getId()
	{
		return(id);
	}

	public Integer getReferralId()
	{
		return(id);
	}
	
	/**
	 * @param id  
	 */
	public void setReferralId(Integer id)
	{
		// this is only here for ws marshalling
		throw(new UnsupportedOperationException());
	}
	
	public Integer getSourceIntegratorFacilityId()
	{
		return sourceIntegratorFacilityId;
	}

	public void setSourceIntegratorFacilityId(Integer sourceIntegratorFacilityId)
	{
		this.sourceIntegratorFacilityId = sourceIntegratorFacilityId;
	}

	public Integer getSourceCaisiDemographicId()
	{
		return sourceCaisiDemographicId;
	}

	public void setSourceCaisiDemographicId(Integer sourceCaisiDemographicId)
	{
		this.sourceCaisiDemographicId = sourceCaisiDemographicId;
	}

	public String getSourceCaisiProviderId()
	{
		return sourceCaisiProviderId;
	}

	public void setSourceCaisiProviderId(String sourceCaisiProviderId)
	{
		this.sourceCaisiProviderId = StringUtils.trimToNull(sourceCaisiProviderId);
	}

	public Integer getDestinationIntegratorFacilityId()
	{
		return destinationIntegratorFacilityId;
	}

	public void setDestinationIntegratorFacilityId(Integer destinationIntegratorFacilityId)
	{
		this.destinationIntegratorFacilityId = destinationIntegratorFacilityId;
	}

	public Integer getDestinationCaisiProgramId()
	{
		return destinationCaisiProgramId;
	}

	public void setDestinationCaisiProgramId(Integer destinationCaisiProgramId)
	{
		this.destinationCaisiProgramId = destinationCaisiProgramId;
	}

	public Date getReferralDate()
	{
		return referralDate;
	}

	public void setReferralDate(Date referralDate)
	{
		this.referralDate = referralDate;
	}

	public String getReasonForReferral()
	{
		return reasonForReferral;
	}

	public void setReasonForReferral(String reasonForReferral)
	{
		this.reasonForReferral = StringUtils.trimToNull(reasonForReferral);
	}

	public String getPresentingProblem()
	{
		return presentingProblem;
	}

	public void setPresentingProblem(String presentingProblem)
	{
		this.presentingProblem = StringUtils.trimToNull(presentingProblem);
	}
}
