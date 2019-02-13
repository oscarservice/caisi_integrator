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

/**
 * This entity is intended to allow providers to send other providers data.
 * The data can be anything from a referral or message or picture or what ever.
 * The data is determined by the content.
 */
@Entity
public class ProviderCommunication extends AbstractModel<Integer>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(nullable=false)
	private Integer sourceIntegratorFacilityId = null;

	@Column(nullable=false)
	private String sourceProviderId = null;

	@Column(nullable=false)
	@Index
	private Integer destinationIntegratorFacilityId = null;

	@Column(nullable=false)
	@Index
	private String destinationProviderId = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date sentDate = new Date();

	@Column(nullable=false, columnDefinition="tinyint")
	private boolean active = true;

	/**
	 * Type can be used to distinguish different types of messages, such as "text message" or "image" or "followup" or what ever.
	 */
	@Index
	private String type=null;
	
	@Column(columnDefinition="mediumblob")
	private byte[] data = null;

	@Override
	public Integer getId()
	{
		return(id);
	}

	public Integer getSourceIntegratorFacilityId()
	{
		return(sourceIntegratorFacilityId);
	}

	public void setSourceIntegratorFacilityId(Integer sourceIntegratorFacilityId)
	{
		this.sourceIntegratorFacilityId = sourceIntegratorFacilityId;
	}

	public String getSourceProviderId()
	{
		return(sourceProviderId);
	}

	public void setSourceProviderId(String sourceProviderId)
	{
		this.sourceProviderId = sourceProviderId;
	}

	public Integer getDestinationIntegratorFacilityId()
	{
		return(destinationIntegratorFacilityId);
	}

	public void setDestinationIntegratorFacilityId(Integer destinationIntegratorFacilityId)
	{
		this.destinationIntegratorFacilityId = destinationIntegratorFacilityId;
	}

	public String getDestinationProviderId()
	{
		return(destinationProviderId);
	}

	public void setDestinationProviderId(String destinationProviderId)
	{
		this.destinationProviderId = destinationProviderId;
	}

	public Date getSentDate()
	{
		return(sentDate);
	}

	public void setSentDate(Date sentDate)
	{
		this.sentDate = sentDate;
	}

	public boolean isActive()
	{
		return(active);
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public byte[] getData()
	{
		return(data);
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public String getType()
	{
		return(type);
	}

	public void setType(String type)
	{
		this.type = StringUtils.trimToNull(type);
	}
}
