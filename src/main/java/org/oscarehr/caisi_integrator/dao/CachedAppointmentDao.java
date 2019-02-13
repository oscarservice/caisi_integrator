package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class CachedAppointmentDao extends AbstractDao<CachedAppointment>
{
	public CachedAppointmentDao()
	{
		super(CachedAppointment.class);
	}

	public List<CachedAppointment> findByFacilityAndDemographicId(Integer integratorFacilityId, Integer caisiDemographicId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiDemographicId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiDemographicId);

		@SuppressWarnings("unchecked")
		List<CachedAppointment> results = query.getResultList();

		return(results);
	}

	public List<CachedAppointment> findByFacilityAndProviderId(Integer integratorFacilityId, String caisiProviderId)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where integratorFacilityId=?1 and caisiProviderId=?2", modelClass);
		query.setParameter(1, integratorFacilityId);
		query.setParameter(2, caisiProviderId);

		@SuppressWarnings("unchecked")
		List<CachedAppointment> results = query.getResultList();

		return(results);
	}
}
