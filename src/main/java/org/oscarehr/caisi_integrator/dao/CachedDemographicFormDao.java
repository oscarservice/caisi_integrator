package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicFormDao extends AbstractDao<CachedDemographicForm>
{
	public CachedDemographicFormDao()
	{
		super(CachedDemographicForm.class);
	}

	/**
	 * @param formName can be null to return all forms
	 * @return
	 */
	public List<CachedDemographicForm> findByFacilityDemographicIdFormName(Integer integratorFacilityId, Integer caisiDemographicId, String formName)
	{
		StringBuilder sqlCommand=new StringBuilder();
		sqlCommand.append("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2");
		
		if (formName!=null)
		{
			sqlCommand.append(" and formName=?3");
		}
		
		Query query = entityManager.createNativeQuery(sqlCommand.toString(), modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		if (formName!=null)
		{
			query.setParameter(3, formName);
		}

		@SuppressWarnings("unchecked")
		List<CachedDemographicForm> results = query.getResultList();

		return(results);
	}
}
