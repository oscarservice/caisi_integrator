package org.oscarehr.caisi_integrator.dao;

import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedAdmissionDao extends AbstractDao<CachedAdmission>
{
	public CachedAdmissionDao()
	{
		super(CachedAdmission.class);
	}
	
    public List<CachedAdmission> findByFacilityIdAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from "+modelClass.getSimpleName()+" where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);
		
		@SuppressWarnings("unchecked")
		List<CachedAdmission> results=query.getResultList();
		
		return(results);
	}
    
    /**
     * Find all admissions who were discharged after the specified date. This includes people
     * who aren't discharged at all yet (presumes they eventually will be discharged).
     */
	public List<CachedAdmission> findDischargesToProgramAfterDate(Integer integratorFacilityId, Integer caisiProgramId, GregorianCalendar afterThisDate)
	{
		String sqlCommand="select * from "+modelClass.getSimpleName()+" where integratorFacilityId=?1 and caisiProgramId=?2 and (dischargeDate>?3 or dischargeDate is null)";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiProgramId);
		query.setParameter(3, afterThisDate);
		
		@SuppressWarnings("unchecked")
		List<CachedAdmission> results=query.getResultList();
		
		return(results);
	}

	public List<CachedAdmission> findAdmissionsToProgramAfterDate(Integer integratorFacilityId, Integer caisiProgramId, GregorianCalendar afterThisDate)
	{
		String sqlCommand="select * from "+modelClass.getSimpleName()+" where integratorFacilityId=?1 and caisiProgramId=?2 and admissionDate>?3";

		Query query = entityManager.createNativeQuery(sqlCommand, modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiProgramId);
		query.setParameter(3, afterThisDate);
		
		@SuppressWarnings("unchecked")
		List<CachedAdmission> results=query.getResultList();
		
		return(results);
	}
}
