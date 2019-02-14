package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;
import java.util.Date;

public class IntegratorFileHeader implements Serializable
{

	private Date lastDate;

	private Date date;

	private String dependsOn;

	private Integer cachedFacilityId;

	private String cachedFacilityName;

	private String username;

	public static final int VERSION = 1;

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public Date getLastDate()
	{
		return lastDate;
	}

	public void setLastDate(Date lastDate)
	{
		this.lastDate = lastDate;
	}

	public String getDependsOn()
	{
		return dependsOn;
	}

	public void setDependsOn(String dependsOn)
	{
		this.dependsOn = dependsOn;
	}

	public int getVersion()
	{
		return VERSION;
	}

	public Integer getCachedFacilityId()
	{
		return cachedFacilityId;
	}

	public void setCachedFacilityId(Integer cachedFacilityId)
	{
		this.cachedFacilityId = cachedFacilityId;
	}

	public String getCachedFacilityName()
	{
		return cachedFacilityName;
	}

	public void setCachedFacilityName(String cachedFacilityName)
	{
		this.cachedFacilityName = cachedFacilityName;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

}
