package org.oscarehr.caisi_integrator.dao;

import java.util.List;
import java.util.TreeMap;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.util.CodeType;
import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicIssueDao extends AbstractDao<CachedDemographicIssue>
{	
	public CachedDemographicIssueDao()
	{
		super(CachedDemographicIssue.class);
	}

	/**
	 * Get count by facility.
	 */
	public int getCount(Integer integratorFacilityId)
	{
		Query query = entityManager.createNativeQuery("select count(*) from " + modelClass.getSimpleName() + " where integratorFacilityId=?1", Integer.class);
		query.setParameter(1, integratorFacilityId);

		return((Integer)query.getSingleResult());
	}

	public List<CachedDemographicIssue> findByFacilityAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedDemographicIssue> results = query.getResultList();

		return(results);
	}
	
	/**
	 * @return a map of NoteIssue objects representing each type of issue collected by the system. The key is the issue and the value is the description.
	 */
	public TreeMap<NoteIssue, String> findReportedIssues()
	{
		Query query = entityManager.createNativeQuery("select distinct codeType,issueCode,issueDescription from " + modelClass.getSimpleName()+" order by codeType,issueCode");

		@SuppressWarnings("unchecked")
		List<Object[]> tempResults = query.getResultList();
		
		TreeMap<NoteIssue, String> results=new TreeMap<NoteIssue, String>();
		for (Object[] temp : tempResults)
		{
			NoteIssue noteIssue=new NoteIssue();
			noteIssue.setCodeType(CodeType.valueOf((String)temp[0]));
			noteIssue.setIssueCode((String)temp[1]);
			
			String description=(String)temp[2];
			
			results.put(noteIssue, description);
		}

		return(results);
	}
	
	public boolean hasUnresolvedIssueFromIssueGroup(Integer facilityId, Integer caisiDemographicId, String issueGroupName)
	{
		String sqlCommand="select count(*) from CachedDemographicIssue x, IssueGroup y where x.codeType=y.codeType and x.issueCode=y.issueCode and x.integratorFacilityId=?1 and x.caisiDemographicId=?2 and y.name=?3 and x.resolved=?4";
		
		Query query = entityManager.createNativeQuery(sqlCommand, Integer.class);
		query.setParameter(1, facilityId);
		query.setParameter(2, caisiDemographicId);
		query.setParameter(3, issueGroupName);
		query.setParameter(4, false);

		Integer count=(Integer)query.getSingleResult();
		return(count>0);
	}
}
