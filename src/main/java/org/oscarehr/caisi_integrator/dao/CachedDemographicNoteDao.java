package org.oscarehr.caisi_integrator.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicNoteDao extends AbstractDao<CachedDemographicNote>
{
	public CachedDemographicNoteDao()
	{
		super(CachedDemographicNote.class);
	}

	/**
	 * THis method will get matching notes, the startDate is inclusive and endDate is exclusive.
	 */
	public ArrayList<CachedDemographicNote> findFirstInstanceOfIssueByIssueCodeAndDate(NoteIssue noteIssue, GregorianCalendar startDate, GregorianCalendar endDate)
	{
		// this maybe more complicated than it originally sounds.
		// first of all we need to match all issues with notes
		// then we need to filter by issues by the ones we want
		// then we calculate the first instance of each issue
		// then we filter on the date range.

		// a reversal of the algorithm should be more efficient and accomplish
		// something similar, first filter issues by issues we want
		// then calculate matching notes to issues
		// then calculate first instance of issue.
		// then filter issues by date range we want.

		// you can not search by notes with in the date range as it will not gurantee 
		// the first instance of the issue is in that date range.

		String sqlCommand = "select CachedDemographicNote.* from CachedDemographicNote,CachedDemographicNoteIssues where CachedDemographicNote.integratorFacilityId=CachedDemographicNoteIssues.integratorFacilityId and CachedDemographicNote.uuid=CachedDemographicNoteIssues.uuid and CachedDemographicNoteIssues.noteIssue=?1";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, noteIssue.toString());

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> allNotesWithThisIssue = query.getResultList();

		// only deal with earliest notes
		// hash map of (facility+clientId) -> earliest note
		HashMap<FacilityIdIntegerCompositePk, CachedDemographicNote> firstNoteForClient = new HashMap<FacilityIdIntegerCompositePk, CachedDemographicNote>();
		for (CachedDemographicNote currentNote : allNotesWithThisIssue)
		{
			FacilityIdIntegerCompositePk clientPk = new FacilityIdIntegerCompositePk();
			clientPk.setIntegratorFacilityId(currentNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
			clientPk.setCaisiItemId(currentNote.getCaisiDemographicId());

			CachedDemographicNote existingNote = firstNoteForClient.get(clientPk);
			if (existingNote == null || currentNote.getObservationDate().before(existingNote.getObservationDate()))
			{
				firstNoteForClient.put(clientPk, currentNote);
			}
		}

		// only get notes within the time frame
		ArrayList<CachedDemographicNote> results = new ArrayList<CachedDemographicNote>();
		for (CachedDemographicNote note : firstNoteForClient.values())
		{
			Date observationDateTemp = note.getObservationDate();
			GregorianCalendar observationDate = new GregorianCalendar();
			observationDate.setTime(observationDateTemp);
			// materialise date;
			observationDate.getTime();

			if (observationDate.after(startDate) && observationDate.before(endDate))
			{
				results.add(note);
			}
		}

		// sort by date
		Collections.sort(results, CachedDemographicNote.OBSERVATION_DATE_COMPARATOR);

		return(results);
	}

	/**
	 * This method will get notes for the given demographic only, non linked.
	 */
	public List<CachedDemographicNote> findByDemographic(FacilityIdIntegerCompositePk cachedDemographicPk)
	{
		String sqlCommand = "select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, cachedDemographicPk.getIntegratorFacilityId());
		query.setParameter(2, cachedDemographicPk.getCaisiItemId());

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> results = query.getResultList();
		return(results);
	}

	/**
	 * This method will get notes for the given demographic only, non linked.
	 */
	public List<CachedDemographicNote> findByCompositeKeys(CachedDemographicNoteCompositePk[] cachedDemographicNotePks)
	{
		if (cachedDemographicNotePks == null || cachedDemographicNotePks.length == 0) return new ArrayList<CachedDemographicNote>();

		StringBuilder sb = new StringBuilder();
		for (CachedDemographicNoteCompositePk pk : cachedDemographicNotePks)
		{
			if (sb.length() > 0) sb.append(" OR ");
			sb.append("(x.integratorFacilityId=" + pk.getIntegratorFacilityId() + " AND x.uuid='" + pk.getUuid() + "')");
		}
		String whereClause = (sb.length() > 0) ? " WHERE " + sb.toString() : "";
		String sqlCommand = "select * from " + modelClass.getSimpleName() + " x " + whereClause;

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> results = query.getResultList();
		return(results);
	}

	/**
	 * This method will get notes for the given demographic only, non linked.
	 */
	public List<CachedDemographicNote> findByDemographicMetaData(FacilityIdIntegerCompositePk cachedDemographicPk)
	{
		String sqlCommand = "select new org.oscarehr.caisi_integrator.dao.CachedDemographicNote(n.cachedDemographicNoteCompositePk.integratorFacilityId, n.cachedDemographicNoteCompositePk.uuid, n.caisiDemographicId, n.caisiProgramId, n.observationCaisiProviderId, n.observationDate, n.role) from " + modelClass.getSimpleName() + " n where n.cachedDemographicNoteCompositePk.integratorFacilityId=?1 and n.caisiDemographicId=?2";

		Query query = entityManager.createQuery(sqlCommand, modelClass);
		query.setParameter(1, cachedDemographicPk.getIntegratorFacilityId());
		query.setParameter(2, cachedDemographicPk.getCaisiItemId());

		@SuppressWarnings("unchecked")
		List<CachedDemographicNote> results = query.getResultList();
		return(results);
	}
}
