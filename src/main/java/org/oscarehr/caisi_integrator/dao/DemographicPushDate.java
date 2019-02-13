package org.oscarehr.caisi_integrator.dao;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class DemographicPushDate extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@Id
	private FacilityIdIntegerCompositePk id = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Index
	private Calendar lastPushDate = new GregorianCalendar();

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return id;
	}

	public Calendar getLastPushDate()
	{
		return(lastPushDate);
	}

	public void setLastPushDate(Calendar lastPushDate)
	{
		this.lastPushDate = lastPushDate;
	}

	public void setId(FacilityIdIntegerCompositePk id)
	{
		this.id = id;
	}

}
