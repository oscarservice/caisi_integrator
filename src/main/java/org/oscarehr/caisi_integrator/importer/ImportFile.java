package org.oscarehr.caisi_integrator.importer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.ByteWrapper;
import org.oscarehr.PMmodule.caisi_integrator.DeleteCachedDemographicIssuesWrapper;
import org.oscarehr.PMmodule.caisi_integrator.DeleteCachedDemographicPreventionsWrapper;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFileFooter;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFileHeader;
import org.oscarehr.PMmodule.caisi_integrator.ProgramDeleteIdWrapper;
import org.oscarehr.caisi_integrator.dao.CachedAdmission;
import org.oscarehr.caisi_integrator.dao.CachedAppointment;
import org.oscarehr.caisi_integrator.dao.CachedBillingOnItem;
import org.oscarehr.caisi_integrator.dao.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.dao.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.dao.CachedDemographicForm;
import org.oscarehr.caisi_integrator.dao.CachedDemographicHL7LabResult;
import org.oscarehr.caisi_integrator.dao.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.dao.CachedDemographicLabResult;
import org.oscarehr.caisi_integrator.dao.CachedDemographicNote;
import org.oscarehr.caisi_integrator.dao.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.dao.CachedDxresearch;
import org.oscarehr.caisi_integrator.dao.CachedEformData;
import org.oscarehr.caisi_integrator.dao.CachedEformValue;
import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedMeasurement;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementExt;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementMap;
import org.oscarehr.caisi_integrator.dao.CachedMeasurementType;
import org.oscarehr.caisi_integrator.dao.CachedProgram;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.dao.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.caisi_integrator.dao.ImportLog;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebService;
import org.oscarehr.caisi_integrator.ws.FacilityWs;
import org.oscarehr.caisi_integrator.ws.transfer.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.transfer.ProviderTransfer;
import org.oscarehr.caisi_integrator.ws.transfer.SetConsentTransfer;

public class ImportFile
{

	static Logger logger = Logger.getLogger(ImportFile.class);
	static Properties properties = null;

	private CachedDemographicDocument lastCachedDemographicDocument = null;
	private IntegratorFileHeader header = null;
	//	private IntegratorFileFooter footer = null;

	private String zipFilename = null;
	private String zipFileChecksum = null;
	private ImportLog importLog = null;

	public ImportFile(Properties p) throws IOException
	{
		properties = p;
		//	logger.info("initing WS with " + getProperty("username") + ":" + getProperty("password"));
		//	WSUtils.init(getProperty("baseUrl"), getProperty("username"), getProperty("password"), getProperty("providerNo"));
	}

	public static String getProperty(String key)
	{
		return properties.getProperty(key);
	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
		//Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		//Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		//Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1)
		{
			digest.update(byteArray, 0, bytesCount);
		}

		//close the stream; We don't need it now.
		fis.close();

		//Get the hash's bytes
		byte[] bytes = digest.digest();

		//This bytes[] has bytes in decimal format;
		//Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		//return complete hash
		return sb.toString();
	}

	public boolean importZipFile(File file) throws IOException, NoSuchAlgorithmException
	{
		this.zipFilename = file.getAbsolutePath();

		MessageDigest md5Digest = MessageDigest.getInstance("MD5");
		this.zipFileChecksum = getFileChecksum(md5Digest, file);

		List<String> files = new ArrayList<String>();

		ZipInputStream in = new ZipInputStream(new FileInputStream(file));
		ZipEntry entry = in.getNextEntry();
		while (entry != null)
		{
			byte[] buf = new byte[1024];
			int len;

			String entryName = entry.getName();
			String fileName = getProperty("workingDir") + File.separator + entryName;
			OutputStream out = new FileOutputStream(fileName);
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			out.close();

			if (fileName.endsWith(".ser"))
			{
				files.add(fileName);
			}
			entry = in.getNextEntry();
		}

		IOUtils.closeQuietly(in);

		//now process the files in order
		Collections.sort(files, new Comparator()
		{

			@Override
			public int compare(Object arg0, Object arg1)
			{
				String f1 = ((String) arg0);
				String f2 = ((String) arg1);

				Integer index1 = Integer.parseInt(f1.split("\\.")[1]);
				Integer index2 = Integer.parseInt(f2.split("\\.")[1]);

				return Integer.compare(index1, index2);

			}
		});

		//boolean error = false;
		for (String f : files)
		{
			if (f.indexOf("documentMeta") != -1)
			{
				continue;
			}

			if (!importFile(new File(f)))
			{
				return false;
			}
			new File(f).delete();
		}

		return true;
	}

