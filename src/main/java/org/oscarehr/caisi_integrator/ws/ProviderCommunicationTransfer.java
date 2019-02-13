package org.oscarehr.caisi_integrator.ws;

import java.util.Date;

public class ProviderCommunicationTransfer
{
	private Integer id = null;
	private Integer sourceIntegratorFacilityId = null;
	private String sourceProviderId = null;
	private Integer destinationIntegratorFacilityId = null;
	private String destinationProviderId = null;
	private Date sentDate = new Date();
	private boolean active = true;
	private String type = null;
	private byte[] data = null;

	public Integer getId()
	{
		return(id);
	}

	public void setId(Integer id)
	{
		this.id = id;
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

	public String getType()
	{
		return(type);
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public byte[] getData()
	{
		return(data);
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

}