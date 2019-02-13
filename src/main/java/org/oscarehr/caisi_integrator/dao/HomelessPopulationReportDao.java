package org.oscarehr.caisi_integrator.dao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.util.AdditionalSchemaGenerationSql;
import org.springframework.stereotype.Repository;

@Repository
public class HomelessPopulationReportDao extends AbstractDao<HomelessPopulationReport>
{
    public HomelessPopulationReportDao()
	{
		super(HomelessPopulationReport.class);
	}

	public HomelessPopulationReport findFirstReport()
	{
		Query query = entityManager.createNativeQuery("select * from "+modelClass.getSimpleName()+" order by reportTime asc limit 1", modelClass);
		
		return(getSingleResultOrNull(query));
	}

	public HomelessPopulationReport findFirstReportBeforeThisTime(GregorianCalendar beforeThisTime)
	{
		Query query = entityManager.createNativeQuery("select * from "+modelClass.getSimpleName()+" where reportTime<?1 order by reportTime desc limit 1", modelClass);
		query.setParameter(1, beforeThisTime);
		
		return(getSingleResultOrNull(query));
	}

	/**
	 * Programs in a report is configurable by the user.
	 * The reports will always be generated based on the latest
	 * set of programs selected. Historical list of programs
	 * is maintained in this table so you can see which programs
	 * were used in previous reports too. The set method
	 * for this is intended to be written to by a report 
	 * configuration screen. The get method is intended to
	 * be called during the generation of the reports.
	 */
	public void setProgramsToUseInReports(ArrayList<FacilityIdIntegerCompositePk> programsInReport)
	{
		GregorianCalendar currentTime=new GregorianCalendar();

		Query query=entityManager.createNativeQuery("insert into HomelessPopulationReportPrograms (configurationTime,facilityId,programId) values (?,?,?)");
		query.setParameter(1, currentTime);
		
		for (FacilityIdIntegerCompositePk pk : programsInReport)
		{
			query.setParameter(2, pk.getIntegratorFacilityId());
			query.setParameter(3, pk.getCaisiItemId());
			query.executeUpdate();
		}
	}

	public ArrayList<FacilityIdIntegerCompositePk> getProgramsUsedReport(GregorianCalendar dateOfReport)
	{
		// get the date of the configuration used.
		Query query = entityManager.createNativeQuery("select configurationTime from HomelessPopulationReportPrograms where configurationTime<=? order by configurationTime desc limit 1", GregorianCalendar.class);
		query.setParameter(1, dateOfReport);
		GregorianCalendar configurationTime=(GregorianCalendar)getSingleResultOrNullNoType(query);
		
		ArrayList<FacilityIdIntegerCompositePk> results=new ArrayList<FacilityIdIntegerCompositePk>();

		if (configurationTime!=null)
		{
			// get all the programs in that configuration
			Query query2 = entityManager.createNativeQuery("select facilityId,programId from HomelessPopulationReportPrograms where configurationTime=?");
			query2.setParameter(1, configurationTime);
			
			@SuppressWarnings("unchecked")
			List<Object[]> tempResults = query2.getResultList();
			for (Object[] temp : tempResults)
			{
				results.add(new FacilityIdIntegerCompositePk((Integer)temp[0], (Integer)temp[1]));
			}
		}
		
		return(results);
	}
	
	/**
	 * @param startDate is inclusive
	 * @param endDate is exclusive
	 * @return results ordered by reportTime
	 */
	public List<HomelessPopulationReport> findByDateRange(GregorianCalendar startDate, GregorianCalendar endDate)
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.reportTime>=?1 and x.reportTime<?2 order by x.reportTime");
		query.setParameter(1, startDate);
		query.setParameter(2, endDate);
		
		@SuppressWarnings("unchecked")
		List<HomelessPopulationReport> results = query.getResultList();

		return(results);
	}
	
	@AdditionalSchemaGenerationSql
	public String[] getAdditionalCustomSql()
	{
		String[] sql = 
		{
			"create table HomelessPopulationReportPrograms (configurationTime datetime not null, index(configurationTime), facilityId int not null, programId int not null)"
		};

		return(sql);
	}

}
