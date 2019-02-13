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
public class CachedDemographicAllergy extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;

	@Column(nullable = false)
	@Index
	private int caisiDemographicId = 0;

	@Temporal(TemporalType.TIMESTAMP)
	private Date entryDate;

	private int pickId;
	private String description;
	private int hiclSeqNo;
	private int hicSeqNo;
	private int agcsp;
	private int agccs;
	private int typeCode;
	private String reaction;	

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	private String ageOfOnset;
	private String severityCode;
	private String onSetCode;
	private String regionalIdentifier;
	private String lifeStage;
	
	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIdIntegerCompositePk;
	}

	public void setCaisiDemographicId(int caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public int getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setEntryDate(Date entryDate)
	{
		this.entryDate = entryDate;
	}

	public Date getEntryDate()
	{
		return entryDate;
	}

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityIdIntegerCompositePk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityIdIntegerCompositePk)
	{
		this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = StringUtils.trimToNull(description);
	}

	public String getReaction()
	{
		return reaction;
	}

	public void setReaction(String reaction)
	{
		this.reaction = StringUtils.trimToNull(reaction);
	}

	public int getPickId()
	{
		return(pickId);
	}

	public void setPickId(int pickId)
	{
		this.pickId = pickId;
	}

	public int getHiclSeqNo()
	{
		return(hiclSeqNo);
	}

	public void setHiclSeqNo(int hiclSeqNo)
	{
		this.hiclSeqNo = hiclSeqNo;
	}

	public int getHicSeqNo()
	{
		return(hicSeqNo);
	}

	public void setHicSeqNo(int hicSeqNo)
	{
		this.hicSeqNo = hicSeqNo;
	}

	public int getAgcsp()
	{
		return(agcsp);
	}

	public void setAgcsp(int agcsp)
	{
		this.agcsp = agcsp;
	}

	public int getAgccs()
	{
		return(agccs);
	}

	public void setAgccs(int agccs)
	{
		this.agccs = agccs;
	}

	public int getTypeCode()
	{
		return(typeCode);
	}

	public void setTypeCode(int typeCode)
	{
		this.typeCode = typeCode;
	}

	public Date getStartDate()
	{
		return(startDate);
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public String getAgeOfOnset()
	{
		return(ageOfOnset);
	}

	public void setAgeOfOnset(String ageOfOnset)
	{
		this.ageOfOnset = StringUtils.trimToNull(ageOfOnset);
	}

	public String getSeverityCode()
	{
		return(severityCode);
	}

	public void setSeverityCode(String severityCode)
	{
		this.severityCode = StringUtils.trimToNull(severityCode);
	}

	public String getOnSetCode()
	{
		return(onSetCode);
	}

	public void setOnSetCode(String onSetCode)
	{
		this.onSetCode = StringUtils.trimToNull(onSetCode);
	}

	public String getRegionalIdentifier()
	{
		return(regionalIdentifier);
	}

	public void setRegionalIdentifier(String regionalIdentifier)
	{
		this.regionalIdentifier = StringUtils.trimToNull(regionalIdentifier);
	}

	public String getLifeStage()
	{
		return(lifeStage);
	}

	public void setLifeStage(String lifeStage)
	{
		this.lifeStage = StringUtils.trimToNull(lifeStage);
	}
}