package org.oscarehr.caisi_integrator.site.ui.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;
import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.CachedFacility;
import org.oscarehr.caisi_integrator.dao.CachedFacilityDao;
import org.oscarehr.caisi_integrator.dao.CachedProgram;
import org.oscarehr.caisi_integrator.dao.CachedProgramDao;
import org.oscarehr.caisi_integrator.dao.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.dao.HomelessPopulationReportDao;
import org.oscarehr.caisi_integrator.site.utils.JsfUtils;
import org.oscarehr.caisi_integrator.util.Named;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class HomelessPopulationReportConfigurationJsfBean implements Serializable
{
	@Transient
	private CachedFacilityDao cachedFacilityDao = (CachedFacilityDao)SpringUtils.getBean("cachedFacilityDao");
	
	@Transient
	private CachedProgramDao cachedProgramDao = (CachedProgramDao)SpringUtils.getBean("cachedProgramDao");

	@Transient
	private HomelessPopulationReportDao homelessPopulationReportDao = (HomelessPopulationReportDao)SpringUtils.getBean("homelessPopulationReportDao");

	private ArrayList<ProgramDisplayRow> resultRows = null;

	public static class ProgramDisplayRow implements Serializable, Named
	{
		private boolean checked=false;
		private FacilityIdIntegerCompositePk programId = null;
		private String facilityName = null;
		private String programName = null;
		private String programType = null;

		public ProgramDisplayRow(FacilityIdIntegerCompositePk programId, String facilityName, String programName, String programType)
		{
			this.programId = programId;
			this.facilityName = facilityName;
			this.programName = programName;
			this.programType = programType;
		}

		public boolean isChecked()
		{
			return checked;
		}

		public void setChecked(boolean checked)
		{
			this.checked = checked;
		}

		public FacilityIdIntegerCompositePk getProgramId()
		{
			return programId;
		}

		public void setProgramId(FacilityIdIntegerCompositePk programId)
		{
			this.programId = programId;
		}

		public String getFacilityName()
		{
			return facilityName;
		}

		public void setFacilityName(String facilityName)
		{
			this.facilityName = facilityName;
		}

		public String getProgramName()
		{
			return programName;
		}

		public void setProgramName(String programName)
		{
			this.programName = programName;
		}

		public String getProgramType()
		{
			return programType;
		}

		public void setProgramType(String programType)
		{
			this.programType = programType;
		}

		@Override
		public String getName()
		{
			return(facilityName +'.'+ programName +'.'+ programType);
		}

		
	}

	public ListDataModel<ProgramDisplayRow> getSelectedPrograms()
	{
		if (resultRows == null)
		{
			resultRows = new ArrayList<ProgramDisplayRow>();
			
			ArrayList<FacilityIdIntegerCompositePk> selectedProgramIds = homelessPopulationReportDao.getProgramsUsedReport(new GregorianCalendar());

			List<CachedProgram> cachedPrograms=cachedProgramDao.findAll();
			for (CachedProgram cachedProgram : cachedPrograms)
			{
				CachedFacility cachedFacility=cachedFacilityDao.find(cachedProgram.getId().getIntegratorFacilityId());				
				
				ProgramDisplayRow programDisplayRow=new ProgramDisplayRow(cachedProgram.getId(), cachedFacility.getName(), cachedProgram.getName(), cachedProgram.getType());

				programDisplayRow.setChecked(selectedProgramIds.contains(cachedProgram.getId()));
				
				resultRows.add(programDisplayRow);
			}
						
			Collections.sort(resultRows, Named.NAME_COMPARATOR);
		}

		ListDataModel<ProgramDisplayRow> results = new ListDataModel<ProgramDisplayRow>(resultRows);
		return(results);
	}

	public String save()
	{
		ArrayList<FacilityIdIntegerCompositePk> selectedProgramIds=new ArrayList<FacilityIdIntegerCompositePk>();
		
		for (ProgramDisplayRow row : resultRows)
		{
			if (row.isChecked())
			{
				selectedProgramIds.add(row.getProgramId());
			}
		}
		
		homelessPopulationReportDao.setProgramsToUseInReports(selectedProgramIds);
		
		JsfUtils.addInfoMessage("Changes Saved");
		
		return(null);
	}
}
