package org.oscarehr.caisi_integrator.importer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.util.ConfigXmlUtils;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.LoggedInInfoWebService;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.context.ApplicationContext;

public class ImportJob extends TimerTask
{
	ApplicationContext applicationContext;
	private static final Logger logger = MiscUtils.getLogger();

	Properties properties = null;

	private File lockFile;

	public Properties getProperties() throws IOException
	{
		if (properties == null)
		{
			Properties temp = new Properties();
			temp.put("scanDirectory", ConfigXmlUtils.getPropertyString("import", "scanDirectory"));
			temp.put("archiveDirectory", ConfigXmlUtils.getPropertyString("import", "archiveDirectory"));
			temp.put("workingDir", ConfigXmlUtils.getPropertyString("import", "workingDir"));
			properties = temp;
		}
		return properties;
	}

	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}

	private static ImportJob importJobTimerTask = null;
	private static final long RUN_PERIOD = DateUtils.MILLIS_PER_MINUTE;

	public static synchronized void start()
	{
		if (importJobTimerTask == null)
		{
			importJobTimerTask = new ImportJob();
			MiscUtils.sharedTimer.schedule(importJobTimerTask, 10000, RUN_PERIOD);
			logger.info("Started ImportJob. CheckPeriod=" + RUN_PERIOD);
		}
		else
		{
			logger.warn("ImportJob called but is already running.");
		}
	}

	public static synchronized void stop()
	{
		if (importJobTimerTask != null)
		{
			importJobTimerTask.cancel();
			importJobTimerTask = null;
			logger.info("Stopped ImportJob");
		}
		else
		{
			logger.warn("stopReportGeneration called but is not currently running.");
		}
	}

	private void cleanup()
	{
		logger.info("cleaning up lock file");
		lockFile.delete();
	}

	private boolean createLockFile()
	{
		logger.info("creating lock file");
		String tmpDir = System.getProperty("java.io.tmpdir");

		lockFile = new File(tmpDir + File.separator + "importer.lock");
		try
		{
			lockFile.createNewFile();
		}
		catch (IOException e)
		{
			logger.error("Error", e);
			return false;
		}
		return true;
	}

	private boolean lockFileExists()
	{
		if (lockFile != null && lockFile.exists())
		{
			return true;
		}
		return false;
	}

	@Override
	public void run()
	{
		logger.info("running import job");

		if (lockFileExists())
		{
			logger.warn("Lock file exists, another process is running..aborting");
			return;
		}

		if (!createLockFile())
		{
			logger.warn("Unable to create lock file. exiting");
			return;
		}

		try
		{
			getProperties();
		}
		catch (Exception e)
		{
			logger.error("Error", e);
			cleanup();
			return;
		}

		String scanDirectory = getProperty("scanDirectory");
		if (scanDirectory == null)
		{
			logger.error("must set scanDirectory in the cnf file");
			cleanup();
			return;
		}
		File dir = new File(scanDirectory);
		if (!dir.exists() || !dir.isDirectory())
		{
			logger.error("scanDirectory must exist and be a writeable directory");
			cleanup();
			return;
		}

		String archiveDirectory = getProperty("archiveDirectory");
		if (archiveDirectory == null)
		{
			logger.error("must set archiveDirectory in the cnf file");
			cleanup();
			return;
		}
		File archiveDir = new File(archiveDirectory);
		if (!archiveDir.exists() || !archiveDir.isDirectory())
		{
			logger.error("archiveDirectory must exist and be a writeable directory");
			cleanup();
			return;
		}

		logger.info("All setup and scanning " + scanDirectory);
		File[] files = dir.listFiles(new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				if (name.startsWith("IntegratorPush") && name.endsWith(".zip") && !name.endsWith("-Docs.zip")) return true;
				return false;
			}
		});

		//TESTING

		String ip = "0.0" + ".0.0";

		LoggedInInfoWebService loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo == null)
		{
			LoggedInInfoWebService.setLoggedInInfo("-1", ip);
			loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
		}

		FacilityDao facilityDao = WSUtils.getFacilityDao();
		Facility f = facilityDao.findAll().get(0);
		loggedInInfo.setAuthenticatedFacility(f);

		List<File> fileList = Arrays.asList(files);

		Collections.sort(fileList, new Comparator<File>()
		{
			public int compare(File a, File b)
			{
				return a.getName().compareTo(b.getName());
			}
		});

		try
		{
			inner: for (File file : fileList)
			{
				if (!process(file, archiveDir))
				{
					break inner;
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Error processing file, aborting this run.", e);
		}

		cleanup();
	}

	public boolean process(File file, File archiveDir) throws IOException, NoSuchAlgorithmException
	{
		logger.info("processing " + file.getName());

		ImportFile importer = new ImportFile(getProperties());

		boolean result = importer.importZipFile(file);

		if (result)
		{
			file.renameTo(new File(archiveDir.getAbsolutePath(), file.getName()));
		}

		return result;
	}
}
