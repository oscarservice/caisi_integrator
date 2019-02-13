package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedDemographicDocument extends AbstractModel<FacilityIdIntegerCompositePk>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityIntegerPk;

	@Column(nullable = false)
	@Index
	private int caisiDemographicId = 0;

	private String docType;
	private String docXml;
	private String docFilename;
	private String docCreator;
	private String responsible;
	private String source;
	private Integer programId;
	private Date updateDateTime;
	private String status;
	private String contentType;
	private int public1;
	private Date observationDate;
	private String reviewer;
	private Date reviewDateTime;
	private int numberOfPages;
	private int appointmentNo;
	private String description;

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityIntegerPk;
	}

	public FacilityIdIntegerCompositePk getFacilityIntegerPk()
	{
		return facilityIntegerPk;
	}

	public void setFacilityIntegerPk(FacilityIdIntegerCompositePk facilityIntegerPk)
	{
		this.facilityIntegerPk = facilityIntegerPk;
	}

	public int getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(int caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getDocType()
	{
		return docType;
	}

	public void setDocType(String docType)
	{
		this.docType = StringUtils.trimToNull(docType);
	}

	public String getDocXml()
	{
		return docXml;
	}

	public void setDocXml(String docXml)
	{
		this.docXml = StringUtils.trimToNull(docXml);
	}

	public String getDescription()
	{
		return(description);
	}

	public void setDescription(String description)
	{
		this.description = StringUtils.trimToNull(description);
	}

	public String getDocFilename()
	{
		return docFilename;
	}

	public void setDocFilename(String docFilename)
	{
		this.docFilename = StringUtils.trimToNull(docFilename);
	}

	public String getDocCreator()
	{
		return docCreator;
	}

	public void setDocCreator(String docCreator)
	{
		this.docCreator = StringUtils.trimToNull(docCreator);
	}

	public String getResponsible()
	{
		return responsible;
	}

	public void setResponsible(String responsible)
	{
		this.responsible = StringUtils.trimToNull(responsible);
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = StringUtils.trimToNull(source);
	}

	public Integer getProgramId()
	{
		return programId;
	}

	public void setProgramId(Integer programId)
	{
		this.programId = programId;
	}

	public Date getUpdateDateTime()
	{
		return updateDateTime;
	}

	public void setUpdateDateTime(Date updateDateTime)
	{
		this.updateDateTime = updateDateTime;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = StringUtils.trimToNull(status);
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = StringUtils.trimToNull(contentType);
	}

	public int getPublic1()
	{
		return public1;
	}

	public void setPublic1(int public1)
	{
		this.public1 = public1;
	}

	public Date getObservationDate()
	{
		return observationDate;
	}

	public void setObservationDate(Date observationDate)
	{
		this.observationDate = observationDate;
	}

	public String getReviewer()
	{
		return reviewer;
	}

	public void setReviewer(String reviewer)
	{
		this.reviewer = StringUtils.trimToNull(reviewer);
	}

	public Date getReviewDateTime()
	{
		return reviewDateTime;
	}

	public void setReviewDateTime(Date reviewDateTime)
	{
		this.reviewDateTime = reviewDateTime;
	}

	public int getNumberOfPages()
	{
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages)
	{
		this.numberOfPages = numberOfPages;
	}

	public int getAppointmentNo()
	{
		return appointmentNo;
	}

	public void setAppointmentNo(int appointmentNo)
	{
		this.appointmentNo = appointmentNo;
	}

}