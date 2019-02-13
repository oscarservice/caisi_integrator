package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedDemographicDrug extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;

	@Column(nullable=false, length=16)
	private String caisiProviderId=null;

	@Column(nullable=false)
	@Index
	private Integer caisiDemographicId=null;
	
	@Temporal(TemporalType.DATE)
	private Date rxDate=null;
	
	@Temporal(TemporalType.DATE)
	private Date endDate=null;

	@Column(length=255)
	private String brandName=null;
	
	@Column(length=255)
	private String customName=null;
	private float takeMin=0;
	private float takeMax=0;

	@Column(length=64)
	private String freqCode=null;

	@Column(length=64)
	private String duration=null;

	@Column(length=64)
	private String durUnit=null;

	@Column(length=64)
	private String quantity=null;
	private int repeats=0;
	
	@Temporal(TemporalType.DATE)
	private Date lastRefillDate=null;

	@Column(columnDefinition="tinyint(1)")
	private boolean noSubs;

	@Column(columnDefinition="tinyint(1)")
	private boolean prn;

	@Column(columnDefinition="text")
	private String special=null;

	@Column(columnDefinition="tinyint(1)")
	private boolean archived;

	@Column(length=100)
	private String archivedReason;

        @Temporal(TemporalType.TIMESTAMP)
        private Date archivedDate;

	@Column(length=255)
	private String genericName=null;
	
	@Column(length=64)
	private String atc=null;

	private int scriptNo=0;

	@Column(length=64)
	private String regionalIdentifier=null;

	@Column(length=64)
	private String unit=null;

	@Column(length=64)
	private String method=null;

	@Column(length=64)
	private String route=null;

	@Column(length=64)
	private String drugForm=null;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate=null;

	@Column(columnDefinition="text")
	private String dosage=null;

	@Column(columnDefinition="tinyint(1)")
	private boolean customInstructions;
	
	@Column(length=32)
	private String unitName=null;

	@Column(columnDefinition="tinyint(1)")
	private Boolean longTerm;

	@Column(columnDefinition="tinyint(1)")
	private Boolean pastMed;

	@Column(columnDefinition="tinyint(1)")
	private Boolean patientCompliance;

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIdIntegerCompositePk;
	}

	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityIdIntegerCompositePk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityIdIntegerCompositePk)
	{
		this.facilityIdIntegerCompositePk=facilityIdIntegerCompositePk;
	}

	public String getCaisiProviderId()
	{
		return caisiProviderId;
	}

	public void setCaisiProviderId(String caisiProviderId)
	{
		this.caisiProviderId=caisiProviderId;
	}

	public Integer getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(Integer caisiDemographicId)
	{
		this.caisiDemographicId=caisiDemographicId;
	}

	public Date getRxDate()
	{
		return rxDate;
	}

	public void setRxDate(Date rxDate)
	{
		this.rxDate=rxDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate=endDate;
	}

	public String getBrandName()
	{
		return brandName;
	}

	public void setBrandName(String brandName)
	{
		this.brandName=brandName;
	}

	public String getCustomName()
	{
		return customName;
	}

	public void setCustomName(String customName)
	{
		this.customName=customName;
	}

	public float getTakeMin()
	{
		return takeMin;
	}

	public void setTakeMin(float takeMin)
	{
		this.takeMin=takeMin;
	}

	public float getTakeMax()
	{
		return takeMax;
	}

	public void setTakeMax(float takeMax)
	{
		this.takeMax=takeMax;
	}

	public String getFreqCode()
	{
		return freqCode;
	}

	public void setFreqCode(String freqCode)
	{
		this.freqCode=freqCode;
	}

	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration=duration;
	}

	public String getDurUnit()
	{
		return durUnit;
	}

	public void setDurUnit(String durUnit)
	{
		this.durUnit=durUnit;
	}

	public String getQuantity()
	{
		return quantity;
	}

	public void setQuantity(String quantity)
	{
		this.quantity=quantity;
	}

	public int getRepeats()
	{
		return repeats;
	}

	public void setRepeats(int repeats)
	{
		this.repeats=repeats;
	}

	public Date getLastRefillDate()
	{
		return lastRefillDate;
	}

	public void setLastRefillDate(Date lastRefillDate)
	{
		this.lastRefillDate=lastRefillDate;
	}

	public boolean isNoSubs()
	{
		return noSubs;
	}

	public void setNoSubs(boolean noSubs)
	{
		this.noSubs=noSubs;
	}

	public boolean isPrn()
	{
		return prn;
	}

	public void setPrn(boolean prn)
	{
		this.prn=prn;
	}

	public String getSpecial()
	{
		return special;
	}

	public void setSpecial(String special)
	{
		this.special=special;
	}

	public boolean isArchived()
	{
		return archived;
	}

	public void setArchived(boolean archived)
	{
		this.archived=archived;
	}

        public String getArchivedReason()
        {
                return archivedReason;
        }

        public void setArchivedReason(String archivedReason)
        {
                this.archivedReason = archivedReason;
        }

        public Date getArchivedDate()
        {
                return archivedDate;
        }

        public void setArchivedDate(Date archivedDate)
        {
                this.archivedDate = archivedDate;
        }

	public String getGenericName()
	{
		return genericName;
	}

	public void setGenericName(String genericName)
	{
		this.genericName=genericName;
	}

	public String getAtc()
	{
		return atc;
	}

	public void setAtc(String atc)
	{
		this.atc=atc;
	}

	public int getScriptNo()
	{
		return scriptNo;
	}

	public void setScriptNo(int scriptNo)
	{
		this.scriptNo=scriptNo;
	}

	public String getRegionalIdentifier()
	{
		return regionalIdentifier;
	}

	public void setRegionalIdentifier(String regionalIdentifier)
	{
		this.regionalIdentifier=regionalIdentifier;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit=unit;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method=method;
	}

	public String getRoute()
	{
		return route;
	}

	public void setRoute(String route)
	{
		this.route=route;
	}

	public String getDrugForm()
	{
		return drugForm;
	}

	public void setDrugForm(String drugForm)
	{
		this.drugForm=drugForm;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate=createDate;
	}

	public String getDosage()
	{
		return dosage;
	}

	public void setDosage(String dosage)
	{
		this.dosage=dosage;
	}

	public boolean isCustomInstructions()
	{
		return customInstructions;
	}

	public void setCustomInstructions(boolean customInstructions)
	{
		this.customInstructions=customInstructions;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName=unitName;
	}

	public Boolean getLongTerm()
	{
		return longTerm;
	}

	public void setLongTerm(Boolean longTerm)
	{
		this.longTerm=longTerm;
	}

	public Boolean getPastMed()
	{
		return pastMed;
	}

	public void setPastMed(Boolean pastMed)
	{
		this.pastMed=pastMed;
	}

	public Boolean getPatientCompliance()
	{
		return patientCompliance;
	}

	public void setPatientCompliance(Boolean patientCompliance)
	{
		this.patientCompliance=patientCompliance;
	}
}
