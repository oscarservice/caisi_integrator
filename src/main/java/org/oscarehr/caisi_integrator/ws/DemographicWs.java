package org.oscarehr.caisi_integrator.ws;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.CachedAdmission;
import org.oscarehr.caisi_integrator.dao.CachedAdmissionDao;
import org.oscarehr.caisi_integrator.dao.CachedAppointment;
import org.oscarehr.caisi_integrator.dao.CachedAppointmentDao;
import org.oscarehr.caisi_integrator.dao.CachedBillingOnItem;
import org.oscarehr.caisi_integrator.dao.CachedBillingOnItemDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographic;
import org.oscarehr.caisi_integrator.dao.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.dao.CachedDemographicAllergyDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsent;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsent.ConsentStatus;
import org.oscarehr.caisi_integrator.dao.CachedDemographicConsentDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDocumentContents;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDocumentContentsDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDocumentDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDrugDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicForm;
import org.oscarehr.caisi_integrator.dao.CachedDemographicFormDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicImage;
import org.oscarehr.caisi_integrator.dao.CachedDemographicImageDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssueDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicLabResult;
import org.oscarehr.caisi_integrator.dao.CachedDemographicLabResultDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNote;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNoteCompositePk;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNoteDao;
import org.oscarehr.caisi_integrator.dao.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.dao.CachedDemographicPreventionDao;
import org.oscarehr.caisi_integrator.dao.CachedDxresearch;
import org.oscarehr.caisi_integrator.dao.CachedDxresearchDao;
import org.oscarehr.caisi_integrator.dao.CachedEformData;
import org.oscarehr.caisi_integrator.dao.CachedEformDataDao;
import org.oscarehr.caisi_integrator.dao.CachedEformValue;
import org.oscarehr.caisi_integrator.dao.CachedEformValueDao;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.CachedMeasurement;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementDao;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementExt;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementExtDao;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementMap;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementMapDao;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementType;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementTypeDao;
import org.oscarehr.caisi_integrator.dao.DemographicLink;
import org.oscarehr.caisi_integrator.dao.DemographicLinkDao;
import org.oscarehr.caisi_integrator.dao.DemographicPushDate;
import org.oscarehr.caisi_integrator.dao.DemographicPushDateDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.FacilityIdLabResultCompositePk;
import org.oscarehr.caisi_integrator.dao.MatchingCachedDemographicScore;
import org.oscarehr.caisi_integrator.util.CodeType;
import org.oscarehr.caisi_integrator.util.ConsentDomainUtil;
import org.oscarehr.caisi_integrator.util.ConsentDomainUtil.CodeGroup;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.ws.GetConsentTransfer.ConsentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class DemographicWs extends AbstractWs
{
	private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	private CachedFacilityDao cachedFacilityDao;

	@Autowired
	private CachedDemographicDao cachedDemographicDao;

	@Autowired
	private CachedDemographicConsentDao cachedDemographicConsentDao;

	@Autowired
	private CachedDemographicIssueDao cachedDemographicIssueDao;

	@Autowired
	private CachedDemographicImageDao cachedDemographicImageDao;

	@Autowired
	private CachedDemographicPreventionDao cachedDemographicPreventionDao;

	@Autowired
	private CachedDemographicDrugDao cachedDemographicDrugDao;

	@Autowired
	private CachedDemographicNoteDao cachedDemographicNoteDao;

	@Autowired
	private DemographicLinkDao demographicLinkDao;

	@Autowired
	private DemographicPushDateDao demographicPushDateDao;

	@Autowired
	private CachedAdmissionDao cachedAdmissionDao;

	@Autowired
	private CachedAppointmentDao cachedAppointmentDao;

	@Autowired
	private CachedMeasurementDao cachedMeasurementDao;

	@Autowired
	private CachedMeasurementExtDao cachedMeasurementExtDao;

	@Autowired
	private CachedMeasurementTypeDao cachedMeasurementTypeDao;

	@Autowired
	private CachedMeasurementMapDao cachedMeasurementMapDao;

	@Autowired
	private CachedDxresearchDao cachedDxresearchDao;

	@Autowired
	private CachedBillingOnItemDao cachedBillingOnItemDao;

	@Autowired
	private CachedEformDataDao cachedEformDataDao;

	@Autowired
	private CachedEformValueDao cachedEformValueDao;

	@Autowired
	private CachedDemographicAllergyDao cachedDemographicAllergyDao;

	@Autowired
	private CachedDemographicDocumentDao cachedDemographicDocumentDao;

	@Autowired
	private CachedDemographicDocumentContentsDao cachedDemographicDocumentContentsDao;

	@Autowired
	private CachedDemographicFormDao cachedDemographicFormDao;

	@Autowired
	private CachedDemographicLabResultDao cachedDemographicLabResultDao;

	/**
	 * When calling this method, the client doesn't need to fill in the facility ID, it will be sorted out by the server.
	 * 
	 * @param cachedDemographic
	 */
	public void setDemographic(DemographicTransfer demographicTransfer)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			// ignore what they pass us as it's unreliable.
			demographicTransfer.setIntegratorFacilityId(loggedInFacility.getId());

			saveCaisiCachedDemographic(demographicTransfer);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	private void saveCaisiCachedDemographic(DemographicTransfer demographicTransfer) throws IllegalAccessException, InvocationTargetException
	{
		FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(demographicTransfer.getIntegratorFacilityId());
		pk.setCaisiItemId(demographicTransfer.getCaisiDemographicId());

		saveBasicDemographicInfo(demographicTransfer, pk);
		saveDemographicImage(demographicTransfer, pk);
	}

	private void saveDemographicImage(DemographicTransfer demographicTransfer, FacilityIdIntegerCompositePk pk)
	{
		if (demographicTransfer.getPhoto() != null)
		{
			CachedDemographicImage cachedDemographicImage = cachedDemographicImageDao.find(pk);

			if (cachedDemographicImage == null) // it's a new entry
			{
				cachedDemographicImage = new CachedDemographicImage();

				cachedDemographicImage.setFacilityIdIntegerCompositePk(pk);
				cachedDemographicImage.setUpdateDate(demographicTransfer.getPhotoUpdateDate());
				cachedDemographicImage.setImage(demographicTransfer.getPhoto());

				cachedDemographicImageDao.persist(cachedDemographicImage);
			}
			else
			// it's an update to an existing entry
			{
				cachedDemographicImage.setUpdateDate(demographicTransfer.getPhotoUpdateDate());
				cachedDemographicImage.setImage(demographicTransfer.getPhoto());
				cachedDemographicImageDao.merge(cachedDemographicImage);
			}
		}
	}

	private void saveBasicDemographicInfo(DemographicTransfer demographicTransfer, FacilityIdIntegerCompositePk pk) throws IllegalAccessException, InvocationTargetException
	{
		CachedDemographic cachedDemographic = cachedDemographicDao.find(pk);

		if (cachedDemographic == null) // it's a new entry
		{
			cachedDemographic = new CachedDemographic();

			BeanUtils.copyProperties(cachedDemographic, demographicTransfer);
			cachedDemographic.setFacilityIdIntegerCompositePk(pk);
			if (demographicTransfer.getRemoveId())
			{
				cachedDemographicDao.makeHash(cachedDemographic);
			}
			cachedDemographicDao.persist(cachedDemographic);
		}
		else
		// it's an update to an existing entry
		{
			BeanUtils.copyProperties(cachedDemographic, demographicTransfer);
			if (demographicTransfer.getRemoveId())
			{
				cachedDemographicDao.makeHash(cachedDemographic);
			}
			cachedDemographicDao.merge(cachedDemographic);
		}
	}

	public ArrayList<MatchingDemographicTransferScore> getMatchingDemographics(MatchingDemographicParameters parameters)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<MatchingCachedDemographicScore> results = cachedDemographicDao.findMatchingDemographics(loggedInFacility.getId(), parameters.maxEntriesToReturn, parameters.minScore, parameters.firstName, parameters.lastName, parameters.birthDate, parameters.hin, null, null, null, null);

			ArrayList<MatchingDemographicTransferScore> allowedResults = new ArrayList<MatchingDemographicTransferScore>();
			for (MatchingCachedDemographicScore entry : results)
			{
				// As per request, there should be no filtering on search/individual results, HIC/Consent/PHI rules are all ignored.
				// if (!PhiFilter.isAllowed(entry.cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), null)) continue;

				CachedDemographic cachedDemographic = entry.cachedDemographic;

				MatchingDemographicTransferScore demographicTransferScore = new MatchingDemographicTransferScore();
				demographicTransferScore.demographicTransfer = makeDemographicTransfer(cachedDemographic);
				demographicTransferScore.score = entry.score;

				allowedResults.add(demographicTransferScore);
			}

			// log search results
			for (MatchingDemographicTransferScore allowedEntry : allowedResults)
			{
				logSearchDemographicTransfer(allowedEntry.demographicTransfer);
			}

			return(allowedResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	private void logSearchDemographicTransfer(DemographicTransfer demographicTransfer)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(demographicTransfer.getClass().getSimpleName());
		sb.append(MiscUtils.ID_SEPARATOR);
		sb.append(demographicTransfer.getIntegratorFacilityId());
		sb.append(MiscUtils.ID_SEPARATOR);
		sb.append(demographicTransfer.getCaisiDemographicId());

		eventLogDao.createDataSearchEventEntry(sb.toString());
	}

	public DemographicTransfer getDemographicByFacilityIdAndDemographicId(@WebParam(name = "integratorFacilityId") Integer integratorFacilityId, @WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setIntegratorFacilityId(integratorFacilityId);
			pk.setCaisiItemId(caisiDemographicId);
			CachedDemographic cachedDemographic = cachedDemographicDao.find(pk);

			// As per request, there should be no filtering on search/individual results, HIC/Consent/PHI rules are all ignored.
			// if (!PhiFilter.isAllowed(cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), null)) return(null);

			DemographicTransfer demographicTransfer = makeDemographicTransfer(cachedDemographic);

			eventLogDao.createDataReadEventEntry(cachedDemographic);
			return(demographicTransfer);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	private DemographicTransfer makeDemographicTransfer(CachedDemographic cachedDemographic) throws IllegalAccessException, InvocationTargetException
	{
		if (cachedDemographic == null) return(null);

		DemographicTransfer demographicTransfer = new DemographicTransfer();
		BeanUtils.copyProperties(demographicTransfer, cachedDemographic);
		demographicTransfer.setIntegratorFacilityId(cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		demographicTransfer.setCaisiDemographicId(cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId());

		CachedDemographicImage cachedDemographicImage = cachedDemographicImageDao.find(cachedDemographic.getId());
		if (cachedDemographicImage != null)
		{
			demographicTransfer.setPhotoUpdateDate(cachedDemographicImage.getUpdateDate());
			demographicTransfer.setPhoto(cachedDemographicImage.getImage());
		}
		return demographicTransfer;
	}

	/**
	 * Although technically speaking you could call this method with every single issue in the system... it's probably better to call it once per demographic
	 * that way it's broken up into smaller requests.
	 */
	public void setCachedDemographicIssues(CachedDemographicIssue[] issues)
	{
		if (issues == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedDemographicIssue issue : issues)
			{
				issue.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedDemographicIssueDao.replace(issue);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public void deleteCachedDemographicIssues(Integer demographicNo, FacilityIdDemographicIssueCompositePk[] cachedDemographicIssuesIds)
	{
		Facility loggedInFacility = getLoggedInFacility();
		List<CachedDemographicIssue> previousDemographicIssues = cachedDemographicIssueDao.findByFacilityAndDemographicId(loggedInFacility.getId(), demographicNo);

		if (cachedDemographicIssuesIds != null && cachedDemographicIssuesIds.length > 0)
		{

			List<FacilityIdDemographicIssueCompositePk> vals = Arrays.asList(cachedDemographicIssuesIds);

			for (CachedDemographicIssue x : previousDemographicIssues)
			{
				//if we have something that's not in the list, we need to delete it

				if (!vals.contains(x.getFacilityDemographicIssuePk()))
				{
					cachedDemographicIssueDao.remove(x.getFacilityDemographicIssuePk());
					logger.debug("Removed Demographic Issue " + x.getFacilityDemographicIssuePk().getCaisiDemographicId() + "," + x.getFacilityDemographicIssuePk().getCodeType() + "," + x.getFacilityDemographicIssuePk().getIssueCode() + " from Facility " + x.getFacilityDemographicIssuePk().getIntegratorFacilityId());
				}

			}
		}
		else
		{
			//remove all
			for (CachedDemographicIssue x : previousDemographicIssues)
			{
				cachedDemographicIssueDao.remove(x.getFacilityDemographicIssuePk());
			}
		}
	}

	public ArrayList<CachedDemographicIssue> getLinkedCachedDemographicIssuesByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<CachedDemographicIssue> results = new ArrayList<CachedDemographicIssue>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk tempPk : linkedIds)
				{
					// skip if no consent
					if (!isAllowedToRead(cachedDemographicConsent, tempPk.getIntegratorFacilityId())) continue;

					List<CachedDemographicIssue> tempResults = cachedDemographicIssueDao.findByFacilityAndDemographicId(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId());
					for (CachedDemographicIssue demographicIssue : tempResults)
					{
						String debugIssueCode = demographicIssue.getFacilityDemographicIssuePk().getIssueCode();
						logger.debug("Found issue : " + debugIssueCode);

						if (!PhiFilter.isAllowedForRole(tempPk.getIntegratorFacilityId(), demographicIssue.getIssueRole()))
						{
							logger.debug("PHI denies issue : " + debugIssueCode);
							continue;
						}

						// check mental health consent
						if (requireConsentToRead && (cachedDemographicConsent == null || cachedDemographicConsent.isExcludeMentalHealthData()))
						{
							if (ConsentDomainUtil.isInDomain(demographicIssue.getFacilityDemographicIssuePk().getCodeType(), CodeGroup.MENTAL_HEALTH, demographicIssue.getFacilityDemographicIssuePk().getIssueCode()))
							{
								logger.debug("Mental Health denies issue " + debugIssueCode + ", it is mental health and no consent given");
								continue;
							}
						}

						logger.debug("Allowing issue : " + debugIssueCode);
						results.add(demographicIssue);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(results);
			return(results);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	/**
	 * Although technically speaking you could call this method with every single issue in the system... it's probably better to call it once per demographic
	 * that way it's broken up into smaller requests.
	 */
	public void setCachedDemographicNotes(CachedDemographicNote[] notes)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedDemographicNote note : notes)
			{
				note.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedDemographicNoteDao.replace(note);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	/**
	 * Get all notes from clients linked to this client, this method will
	 * exclude notes from the caller himself.
	 */
	public List<CachedDemographicNote> getLinkedCachedDemographicNotes(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<CachedDemographicNote> notes = new ArrayList<CachedDemographicNote>();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			Set<FacilityIdIntegerCompositePk> linked = demographicLinkDao.getAllLinkedDemographics(pk, true);
			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk linkedDemographicPk : linked)
				{
					// skip if no consent
					Boolean consent = isAllowedToRead(cachedDemographicConsent, linkedDemographicPk.getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + linkedDemographicPk.getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					List<CachedDemographicNote> tempNotes = cachedDemographicNoteDao.findByDemographic(linkedDemographicPk);

					for (CachedDemographicNote note : tempNotes)
					{
						boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(linkedDemographicPk.getIntegratorFacilityId(), note.getObservationCaisiProviderId());
						logger.debug("PhiFilter.isAllowed : " + note.getCachedDemographicNoteCompositePk().toString() + " : " + phiIsAllowed);

						if (!phiIsAllowed) continue;

						notes.add(note);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(notes);
			return(notes);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	/**
	 * Get all notes (except note data field) from clients linked to this client, this method will
	 * exclude notes from the caller himself.
	 */
	public List<CachedDemographicNote> getLinkedCachedDemographicNoteMetaData(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			List<CachedDemographicNote> notes = new ArrayList<CachedDemographicNote>();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			Set<FacilityIdIntegerCompositePk> linked = demographicLinkDao.getAllLinkedDemographics(pk, true);
			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk linkedDemographicPk : linked)
				{
					// skip if no consent
					Boolean consent = isAllowedToRead(cachedDemographicConsent, linkedDemographicPk.getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + linkedDemographicPk.getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					List<CachedDemographicNote> tempNotes = cachedDemographicNoteDao.findByDemographicMetaData(linkedDemographicPk);

					for (CachedDemographicNote note : tempNotes)
					{
						boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(linkedDemographicPk.getIntegratorFacilityId(), note.getObservationCaisiProviderId());
						logger.debug("PhiFilter.isAllowed : " + note.getCachedDemographicNoteCompositePk().toString() + " : " + phiIsAllowed);

						if (!phiIsAllowed) continue;

						notes.add(note);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(notes);
			return(notes);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	/**
	 * Get notes using a list of pks
	 */
	public List<CachedDemographicNote> getLinkedCachedDemographicNotesByIds(@WebParam(name = "cachedDemographicNoteCompositePk") CachedDemographicNoteCompositePk[] cachedDemographicNoteCompositePk)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			List<CachedDemographicNote> notes = new ArrayList<CachedDemographicNote>();
			List<CachedDemographicNote> filteredNotes = new ArrayList<CachedDemographicNote>();

			notes = cachedDemographicNoteDao.findByCompositeKeys(cachedDemographicNoteCompositePk);
			for (CachedDemographicNote note : notes)
			{
				FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), note.getCaisiDemographicId());
				CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);
				if (isConsentGivenIfNeeded(cachedDemographicConsent))
				{
					Boolean consent = isAllowedToRead(cachedDemographicConsent, note.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + note.getCachedDemographicNoteCompositePk().getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(note.getCachedDemographicNoteCompositePk().getIntegratorFacilityId(), note.getObservationCaisiProviderId());
					logger.debug("PhiFilter.isAllowed : " + note.getCachedDemographicNoteCompositePk().toString() + " : " + phiIsAllowed);

					if (!phiIsAllowed) continue;

					filteredNotes.add(note);
				}
			}
			eventLogDao.createDataReadEventEntries(filteredNotes);
			return(filteredNotes);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	/**
	 * Get all appointments from clients linked to this client, this method will
	 * exclude appointments from the caller himself.
	 */
	public List<CachedAppointment> getLinkedCachedAppointments(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<CachedAppointment> appointments = new ArrayList<CachedAppointment>();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			Set<FacilityIdIntegerCompositePk> linked = demographicLinkDao.getAllLinkedDemographics(pk, true);
			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk linkedDemographicPk : linked)
				{
					// skip if no consent
					Boolean consent = isAllowedToRead(cachedDemographicConsent, linkedDemographicPk.getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + linkedDemographicPk.getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					List<CachedAppointment> tempAppointments = cachedAppointmentDao.findByFacilityAndDemographicId(linkedDemographicPk.getIntegratorFacilityId(), linkedDemographicPk.getCaisiItemId());

					for (CachedAppointment appointment : tempAppointments)
					{
						boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(linkedDemographicPk.getIntegratorFacilityId(), appointment.getCaisiProviderId());
						logger.debug("PhiFilter.isAllowed : " + appointment.getFacilityIdIntegerCompositePk().toString() + " : " + phiIsAllowed);

						if (!phiIsAllowed) continue;

						appointments.add(appointment);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(appointments);
			return(appointments);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	/**
	 * Although technically speaking you could call this method with every single prevention in the system... it's probably better to call it once per demographic
	 * that way it's broken up into smaller requests.
	 */
	public void setCachedDemographicPreventions(CachedDemographicPrevention[] preventions)
	{
		if (preventions == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedDemographicPrevention prevention : preventions)
			{
				prevention.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedDemographicPreventionDao.replace(prevention);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public void deleteCachedDemographicPreventions(Integer demographicNo, Integer[] cachedDemographicPreventionIds)
	{
		Facility loggedInFacility = getLoggedInFacility();
		List<CachedDemographicPrevention> previousDemographicPreventions = cachedDemographicPreventionDao.findByFacilityIdAndDemographicId(loggedInFacility.getId(), demographicNo);

		if (cachedDemographicPreventionIds != null && cachedDemographicPreventionIds.length > 0)
		{

			List<Integer> vals = Arrays.asList(cachedDemographicPreventionIds);

			for (CachedDemographicPrevention x : previousDemographicPreventions)
			{
				//if we have something that's not in the list, we need to delete it

				if (!vals.contains(x.getFacilityPreventionPk().getCaisiItemId()))
				{
					cachedDemographicPreventionDao.remove(x.getId());
					logger.debug("Removed Demographic Prevention " + x.getId());
				}

			}
		}
		else
		{
			//remove all
			for (CachedDemographicPrevention x : previousDemographicPreventions)
			{
				cachedDemographicIssueDao.remove(x.getFacilityPreventionPk());
			}
		}
	}

	public void linkDemographics(@WebParam(name = "creatorCaisiProviderId") String creatorCaisiProviderId, @WebParam(name = "caisiDemographicIdAtCurrentFacility") Integer caisiDemographicIdAtCurrentFacility, @WebParam(name = "integratorDemographicFacilityIdOnIntegrator") Integer integratorDemographicFacilityIdOnIntegrator, @WebParam(name = "caisiDemographicIdOnIntegrator") Integer caisiDemographicIdOnIntegrator)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			// check to see if already linked
			FacilityIdIntegerCompositePk pk1 = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicIdAtCurrentFacility);
			FacilityIdIntegerCompositePk pk2 = new FacilityIdIntegerCompositePk(integratorDemographicFacilityIdOnIntegrator, caisiDemographicIdOnIntegrator);
			DemographicLink demographicLink = demographicLinkDao.findByPair(pk1, pk2);

			// if already linked, do nothing
			if (demographicLink != null) return;

			// if not currently linked, link them
			demographicLink = new DemographicLink();
			demographicLink.setCaisiDemographicId1(caisiDemographicIdAtCurrentFacility);
			demographicLink.setCaisiDemographicId2(caisiDemographicIdOnIntegrator);
			demographicLink.setCreatorCaisiProviderId(creatorCaisiProviderId);
			demographicLink.setCreatorIntegratorProviderFacilityId(loggedInFacility.getId());
			demographicLink.setIntegratorDemographicFacilityId1(loggedInFacility.getId());
			demographicLink.setIntegratorDemographicFacilityId2(integratorDemographicFacilityIdOnIntegrator);

			demographicLinkDao.persist(demographicLink);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public ArrayList<CachedDemographicPrevention> getLinkedCachedDemographicPreventionsByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<CachedDemographicPrevention> filteredResults = new ArrayList<CachedDemographicPrevention>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			for (FacilityIdIntegerCompositePk tempPk : linkedIds)
			{
				List<CachedDemographicPrevention> tempResults = cachedDemographicPreventionDao.findByFacilityIdAndDemographicId(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId());

				for (CachedDemographicPrevention cachedDemographicPrevention : tempResults)
				{
					if (!PhiFilter.isAllowedFromProvider(tempPk.getIntegratorFacilityId(), cachedDemographicPrevention.getCaisiProviderId())) continue;

					if (checkPreventionConsent(cachedDemographicConsent, cachedDemographicPrevention)) filteredResults.add(cachedDemographicPrevention);
				}
			}

			eventLogDao.createDataReadEventEntries(filteredResults);
			return(filteredResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public CachedDemographicPrevention getCachedDemographicPreventionsByPreventionId(@WebParam(name = "preventionId") FacilityIdIntegerCompositePk preventionId)
	{
		try
		{
			// get the prevention
			CachedDemographicPrevention cachedDemographicPrevention = cachedDemographicPreventionDao.find(preventionId);

			FacilityIdIntegerCompositePk demographicPk = new FacilityIdIntegerCompositePk();
			demographicPk.setIntegratorFacilityId(cachedDemographicPrevention.getId().getIntegratorFacilityId());
			demographicPk.setCaisiItemId(cachedDemographicPrevention.getCaisiDemographicId());
			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(demographicPk);

			if (!PhiFilter.isAllowedFromProvider(demographicPk.getIntegratorFacilityId(), cachedDemographicPrevention.getCaisiProviderId())) return(null);

			// check the demographics consent
			if (checkPreventionConsent(cachedDemographicConsent, cachedDemographicPrevention))
			{
				eventLogDao.createDataReadEventEntry(cachedDemographicPrevention);
				return(cachedDemographicPrevention);
			}
			else
			{
				return(null);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	/**
	 * @return true if allowed to read
	 */
	private boolean checkPreventionConsent(CachedDemographicConsent cachedDemographicConsent, CachedDemographicPrevention cachedDemographicPrevention)
	{
		if (!requireConsentToRead) return(true);

		boolean allowed = false;
		if (cachedDemographicConsent != null)
		{
			allowed = cachedDemographicConsent.allowedToShareData(cachedDemographicPrevention.getFacilityPreventionPk().getIntegratorFacilityId());

			// if mental health issue
			if (ConsentDomainUtil.isInDomain(CodeType.PREVENTION, CodeGroup.MENTAL_HEALTH, cachedDemographicPrevention.getPreventionType()))
			{
				allowed = allowed && !cachedDemographicConsent.isExcludeMentalHealthData();
			}
		}

		return(allowed);
	}

	public ArrayList<CachedMeasurement> getLinkedCachedDemographicMeasurementByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<CachedMeasurement> filteredResults = new ArrayList<CachedMeasurement>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			for (FacilityIdIntegerCompositePk tempPk : linkedIds)
			{
				Boolean consent = isAllowedToRead(cachedDemographicConsent, tempPk.getIntegratorFacilityId());
				logger.debug("cachedDemographicConsent.allowedToShareData : " + tempPk.getIntegratorFacilityId() + " : " + consent);
				if (!consent) continue;

				List<CachedMeasurement> tempResults = cachedMeasurementDao.findByFacilityAndDemographicId(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId());

				for (CachedMeasurement cachedMeasurement : tempResults)
				{
					if (!PhiFilter.isAllowedFromProvider(tempPk.getIntegratorFacilityId(), cachedMeasurement.getCaisiProviderId())) continue;

					filteredResults.add(cachedMeasurement);
				}
			}

			eventLogDao.createDataReadEventEntries(filteredResults);
			return(filteredResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public ArrayList<DemographicTransfer> getDirectlyLinkedDemographicsByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getDirectlyLinkedDemographics(pk, null);
			ArrayList<DemographicTransfer> demographicTransfers = new ArrayList<DemographicTransfer>();
			for (FacilityIdIntegerCompositePk linkedId : linkedIds)
			{
				// As per request, there should be no filtering on search/individual results, HIC/Consent/PHI rules are all ignored.
				// if (!PhiFilter.isAllowed(linkedId.getIntegratorFacilityId(), null)) continue;

				CachedDemographic cachedDemographic = cachedDemographicDao.find(linkedId);
				demographicTransfers.add(makeDemographicTransfer(cachedDemographic));
			}

			// log search
			for (DemographicTransfer demographicTransfer : demographicTransfers)
			{
				logSearchDemographicTransfer(demographicTransfer);
			}
			return(demographicTransfers);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public ArrayList<DemographicTransfer> getLinkedDemographicsByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<DemographicTransfer> demographicTransfers = new ArrayList<DemographicTransfer>();
			for (FacilityIdIntegerCompositePk linkedId : linkedIds)
			{
				// As per request, there should be no filtering on search/individual results, HIC/Consent/PHI rules are all ignored.
				// if (!PhiFilter.isAllowed(linkedId.getIntegratorFacilityId(), null)) continue;

				CachedDemographic cachedDemographic = cachedDemographicDao.find(linkedId);
				demographicTransfers.add(makeDemographicTransfer(cachedDemographic));
			}

			// log search
			for (DemographicTransfer demographicTransfer : demographicTransfers)
			{
				logSearchDemographicTransfer(demographicTransfer);
			}
			return(demographicTransfers);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public void unLinkDemographics(@WebParam(name = "caisiDemographicIdAtCurrentFacility") Integer caisiDemographicIdAtCurrentFacility, @WebParam(name = "integratorDemographicFacilityIdOnIntegrator") Integer integratorDemographicFacilityIdOnIntegrator, @WebParam(name = "caisiDemographicIdOnIntegrator") Integer caisiDemographicIdOnIntegrator)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			// find the link
			FacilityIdIntegerCompositePk pk1 = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicIdAtCurrentFacility);
			FacilityIdIntegerCompositePk pk2 = new FacilityIdIntegerCompositePk(integratorDemographicFacilityIdOnIntegrator, caisiDemographicIdOnIntegrator);
			DemographicLink demographicLink = demographicLinkDao.findByPair(pk1, pk2);

			if (demographicLink != null) demographicLinkDao.remove(demographicLink.getId());
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public void setCachedDemographicConsent(@WebParam(name = "consentTransfer") SetConsentTransfer consentTransfer)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), consentTransfer.demographicId);

			if (cachedDemographicConsentDao.find(pk) != null) cachedDemographicConsentDao.remove(pk);

			CachedDemographicConsent cachedDemographicConsent = new CachedDemographicConsent();
			cachedDemographicConsent.setClientConsentStatus(ConsentStatus.valueOf(consentTransfer.consentStatus));
			cachedDemographicConsent.setCreatedDate(consentTransfer.createdDate);
			cachedDemographicConsent.setExcludeMentalHealthData(consentTransfer.excludeMentalHealthData);
			cachedDemographicConsent.setExpiry(consentTransfer.expiry);
			cachedDemographicConsent.setFacilityDemographicPk(pk);
			for (SetConsentTransfer.FacilityConsentPair pair : consentTransfer.consentToShareData)
			{
				cachedDemographicConsent.getConsentToShareData().put(pair.remoteFacilityId, pair.shareData);
			}

			cachedDemographicConsentDao.persist(cachedDemographicConsent);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public GetConsentTransfer getConsentState(@WebParam(name = "integratorFacilityAndDemographicId") FacilityIdIntegerCompositePk integratorFacilityAndDemographicId)
	{
		try
		{
			CachedDemographicConsent consent = cachedDemographicConsentDao.findLatestConsentFromAllLinkedClients(integratorFacilityAndDemographicId);
			if (consent == null) return(null);

			GetConsentTransfer result = new GetConsentTransfer();
			result.consentDate = consent.getCreatedDate();
			result.integratorFacilityId = consent.getFacilityDemographicPk().getIntegratorFacilityId();

			// sort out the actual state... this is a mess
			// easiest way is to check for all or check for none, else it's "some"
			// 1) check for all
			List<CachedFacility> cachedFacilities = cachedFacilityDao.findAll();
			if (!consent.isExcludeMentalHealthData() && consent.getClientConsentStatus() == ConsentStatus.GIVEN && (consent.getExpiry() == null || consent.getExpiry().after(new Date())))
			{
				boolean allAreTrue = true;
				for (CachedFacility cacheFacility : cachedFacilities)
				{
					if (!consent.allowedToShareData(cacheFacility.getIntegratorFacilityId()))
					{
						allAreTrue = false;
						break;
					}
				}

				if (allAreTrue) result.consentState = ConsentState.ALL;
			}
			// check for none
			if (result.consentState == null && (consent.getClientConsentStatus() == ConsentStatus.REVOKED || (consent.getExpiry() != null && consent.getExpiry().before(new Date()))))
			{
				result.consentState = ConsentState.NONE;
			}
			// else it's "some"
			if (result.consentState == null) result.consentState = ConsentState.SOME;

			// log read
			StringBuilder sb = new StringBuilder();
			sb.append(result.getClass().getSimpleName());
			sb.append(MiscUtils.ID_SEPARATOR);
			sb.append(consent.getId().toString());
			eventLogDao.createDataSearchEventEntry(sb.toString());

			return(result);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	/**
	 * Although technically speaking you could call this method with every single drug in the system... it's probably better to call it once per demographic
	 * that way it's broken up into smaller requests.
	 */
	public void setCachedDemographicDrugs(CachedDemographicDrug[] drugs)
	{
		if (drugs == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedDemographicDrug drug : drugs)
			{
				drug.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedDemographicDrugDao.replace(drug);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public ArrayList<CachedDemographicDrug> getLinkedCachedDemographicDrugsByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<CachedDemographicDrug> filteredResults = new ArrayList<CachedDemographicDrug>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			for (FacilityIdIntegerCompositePk tempPk : linkedIds)
			{
				List<CachedDemographicDrug> tempResults = cachedDemographicDrugDao.findByFacilityAndDemographicId(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId());

				for (CachedDemographicDrug cachedDemographicDrug : tempResults)
				{
					if (!PhiFilter.isAllowedFromProvider(tempPk.getIntegratorFacilityId(), cachedDemographicDrug.getCaisiProviderId())) continue;

					if (checkDrugConsent(cachedDemographicConsent, cachedDemographicDrug)) filteredResults.add(cachedDemographicDrug);
				}
			}

			eventLogDao.createDataReadEventEntries(filteredResults);
			return(filteredResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	private boolean checkDrugConsent(CachedDemographicConsent cachedDemographicConsent, CachedDemographicDrug cachedDemographicDrug)
	{
		if (!requireConsentToRead) return(true);

		if (cachedDemographicConsent == null) return(false);

		if (!cachedDemographicConsent.allowedToShareData(cachedDemographicDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId())) return(false);

		// if mental health issue
		if (ConsentDomainUtil.isInDomain(CodeType.DRUG, CodeGroup.MENTAL_HEALTH, cachedDemographicDrug.getRegionalIdentifier()))
		{
			if (!cachedDemographicConsent.isExcludeMentalHealthData()) return(false);
		}

		return(true);
	}

	public void setCachedAdmissions(CachedAdmission[] cachedAdmissions)
	{
		if (cachedAdmissions == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedAdmission admission : cachedAdmissions)
			{
				admission.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedAdmissionDao.replace(admission);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public ArrayList<CachedAdmission> getLinkedCachedAdmissionsByDemographicId(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<CachedAdmission> filteredResults = new ArrayList<CachedAdmission>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			for (FacilityIdIntegerCompositePk tempPk : linkedIds)
			{
				List<CachedAdmission> tempResults = cachedAdmissionDao.findByFacilityIdAndDemographicId(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId());

				for (CachedAdmission cachedAdmission : tempResults)
				{
					// as per request, all admissions are viewable by everyone
					// if (!PhiFilter.isAllowed(tempPk.getIntegratorFacilityId(), null)) continue;

					if (isAllowedToRead(cachedDemographicConsent, tempPk.getIntegratorFacilityId()))
					{
						filteredResults.add(cachedAdmission);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(filteredResults);
			return(filteredResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public void setCachedAppointments(CachedAppointment[] cachedAppointments)
	{
		if (cachedAppointments == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedAppointment appointment : cachedAppointments)
			{
				appointment.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedAppointmentDao.replace(appointment);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public void setCachedMeasurements(CachedMeasurement[] cachedMeasurements)
	{
		if (cachedMeasurements == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedMeasurement measurement : cachedMeasurements)
			{
				measurement.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedMeasurementDao.replace(measurement);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedMeasurementExts(CachedMeasurementExt[] cachedMeasurementExts)
	{
		if (cachedMeasurementExts == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedMeasurementExt measurementExt : cachedMeasurementExts)
			{
				measurementExt.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedMeasurementExtDao.replace(measurementExt);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedMeasurementTypes(CachedMeasurementType[] cachedMeasurementTypes)
	{
		if (cachedMeasurementTypes == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedMeasurementType measurementType : cachedMeasurementTypes)
			{
				measurementType.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedMeasurementTypeDao.replace(measurementType);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedMeasurementMaps(CachedMeasurementMap[] cachedMeasurementMaps)
	{
		if (cachedMeasurementMaps == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedMeasurementMap measurementMap : cachedMeasurementMaps)
			{
				measurementMap.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedMeasurementMapDao.replace(measurementMap);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedDxresearch(CachedDxresearch[] cachedDxresearchs)
	{
		if (cachedDxresearchs == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedDxresearch dxresearch : cachedDxresearchs)
			{
				dxresearch.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedDxresearchDao.replace(dxresearch);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedBillingOnItem(CachedBillingOnItem[] cachedBillingOnItems)
	{
		if (cachedBillingOnItems == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedBillingOnItem billingOnItem : cachedBillingOnItems)
			{
				billingOnItem.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedBillingOnItemDao.replace(billingOnItem);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedEformData(CachedEformData[] cachedEformDatas)
	{
		if (cachedEformDatas == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedEformData eformData : cachedEformDatas)
			{
				eformData.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedEformDataDao.replace(eformData);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	public void setCachedEformValues(CachedEformValue[] cachedEformValues)
	{
		if (cachedEformValues == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedEformValue eformValue : cachedEformValues)
			{
				eformValue.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedEformValueDao.replace(eformValue);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}

	}

	/**
	 * Although technically speaking you could call this method with every single item in the system... it's probably better to call it once per demographic
	 * that way it's broken up into smaller requests.
	 */
	public void setCachedDemographicAllergies(CachedDemographicAllergy[] allergies)
	{
		if (allergies == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			for (CachedDemographicAllergy allergy : allergies)
			{
				allergy.getId().setIntegratorFacilityId(loggedInFacility.getId());
				cachedDemographicAllergyDao.replace(allergy);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	/**
	 * Get all allergies from clients linked to this client, this method will
	 * exclude allergies from the caller himself.
	 */
	public List<CachedDemographicAllergy> getLinkedCachedDemographicAllergies(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<CachedDemographicAllergy> allergies = new ArrayList<CachedDemographicAllergy>();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			Set<FacilityIdIntegerCompositePk> linked = demographicLinkDao.getAllLinkedDemographics(pk, true);
			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk linkedDemographicPk : linked)
				{
					// skip if no consent
					Boolean consent = isAllowedToRead(cachedDemographicConsent, linkedDemographicPk.getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + linkedDemographicPk.getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					List<CachedDemographicAllergy> tempAllergies = cachedDemographicAllergyDao.findByFacilityIdAndDemographicId(linkedDemographicPk.getIntegratorFacilityId(), linkedDemographicPk.getCaisiItemId());

					for (CachedDemographicAllergy allergy : tempAllergies)
					{
						// temporary commented out until oscar tracks the author of an allergy
						//						boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(linkedDemographicPk.getIntegratorFacilityId(), allergy.getObservationCaisiProviderId());
						//						logger.debug("PhiFilter.isAllowed : " + note.getCachedDemographicNoteCompositePk().toString() + " : " + phiIsAllowed);
						//
						//						if (!phiIsAllowed) continue;

						allergies.add(allergy);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(allergies);
			return(allergies);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	/**
	 * lab results are large, so send one at a time instead of a whole list like other items.
	 */
	public void addCachedDemographicLabResult(CachedDemographicLabResult cachedDemographicLabResult)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();
			cachedDemographicLabResult.getId().setIntegratorFacilityId(loggedInFacility.getId());

			logger.debug("add lab : " + cachedDemographicLabResult);
			cachedDemographicLabResultDao.replace(cachedDemographicLabResult);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	/**
	 * Get all lab results from clients linked to this client, this method will
	 * exclude lab results from the caller himself.
	 */
	public List<CachedDemographicLabResult> getLinkedCachedDemographicLabResults(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<CachedDemographicLabResult> labResults = new ArrayList<CachedDemographicLabResult>();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);
			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			Set<FacilityIdIntegerCompositePk> linked = demographicLinkDao.getAllLinkedDemographics(pk, true);
			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk linkedDemographicPk : linked)
				{
					// skip if no consent
					Boolean consent = isAllowedToRead(cachedDemographicConsent, linkedDemographicPk.getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + linkedDemographicPk.getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					List<CachedDemographicLabResult> tempResults = cachedDemographicLabResultDao.findByFacilityIdAndDemographicId(linkedDemographicPk.getIntegratorFacilityId(), linkedDemographicPk.getCaisiItemId());

					for (CachedDemographicLabResult labResult : tempResults)
					{
						// temporary commented out, lab result currently does not support tracking provider ID, need to uncomment when oscar supports this 
						//						boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(linkedDemographicPk.getIntegratorFacilityId(), document.getObservationCaisiProviderId());
						//						logger.debug("PhiFilter.isAllowed : " + document.getCachedDemographicNoteCompositePk().toString() + " : " + phiIsAllowed);
						//
						//						if (!phiIsAllowed) continue;

						labResults.add(labResult);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(labResults);

			logger.debug("returning labResults size=" + labResults.size());

			return(labResults);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	public CachedDemographicLabResult getCachedDemographicLabResult(FacilityIdLabResultCompositePk pk)
	{
		try
		{
			CachedDemographicLabResult cachedDemographicLabResult = cachedDemographicLabResultDao.find(pk);
			logger.debug("retrieve lab pk=" + pk + ", results=" + cachedDemographicLabResult);

			FacilityIdIntegerCompositePk demographicPk = new FacilityIdIntegerCompositePk();
			demographicPk.setIntegratorFacilityId(pk.getIntegratorFacilityId());
			demographicPk.setCaisiItemId(cachedDemographicLabResult.getCaisiDemographicId());

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(demographicPk);

			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				Boolean consent = isAllowedToRead(cachedDemographicConsent, cachedDemographicLabResult.getFacilityIdLabResultCompositePk().getIntegratorFacilityId());

				if (consent)
				{
					eventLogDao.createDataReadEventEntry(cachedDemographicLabResult);
					return(cachedDemographicLabResult);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	/**
	 * Documents are large, so send one at a time instead of a whole list like other items.
	 */
	public void addCachedDemographicDocumentAndContents(CachedDemographicDocument cachedDemographicDocument, byte[] fileContents)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			cachedDemographicDocument.getId().setIntegratorFacilityId(loggedInFacility.getId());

			CachedDemographicDocumentContents cachedDemographicDocumentContents = new CachedDemographicDocumentContents();
			FacilityIdIntegerCompositePk contentsPk = new FacilityIdIntegerCompositePk();
			contentsPk.setIntegratorFacilityId(loggedInFacility.getId());
			contentsPk.setCaisiItemId(cachedDemographicDocument.getId().getCaisiItemId());

			cachedDemographicDocumentContents.setFacilityIntegerCompositePk(contentsPk);
			cachedDemographicDocumentContents.setFileContents(fileContents);

			cachedDemographicDocumentDao.replace(cachedDemographicDocument);
			cachedDemographicDocumentContentsDao.replace(cachedDemographicDocumentContents);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	/**
	 * Get all documents from clients linked to this client, this method will
	 * exclude documents from the caller himself.
	 */
	public List<CachedDemographicDocument> getLinkedCachedDemographicDocuments(@WebParam(name = "caisiDemographicId") Integer caisiDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			ArrayList<CachedDemographicDocument> documents = new ArrayList<CachedDemographicDocument>();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);
			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			Set<FacilityIdIntegerCompositePk> linked = demographicLinkDao.getAllLinkedDemographics(pk, true);
			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk linkedDemographicPk : linked)
				{
					// skip if no consent
					Boolean consent = isAllowedToRead(cachedDemographicConsent, linkedDemographicPk.getIntegratorFacilityId());
					logger.debug("cachedDemographicConsent.allowedToShareData : " + linkedDemographicPk.getIntegratorFacilityId() + " : " + consent);
					if (!consent) continue;

					List<CachedDemographicDocument> tempDocuments = cachedDemographicDocumentDao.findByFacilityIdAndDemographicId(linkedDemographicPk.getIntegratorFacilityId(), linkedDemographicPk.getCaisiItemId());

					for (CachedDemographicDocument document : tempDocuments)
					{
						// temporary commented out, document currently does not support tracking provider ID, need to uncomment when oscar supports this 
						//						boolean phiIsAllowed = PhiFilter.isAllowedFromProvider(linkedDemographicPk.getIntegratorFacilityId(), document.getObservationCaisiProviderId());
						//						logger.debug("PhiFilter.isAllowed : " + document.getCachedDemographicNoteCompositePk().toString() + " : " + phiIsAllowed);
						//
						//						if (!phiIsAllowed) continue;

						documents.add(document);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(documents);

			logger.debug("returning documents size=" + documents.size());

			return(documents);
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
			return(null);
		}
	}

	public CachedDemographicDocument getCachedDemographicDocument(FacilityIdIntegerCompositePk pk)
	{
		try
		{
			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				CachedDemographicDocument cachedDemographicDocument = cachedDemographicDocumentDao.find(pk);

				Boolean consent = isAllowedToRead(cachedDemographicConsent, cachedDemographicDocument.getFacilityIntegerPk().getIntegratorFacilityId());

				if (consent)
				{
					eventLogDao.createDataReadEventEntry(cachedDemographicDocument);
					return(cachedDemographicDocument);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	public CachedDemographicDocumentContents getCachedDemographicDocumentContents(FacilityIdIntegerCompositePk pk)
	{
		try
		{
			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				CachedDemographicDocumentContents cachedDemographicDocumentContents = cachedDemographicDocumentContentsDao.find(pk);

				Boolean consent = isAllowedToRead(cachedDemographicConsent, cachedDemographicDocumentContents.getFacilityIntegerCompositePk().getIntegratorFacilityId());

				if (consent)
				{
					eventLogDao.createDataReadEventEntry(cachedDemographicDocumentContents);
					return(cachedDemographicDocumentContents);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	public void addCachedDemographicForm(CachedDemographicForm form)
	{
		if (form == null) return;

		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			form.getId().setIntegratorFacilityId(loggedInFacility.getId());
			cachedDemographicFormDao.replace(form);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	public CachedDemographicForm getCachedDemographicForm(FacilityIdIntegerCompositePk pk)
	{
		try
		{
			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				CachedDemographicForm cachedDemographicForm = cachedDemographicFormDao.find(pk);

				Boolean consent = isAllowedToRead(cachedDemographicConsent, cachedDemographicForm.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());

				if (consent)
				{
					eventLogDao.createDataReadEventEntry(cachedDemographicForm);
					return(cachedDemographicForm);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected Error.", e);
		}

		return(null);
	}

	public ArrayList<CachedDemographicForm> getLinkedCachedDemographicForms(Integer caisiDemographicId, String formName)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk(loggedInFacility.getId(), caisiDemographicId);

			Set<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(pk, true);
			ArrayList<CachedDemographicForm> filteredResults = new ArrayList<CachedDemographicForm>();

			CachedDemographicConsent cachedDemographicConsent = getCachedDemographicConsentIfNeeded(pk);

			if (isConsentGivenIfNeeded(cachedDemographicConsent))
			{
				for (FacilityIdIntegerCompositePk tempPk : linkedIds)
				{
					List<CachedDemographicForm> tempResults = cachedDemographicFormDao.findByFacilityDemographicIdFormName(tempPk.getIntegratorFacilityId(), tempPk.getCaisiItemId(), formName);

					for (CachedDemographicForm cachedDemographicForm : tempResults)
					{
						if (!PhiFilter.isAllowedFromProvider(tempPk.getIntegratorFacilityId(), cachedDemographicForm.getCaisiProviderId())) continue;

						if (isAllowedToRead(cachedDemographicConsent, tempPk.getIntegratorFacilityId())) filteredResults.add(cachedDemographicForm);
					}
				}
			}

			eventLogDao.createDataReadEventEntries(filteredResults);
			return(filteredResults);
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public void setLastPushDate(int oscarDemographicId)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			FacilityIdIntegerCompositePk id = new FacilityIdIntegerCompositePk();
			id.setIntegratorFacilityId(loggedInFacility.getId());
			id.setCaisiItemId(oscarDemographicId);

			DemographicPushDate demographicPushDate = demographicPushDateDao.find(id);
			if (demographicPushDate != null)
			{
				demographicPushDate.setLastPushDate(new GregorianCalendar());
				demographicPushDateDao.merge(demographicPushDate);
			}
			else
			{
				demographicPushDate = new DemographicPushDate();
				demographicPushDate.setId(id);
				demographicPushDateDao.persist(demographicPushDate);
			}
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
		}
	}

	/**
	 * This gets a list of demographics which have been pushed after the given date.
	 * The list only returns demographics in the requesting facility.
	 * This method will return people who are linked even if they are not directly updated.
	 */
	public Integer[] getDemographicIdPushedAfterDateByRequestingFacility(Calendar afterThisDate)
	{
		try
		{
			Facility loggedInFacility = getLoggedInFacility();

			List<Integer> resultsToReturn = demographicPushDateDao.getUpdatedLinkedDemographicIdsFromFacilityAfterDate(loggedInFacility.getId(), afterThisDate);

			return(resultsToReturn.toArray(new Integer[0]));
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}

	public DemographicPushDate[] getDemographicsPushedAfterDate(Calendar afterThisDate)
	{
		try
		{
			List<DemographicPushDate> resultsToReturn = demographicPushDateDao.findAfterDate(afterThisDate);

			return(resultsToReturn.toArray(new DemographicPushDate[0]));
		}
		catch (Exception e)
		{
			logger.error("unexpected error", e);
			return(null);
		}
	}
}