	public boolean importFile(File file)
	{

		if (!file.exists())
		{
			logger.error("File does not exist: " + file.getName());
			return false;
		}

		logger.info("import file from zip:" + file.getName());

		// this could actually be in the header of the file. but this is
		// probably more secure. You'd need a config per client though

		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream(new FileInputStream(file));

			int count = 0;
			while (true)
			{
				Object obj = null;

				try
				{
					obj = in.readObject();
					processObject(obj);
				}
				catch (EOFException e)
				{
					break;
				}
				count++;
			}

			WSUtils.getFacilityWs().updateMyFacilityLastUpdateDate(header.getDate());

			logger.info("done reading file. Found " + count + " objects");
		}
		catch (IOException e)
		{
			logger.error("Error reading file " + file.getName(), e);
			if (importLog != null) WSUtils.getFacilityWs().errorImportLog(importLog.getId().intValue());
			return false;
		}
		catch (ClassNotFoundException e)
		{
			logger.error("System Error", e);
			if (importLog != null) WSUtils.getFacilityWs().errorImportLog(importLog.getId().intValue());
			return false;
		}
		catch (Exception e)
		{
			logger.error("System Error", e);
			if (importLog != null) WSUtils.getFacilityWs().errorImportLog(importLog.getId().intValue());
			return false;

		}
		finally
		{
			IOUtils.closeQuietly(in);
		}

