package org.oscarehr.caisi_integrator.dao;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.apache.openjpa.persistence.PersistentCollection;
import org.apache.openjpa.persistence.jdbc.ContainerTable;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.Index;
import org.apache.openjpa.persistence.jdbc.XJoinColumn;

@Entity
public class CachedDemographicNote extends AbstractModel<CachedDemographicNoteCompositePk>
{
	public static final Comparator<CachedDemographicNote> OBSERVATION_DATE_COMPARATOR = new Comparator<CachedDemographicNote>()
	{
		@Override
		public int compare(CachedDemographicNote o1, CachedDemographicNote o2)
		{
			return(o1.getObservationDate().compareTo(o2.getObservationDate()));
		}
	};

	@EmbeddedId
	@Index
	private CachedDemographicNoteCompositePk cachedDemographicNoteCompositePk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date updateDate = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date observationDate = null;

	@Column(nullable = false)
	private int caisiDemographicId = 0;

	@Column(nullable = false, length = 16)
	private String observationCaisiProviderId = null;

	@Column(length = 16)
	private String signingCaisiProviderId = null;

	@Column(nullable = false, length = 100)
	private String encounterType = null;

	@Column(nullable = false)
	private int caisiProgramId = 0;

	@Column(columnDefinition = "mediumtext")
	private String note = null;

	@Column(nullable = false, length = 64)
	private String role = null;

	@PersistentCollection(elementType = String.class/*, fetch = FetchType.EAGER*/)
	@Externalizer("NoteIssue.toStrings")
	@Factory("NoteIssue.fromStrings")
	@ContainerTable(name = "CachedDemographicNoteIssues", joinColumns = { @XJoinColumn(name = "integratorFacilityId", nullable = false), @XJoinColumn(name = "uuid", nullable = false) })
	@ElementJoinColumn(name = "noteIssue", nullable = false, columnDefinition = "varchar(64)")
	private HashSet<NoteIssue> issues = new HashSet<NoteIssue>();

	/**
	 * This is a work around for eager fetching of primitive collections error.
	 */
	@Transient
	private HashSet<NoteIssue> tempHackIssues = null;
	@Transient
	private boolean tempHackIssuesUsed = false;

	public CachedDemographicNote()
	{

	}

	public CachedDemographicNote(Integer integratedFacilityId, String uuid, int caisiDemographicId, int caisiProgramId, String observationCaisiProviderId, Date observationDate, String role)
	{

		this.cachedDemographicNoteCompositePk = new CachedDemographicNoteCompositePk(integratedFacilityId, uuid);
		this.caisiDemographicId = caisiDemographicId;
		this.caisiProgramId = caisiProgramId;
		this.observationCaisiProviderId = observationCaisiProviderId;
		this.observationDate = observationDate;
		this.role = role;
	}

	public void setCachedDemographicNoteCompositePk(CachedDemographicNoteCompositePk cachedDemographicNoteCompositePk)
	{
		this.cachedDemographicNoteCompositePk = cachedDemographicNoteCompositePk;
	}

	public CachedDemographicNoteCompositePk getCachedDemographicNoteCompositePk()
	{
		return cachedDemographicNoteCompositePk;
	}

	@Override
	public CachedDemographicNoteCompositePk getId()
	{
		return cachedDemographicNoteCompositePk;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public Date getObservationDate()
	{
		return observationDate;
	}

	public void setObservationDate(Date observationDate)
	{
		this.observationDate = observationDate;
	}

	public int getCaisiDemographicId()
	{
		return caisiDemographicId;
	}

	public void setCaisiDemographicId(int caisiDemographicId)
	{
		this.caisiDemographicId = caisiDemographicId;
	}

	public String getObservationCaisiProviderId()
	{
		return observationCaisiProviderId;
	}

	public void setObservationCaisiProviderId(String observationCaisiProviderId)
	{
		this.observationCaisiProviderId = observationCaisiProviderId;
	}

	public int getCaisiProgramId()
	{
		return caisiProgramId;
	}

	public void setCaisiProgramId(int caisiProgramId)
	{
		this.caisiProgramId = caisiProgramId;
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getSigningCaisiProviderId()
	{
		return signingCaisiProviderId;
	}

	public void setSigningCaisiProviderId(String signingCaisiProviderId)
	{
		this.signingCaisiProviderId = signingCaisiProviderId;
	}

	public String getEncounterType()
	{
		return encounterType;
	}

	public void setEncounterType(String encounterType)
	{
		this.encounterType = encounterType;
	}

	public HashSet<NoteIssue> getIssues()
	{
		if (issues == null)
		{
			tempHackIssuesUsed = true;
			return(tempHackIssues);
		}

		return issues;
	}

	public void setIssues(HashSet<NoteIssue> issues)
	{
		this.issues = issues;
	}

	@PostLoad
	protected void logRead()
	{
		tempHackIssues = new HashSet<NoteIssue>(issues);
	}

	@PostPersist
	@PostUpdate
	protected void postUpdatePersists()
	{
		if (tempHackIssuesUsed)
		{
			issues.clear();
			issues.addAll(tempHackIssues);
		}
	}
}
