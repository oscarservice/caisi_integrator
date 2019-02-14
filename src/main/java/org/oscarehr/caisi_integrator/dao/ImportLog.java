package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.DataCache;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
@DataCache(enabled = false)
public class ImportLog extends AbstractModel<Long>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id = null;

	@Column(length = 255)
	private String filename;

	@Column(length = 255)
	private String checksum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dateIntervalStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dateIntervalEnd;

	@Column(length = 50)
	private String status;

	@Index
	private Integer facilityId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date lastUpdatedDate;

	@Column(length = 255)
	private String dependsOn;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getChecksum()
	{
		return checksum;
	}

	public void setChecksum(String checksum)
	{
		this.checksum = checksum;
	}

	public Date getDateIntervalStart()
	{
		return dateIntervalStart;
	}

	public void setDateIntervalStart(Date dateIntervalStart)
	{
		this.dateIntervalStart = dateIntervalStart;
	}

	public Date getDateIntervalEnd()
	{
		return dateIntervalEnd;
	}

	public void setDateIntervalEnd(Date dateIntervalEnd)
	{
		this.dateIntervalEnd = dateIntervalEnd;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Integer getFacilityId()
	{
		return facilityId;
	}

	public void setFacilityId(Integer facilityId)
	{
		this.facilityId = facilityId;
	}

	public Date getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdatedDate()
	{
		return lastUpdatedDate;
	}

	public String getDependsOn()
	{
		return dependsOn;
	}

	public void setDependsOn(String dependsOn)
	{
		this.dependsOn = dependsOn;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate)
	{
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

}
