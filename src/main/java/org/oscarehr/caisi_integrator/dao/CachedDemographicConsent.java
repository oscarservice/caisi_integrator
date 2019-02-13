package org.oscarehr.caisi_integrator.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.PersistentMap;
import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.KeyColumn;

/**
 * This class should represent the latest useful consent set for a given demographic
 * at a given facility. We only need the latest one from each facility to determine 
 * which one is in use so we don't need a history nor do we want non useful ones here
 * (i.e. deferred, or refused to sign). 
 */
@Entity
public class CachedDemographicConsent extends AbstractModel<FacilityIdIntegerCompositePk>
{
	/**
	 * Only need the useful subset of consents from the caisi side, non useful ones 
	 * should not be stored here.
	 */
	public enum ConsentStatus
	{
		GIVEN, REVOKED
	}

	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityDemographicPk;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate = null;

	/**
	 * (Integer,Boolean) is (IntegratorFacilityId,ShareData).
	 */
	@PersistentMap(fetch = FetchType.EAGER)
	@ContainerTable(name = "CachedDemographicConsentConsents")
	@KeyColumn(name = "integratorFacilityId")
	@ElementJoinColumn(name = "shareData", columnDefinition = "tinyint(1)")
	private Map<Integer, Boolean> consentToShareData = new HashMap<Integer, Boolean>();

	private boolean excludeMentalHealthData = false;

	@Enumerated(EnumType.STRING)
	private ConsentStatus clientConsentStatus = null;

	@Temporal(TemporalType.DATE)
	private Date expiry=null;
	
	public FacilityIdIntegerCompositePk getFacilityDemographicPk()
	{
		return facilityDemographicPk;
	}

	public void setFacilityDemographicPk(FacilityIdIntegerCompositePk facilityDemographicPk)
	{
		this.facilityDemographicPk = facilityDemographicPk;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityDemographicPk;
	}

	public Map<Integer, Boolean> getConsentToShareData()
	{
		return consentToShareData;
	}

	public void setConsentToShareData(Map<Integer, Boolean> consentToShareData)
	{
		this.consentToShareData = consentToShareData;
	}

	/**
	 * This checks the business criteria for allowing or disallowing sharing for 
	 * this facility
	 */
	public boolean allowedToShareData(Integer integratorFacilityId)
	{
		if (clientConsentStatus!=ConsentStatus.GIVEN) return(false);
		if (expiry!=null && expiry.before(new Date())) return(false);
		
		Boolean result=consentToShareData.get(integratorFacilityId);
		if (result==null) return(true); // was asked to default to true;
		else return(result);
	}
	
	public boolean isExcludeMentalHealthData()
	{
		return excludeMentalHealthData;
	}

	public void setExcludeMentalHealthData(boolean excludeMentalHealthData)
	{
		this.excludeMentalHealthData = excludeMentalHealthData;
	}

	public ConsentStatus getClientConsentStatus()
	{
		return clientConsentStatus;
	}

	public void setClientConsentStatus(ConsentStatus clientConsentStatus)
	{
		this.clientConsentStatus = clientConsentStatus;
	}

	public Date getExpiry()
	{
		return expiry;
	}

	public void setExpiry(Date expiry)
	{
		this.expiry = expiry;
	}
}