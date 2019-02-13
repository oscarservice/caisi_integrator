package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedAppointment extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedAppointment>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityAppointmentPk;

	@Column(nullable = false)
	@Index
	private Integer caisiDemographicId = null;

	@Column(nullable = false, length = 16)
	private String caisiProviderId = null;

	@Temporal(TemporalType.DATE)
	private Date appointmentDate = null;

	@Temporal(TemporalType.TIME)
	private Date startTime = null;

	@Temporal(TemporalType.TIME)
	private Date endTime = null;

	@Column(length = 80)
	private String notes = null;

	@Column(length = 80)
	private String reason = null;

	@Column(length = 30)
	private String location = null;

	@Column(length = 255)
	private String resources = null;

	@Column(length = 10)
	private String type = null;

	@Column(length = 10)
	private String style = null;

	@Column(length = 2)
	private String status = null;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDatetime = null;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDatetime = null;

	@Column(length = 50)
	private String remarks = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityAppointmentPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityAppointmentPk)
	{
		this.facilityAppointmentPk = facilityAppointmentPk;
	}

	public Date getAppointmentDate()
	{
		return this.appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate)
	{
		this.appointmentDate = appointmentDate;
	}

	public Date getStartTime()
	{
		return this.startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return this.endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Integer getCaisiDemographicId()
	{
		return this.caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getCaisiProviderId()
	{
		return this.caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId = StringUtils.trimToEmpty(caisiProviderId);
	}

	public String getLocation()
	{
		return this.location;
	}

	public void setLocation(String location)
	{
		this.location = StringUtils.trimToNull(location);
	}

	public String getNotes()
	{
		return this.notes;
	}

	public void setNotes(String notes)
	{
		this.notes = StringUtils.trimToNull(notes);
	}

	public String getReason()
	{
		return this.reason;
	}

	public void setReason(String reason)
	{
		this.reason = StringUtils.trimToNull(reason);
	}

	public String getRemarks()
	{
		return this.remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = StringUtils.trimToNull(remarks);
	}

	public String getResources()
	{
		return this.resources;
	}

	public void setResources(String resources)
	{
		this.resources = StringUtils.trimToNull(resources);
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = StringUtils.trimToNull(status);
	}

	public String getStyle()
	{
		return this.style;
	}

	public void setStyle(String style)
	{
		this.style = StringUtils.trimToNull(style);
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = StringUtils.trimToNull(type);
	}

	public Date getCreateDatetime()
	{
		return this.createDatetime;
	}

	public void setCreateDatetime(Date createDatetime)
	{
		this.createDatetime = createDatetime;
	}

	public Date getUpdateDatetime()
	{
		return this.updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime)
	{
		this.updateDatetime = updateDatetime;
	}

	@Override
	public int compareTo(CachedAppointment o)
	{
		return(facilityAppointmentPk.getCaisiItemId() - o.facilityAppointmentPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityAppointmentPk;
	}
}
