package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedAdmission extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;

	@Column(nullable = false)
	@Index
	private int caisiDemographicId = 0;
	
	@Column(nullable = false)
	@Index
	private int caisiProgramId = 0;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Index
	private Date admissionDate = null;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dischargeDate = null;

	@Column(columnDefinition = "mediumtext")
	private String admissionNotes = null;

	@Column(columnDefinition = "mediumtext")
	private String dischargeNotes = null;

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityIdIntegerCompositePk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityIdIntegerCompositePk)
	{
		this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIdIntegerCompositePk;
	}

	public int getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(int caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public int getCaisiProgramId()
	{
		return caisiProgramId;
	}

	public void setCaisiProgramId(int caisiProgramId)
	{
		this.caisiProgramId = caisiProgramId;
	}

	public Date getAdmissionDate()
	{
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate)
	{
		this.admissionDate = admissionDate;
	}

	public Date getDischargeDate()
	{
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate)
	{
		this.dischargeDate = dischargeDate;
	}

	public String getAdmissionNotes()
	{
		return admissionNotes;
	}

	public void setAdmissionNotes(String admissionNotes)
	{
		this.admissionNotes = admissionNotes;
	}

	public String getDischargeNotes()
	{
		return dischargeNotes;
	}

	public void setDischargeNotes(String dischargeNotes)
	{
		this.dischargeNotes = dischargeNotes;
	}	
}