		return true;
	}

	public static GregorianCalendar toCalendar(Date date)
	{
		if (date == null) return(null);

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return(cal);
	}

	//checkstyle issue
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processObjectContinued(Object obj) throws Exception
	{
		ArrayList list = (ArrayList) obj;
		list.get(0).getClass();
		if (list.get(0).getClass() == ProviderTransfer.class)
		{
			processProviderTransfer(list);
		}
		else if (list.get(0).getClass() == CachedProgram.class)
		{
			processCachedProgram(list);
		}
		else if (list.get(0).getClass() == CachedDemographicIssue.class)
		{
			processCachedDemographicIssue(list);
		}
		else if (list.get(0).getClass() == CachedDemographicDrug.class)
		{
			processCachedDemographicDrug(list);
		}
		else if (list.get(0).getClass() == CachedDemographicPrevention.class)
		{
			processCachedDemographicPrevention(list);
		}
		else if (list.get(0).getClass() == CachedDemographicNote.class)
		{
			processCachedDemographicNote(list);
		}
		else if (list.get(0).getClass() == CachedAppointment.class)
		{
			processCachedAppointment(list);
		}
		else if (list.get(0).getClass() == CachedAdmission.class)
		{
			processCachedAdmission(list);
		}
		else if (list.get(0).getClass() == CachedMeasurement.class)
		{
			processCachedMeasurement(list);
		}
		else if (list.get(0).getClass() == CachedMeasurementExt.class)
		{
			processCachedMeasurementExt(list);
		}
		else if (list.get(0).getClass() == CachedMeasurementType.class)
		{
			processCachedMeasurementType(list);
		}
		else if (list.get(0).getClass() == CachedMeasurementMap.class)
		{
			processCachedMeasurementMap(list);
		}
		else if (list.get(0).getClass() == CachedDxresearch.class)
		{
			processCachedDxresearch(list);
		}
		else if (list.get(0).getClass() == CachedBillingOnItem.class)
		{
			processCachedBillingOnItem(list);
		}
		else if (list.get(0).getClass() == CachedEformData.class)
		{
			processCachedEformData(list);
		}
		else if (list.get(0).getClass() == CachedEformValue.class)
		{
			processCachedEformValue(list);
		}
		else if (list.get(0).getClass() == CachedDemographicAllergy.class)
		{
			processCachedDemographicAllergy(list);
		}
		else if (list.get(0).getClass() == CachedDemographicHL7LabResult.class)
		{
			processCachedDemographicHL7LabResult(list);
		}
		else
		{
			logger.error("Unknown List Type Found! - " + list.get(0).getClass());
			throw new RuntimeException("System Error");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processObject(Object obj) throws Exception
	{
		if (obj instanceof CachedFacility)
		{
			processCachedFacility((CachedFacility) obj);
		}
		else if (obj instanceof IntegratorFileHeader)
		{
			processIntegratorFileHeader((IntegratorFileHeader) obj);
		}
		else if (obj instanceof IntegratorFileFooter)
		{
			processIntegratorFileFooter((IntegratorFileFooter) obj);
		}
		else if (obj instanceof DemographicTransfer)
		{
			processDemographicTransfer((DemographicTransfer) obj);
		}
		else if (obj instanceof SetConsentTransfer)
		{
			processSetConsentTransfer((SetConsentTransfer) obj);
		}
		else if (obj instanceof CachedDemographicDocument)
		{
			processCachedDemographicDocument((CachedDemographicDocument) obj);
		}
		else if (obj instanceof ByteWrapper)
		{
			processByteWrapper((ByteWrapper) obj);
		}
		else if (obj instanceof CachedDemographicForm)
		{
			processCachedDemographicForm((CachedDemographicForm) obj);
		}
		else if (obj instanceof CachedDemographicLabResult)
		{
			processCachedDemographicLabResult((CachedDemographicLabResult) obj);
		}
		else if (obj instanceof CachedDemographicHL7LabResult)
		{
			processCachedDemographicHL7LabResult((CachedDemographicHL7LabResult) obj);
		}
		else if (obj instanceof ProgramDeleteIdWrapper)
		{
			//logger.info("skipping ProgramDeleteIdWrapper");
			processProgramDeleteIdWrapper((ProgramDeleteIdWrapper) obj);
		}
		else if (obj instanceof DeleteCachedDemographicIssuesWrapper)
		{
			//logger.info("skipping DeleteCachedDemographicIssuesWrapper");
			processDeleteCachedDemographicIssuesWrapper((DeleteCachedDemographicIssuesWrapper) obj);
		}
		else if (obj instanceof DeleteCachedDemographicPreventionsWrapper)
		{
			//logger.info("skipping DeleteCachedDemographicPreventionsWrapper");
			//HMM
			processDeleteCachedDemographicPreventionsWrapper((DeleteCachedDemographicPreventionsWrapper) obj);
		}
		else if (obj instanceof ArrayList)
		{
			processObjectContinued(obj);
		}
		else
		{
			logger.error("Uknown Type Found! - " + obj.getClass().getName());
			throw new RuntimeException("SystemError");
		}
	}

	private void processCachedFacility(CachedFacility cachedFacility)
	{
		logger.debug("Found a CachedFacility");

		FacilityWs service = WSUtils.getFacilityWs();
		// CachedFacility currentlyCachedFacility = service.getMyFacility();
		service.setMyFacility(cachedFacility);
	}

	private void processSetConsentTransfer(SetConsentTransfer setConsentTransfer)
	{
		logger.debug("Found list of SetConsentTransfer" + setConsentTransfer);
	}

	private void processProviderTransfer(ArrayList<ProviderTransfer> providerTransfer)
	{
		logger.debug("Found list of ProviderTransfer");
		WSUtils.getProviderWs().setCachedProviders(providerTransfer);
	}

	private void processCachedProgram(ArrayList<CachedProgram> cachedProgram)
	{
		logger.debug("Found list of CachedProgram");
		WSUtils.getProgramWs().setCachedPrograms(cachedProgram);
	}

	private void processCachedDemographicIssue(ArrayList<CachedDemographicIssue> cachedDemographicIssue)
	{
		logger.debug("Found list of CachedDemographicIssue");
		WSUtils.getDemographicWs().setCachedDemographicIssues(cachedDemographicIssue.toArray(new CachedDemographicIssue[cachedDemographicIssue.size()]));
	}

	private void processCachedDemographicPrevention(ArrayList<CachedDemographicPrevention> cachedDemographicPrevention)
	{
		logger.debug("Found list of CachedDemographicPrevention");
		WSUtils.getDemographicWs().setCachedDemographicPreventions(cachedDemographicPrevention.toArray(new CachedDemographicPrevention[cachedDemographicPrevention.size()]));
	}

	private void processCachedDemographicNote(ArrayList<CachedDemographicNote> cachedDemographicNote)
	{
		logger.debug("Found list of CachedDemographicNote");
		WSUtils.getDemographicWs().setCachedDemographicNotes(cachedDemographicNote.toArray(new CachedDemographicNote[cachedDemographicNote.size()]));

	}

	private void processCachedDemographicDrug(ArrayList<CachedDemographicDrug> cachedDemographicDrug)
	{
		logger.debug("Found list of CachedDemographicDrug");
		WSUtils.getDemographicWs().setCachedDemographicDrugs(cachedDemographicDrug.toArray(new CachedDemographicDrug[cachedDemographicDrug.size()]));
	}

	private void processCachedAppointment(ArrayList<CachedAppointment> cachedAppointment)
	{
		logger.debug("Found list of CachedAppointment");
		WSUtils.getDemographicWs().setCachedAppointments(cachedAppointment.toArray(new CachedAppointment[cachedAppointment.size()]));
	}

	private void processCachedAdmission(ArrayList<CachedAdmission> cachedAdmission)
	{
		logger.debug("Found list of CachedAdmission");
		WSUtils.getDemographicWs().setCachedAdmissions(cachedAdmission.toArray(new CachedAdmission[cachedAdmission.size()]));
	}

	private void processCachedMeasurement(ArrayList<CachedMeasurement> cachedMeasurement)
	{
		logger.debug("Found list of CachedMeasurement");
		WSUtils.getDemographicWs().setCachedMeasurements(cachedMeasurement.toArray(new CachedMeasurement[cachedMeasurement.size()]));
	}

	private void processCachedMeasurementExt(ArrayList<CachedMeasurementExt> cachedMeasurementExt)
	{
		logger.debug("Found list of CachedMeasurementExt");
		WSUtils.getDemographicWs().setCachedMeasurementExts(cachedMeasurementExt.toArray(new CachedMeasurementExt[cachedMeasurementExt.size()]));
	}

	private void processCachedMeasurementType(ArrayList<CachedMeasurementType> cachedMeasurementType)
	{
		logger.debug("Found list of CachedMeasurementType");
		WSUtils.getDemographicWs().setCachedMeasurementTypes(cachedMeasurementType.toArray(new CachedMeasurementType[cachedMeasurementType.size()]));
	}

	private void processCachedMeasurementMap(ArrayList<CachedMeasurementMap> cachedMeasurementMap)
	{
		logger.debug("Found list of CachedMeasurementMap");
		WSUtils.getDemographicWs().setCachedMeasurementMaps(cachedMeasurementMap.toArray(new CachedMeasurementMap[cachedMeasurementMap.size()]));
	}

	private void processCachedDxresearch(ArrayList<CachedDxresearch> cachedDxresearch)
	{
		logger.debug("Found list of CachedDxresearch");
		WSUtils.getDemographicWs().setCachedDxresearch(cachedDxresearch.toArray(new CachedDxresearch[cachedDxresearch.size()]));
	}

	private void processCachedBillingOnItem(ArrayList<CachedBillingOnItem> cachedBillingOnItem)
	{
		logger.debug("Found list of CachedBillingOnItem");
		WSUtils.getDemographicWs().setCachedBillingOnItem(cachedBillingOnItem.toArray(new CachedBillingOnItem[cachedBillingOnItem.size()]));
	}

	private void processCachedEformData(ArrayList<CachedEformData> cachedEformData)
	{
		logger.debug("Found list of CachedEformData");
		WSUtils.getDemographicWs().setCachedEformData(cachedEformData.toArray(new CachedEformData[cachedEformData.size()]));
	}

	private void processCachedEformValue(ArrayList<CachedEformValue> cachedEformValue)
	{
		logger.debug("Found list of CachedEformValue");
		WSUtils.getDemographicWs().setCachedEformValues(cachedEformValue.toArray(new CachedEformValue[cachedEformValue.size()]));
	}

	private void processCachedDemographicAllergy(ArrayList<CachedDemographicAllergy> cachedDemographicAllergy)
	{
		logger.debug("Found list of CachedDemographicAllergy");
		WSUtils.getDemographicWs().setCachedDemographicAllergies(cachedDemographicAllergy.toArray(new CachedDemographicAllergy[cachedDemographicAllergy.size()]));
	}

	private void processCachedDemographicHL7LabResult(ArrayList<CachedDemographicHL7LabResult> cachedDemographicHL7LabResult)
	{
		logger.debug("Found list of CachedDemographicHL7LabResult");
		WSUtils.getDemographicWs().setCachedDemographicHL7Labs(cachedDemographicHL7LabResult.toArray(new CachedDemographicHL7LabResult[cachedDemographicHL7LabResult.size()]));
	}

	private void processCachedDemographicDocument(CachedDemographicDocument cachedDemographicDocument)
	{
		logger.debug("Found a CachedDemographicDocument");
		this.lastCachedDemographicDocument = cachedDemographicDocument;
		WSUtils.getDemographicWs().addCachedDemographicDocument(lastCachedDemographicDocument);
	}

	private void processByteWrapper(ByteWrapper byteWrapper)
	{
		logger.debug("Found a ByteWrapper");
		if (lastCachedDemographicDocument != null)
		{
			WSUtils.getDemographicWs().addCachedDemographicDocumentAndContents(lastCachedDemographicDocument, byteWrapper.getData());
		}
		lastCachedDemographicDocument = null;
	}

	private void processCachedDemographicForm(CachedDemographicForm cachedDemographicForm)
	{
		logger.debug("Found a CachedDemographicForm");
		WSUtils.getDemographicWs().addCachedDemographicForm(cachedDemographicForm);
	}

	private void processCachedDemographicLabResult(CachedDemographicLabResult cachedDemographicLabResult)
	{
		logger.debug("Found a CachedDemographicLabResult");
		WSUtils.getDemographicWs().addCachedDemographicLabResult(cachedDemographicLabResult);
	}

	private void processCachedDemographicHL7LabResult(CachedDemographicHL7LabResult cachedDemographicHL7LabResult)
	{
		logger.debug("Found a CachedDemographicHL7LabResult");
		WSUtils.getDemographicWs().addCachedDemographicHL7LabResult(cachedDemographicHL7LabResult);
	}

	private void processDemographicTransfer(DemographicTransfer demographicTransfer)
	{
		logger.debug("Found a DemographicTransfer");
		WSUtils.getDemographicWs().setLastPushDate(demographicTransfer.getCaisiDemographicId());

		WSUtils.getDemographicWs().setDemographic(demographicTransfer);
	}

	protected void processProgramDeleteIdWrapper(ProgramDeleteIdWrapper programDeleteIdWrapper)
	{
		logger.debug("Found a ProgramDeleteIdWrapper");
		WSUtils.getProgramWs().deleteCachedProgramsMissingFromList(programDeleteIdWrapper.getIds().toArray(new Integer[programDeleteIdWrapper.getIds().size()]));
	}

	protected void processDeleteCachedDemographicIssuesWrapper(DeleteCachedDemographicIssuesWrapper deleteCachedDemographicIssuesWrapper)
	{
		logger.debug("Found a DeleteCachedDemographicIssuesWrapper");
		WSUtils.getDemographicWs().deleteCachedDemographicIssues(deleteCachedDemographicIssuesWrapper.getDemographicNo(), deleteCachedDemographicIssuesWrapper.getKeys().toArray(new FacilityIdDemographicIssueCompositePk[deleteCachedDemographicIssuesWrapper.getKeys().size()]));
	}

	protected void processDeleteCachedDemographicPreventionsWrapper(DeleteCachedDemographicPreventionsWrapper deleteCachedDemographicPreventionsWrapper)
	{
		logger.debug("Found a DeleteCachedDemographicPreventionsWrapper");
		WSUtils.getDemographicWs().deleteCachedDemographicPreventions(deleteCachedDemographicPreventionsWrapper.getDemographicNo(), deleteCachedDemographicPreventionsWrapper.getNonDeletedIds().toArray(new Integer[deleteCachedDemographicPreventionsWrapper.getNonDeletedIds().size()]));
	}

	private void processIntegratorFileHeader(IntegratorFileHeader integratorFileHeader)
	{
		LoggedInInfoWebService loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo == null)
		{
			LoggedInInfoWebService.setLoggedInInfo("-1", "0.0" + "0.0");
			loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
		}

		FacilityDao facilityDao = WSUtils.getFacilityDao();
		Facility f = facilityDao.findByName(integratorFileHeader.getUsername());

		if (f == null)
		{
			throw new RuntimeException("Invalid username in header");
		}
		//Facility f = facilityDao.findAll().get(integratorFileHeader.getCachedFacilityId());
		loggedInInfo.setAuthenticatedFacility(f);

		logger.debug("Found a IntegratorFileHeader");
		this.header = integratorFileHeader;

		if (integratorFileHeader.getVersion() != IntegratorFileHeader.VERSION)
		{
			throw new RuntimeException("Wrong version of file! Make sure the importer and OSCAR are the right versions");
		}

		//can we continue?
		String validation = WSUtils.getFacilityWs().canProcessFile(zipFilename.substring(zipFilename.lastIndexOf("/") + 1), zipFileChecksum, header.getLastDate(), header.getDate(), header.getDependsOn());

		if ("OK".equals(validation))
		{
			importLog = WSUtils.getFacilityWs().createImportLog(zipFilename.substring(zipFilename.lastIndexOf("/") + 1), zipFileChecksum, header.getLastDate(), header.getDate(), header.getDependsOn());
		}
		else if ("INTEGRATOR_HAS_NEWER_DATA".equals(validation))
		{
			importLog = WSUtils.getFacilityWs().createImportLogWithStatus(zipFilename.substring(zipFilename.lastIndexOf("/") + 1), zipFileChecksum, header.getLastDate(), header.getDate(), header.getDependsOn(), "ERROR");
			throw new RuntimeException("Integrator has newer data, aborting");
		}
		else if ("DEPENDENCY_NOT_MET".equals(validation))
		{
			importLog = WSUtils.getFacilityWs().createImportLogWithStatus(zipFilename.substring(zipFilename.lastIndexOf("/") + 1), zipFileChecksum, header.getLastDate(), header.getDate(), header.getDependsOn(), "ERROR");
			throw new RuntimeException("Integrator does not have dependencies met (" + integratorFileHeader.getDependsOn() + ")");
		}
		else if ("ALREADY_IMPORTED".equals(validation))
		{
			importLog = WSUtils.getFacilityWs().createImportLogWithStatus(zipFilename.substring(zipFilename.lastIndexOf("/") + 1), zipFileChecksum, header.getLastDate(), header.getDate(), header.getDependsOn(), "ERROR");
			throw new RuntimeException("Integrator already has this file");
		}

	}

	private void processIntegratorFileFooter(IntegratorFileFooter integratorFileFooter)
	{
		logger.debug("Found a IntegratorFileFooter" + integratorFileFooter);
		//this.footer = integratorFileFooter;

		//lets process the docs
		processDocsZip();

		WSUtils.getFacilityWs().completeImportLog(importLog.getId().intValue());
	}

	private String[] processDocumentMetaLine(String v)
	{
		if (v == null || v.isEmpty())
		{
			return null;
		}

		String[] results = new String[3];
		results[0] = v.substring(0, v.indexOf(","));
		v = v.substring(results[0].length() + 1);
		results[1] = v.substring(0, v.indexOf(","));
		v = v.substring(results[1].length() + 1);
		results[2] = v;

		return results;
	}

	protected void processDocsZip()
	{
		String filename = this.zipFilename.replaceAll("\\.zip$", "-Docs.zip");
		logger.info("processing zip file for docs - " + filename);

		BufferedReader in = null;
		BufferedInputStream in2 = null;

		try
		{
			ProcessBuilder processBuilder = new ProcessBuilder("unzip", filename, "-d", getProperty("workingDir")).inheritIO();

			Process process = processBuilder.start();

			process.waitFor();

			logger.info("unzip is complete");

			//read the documentMeta.txt file, and start importing the files?

			File f = new File(getProperty("workingDir"), "documentMeta.txt");
			if (!f.exists())
			{
				logger.warn("no documentMeta.txt file exists in " + getProperty("workingDir"));
				return;
			}
			in = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = in.readLine()) != null)
			{
				String[] parts = processDocumentMetaLine(line);
				File f2 = new File(getProperty("workingDir"), parts[2]);
				if (!f2.exists())
				{
					logger.warn("file doesn't exist " + f2);
					continue;
				}
				in2 = new BufferedInputStream(new FileInputStream(f2));

				byte[] documentData = IOUtils.toByteArray(in2);
				WSUtils.getDemographicWs().addCachedDemographicDocumentContents(Integer.parseInt(parts[0]), documentData);

				in2.close();

				f2.delete();

				logger.debug("inserted and deleted " + parts[2]);
			}

		}
		catch (Exception e)
		{
			logger.error("Error", e);
		}
		finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(in2);
		}

	}

}
