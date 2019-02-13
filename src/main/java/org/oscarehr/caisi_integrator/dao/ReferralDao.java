package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class ReferralDao extends AbstractDao<Referral>
{
    public ReferralDao()
	{
		super(Referral.class);
	}

    public List<Referral> findByDestinationFacilityAndProgramId(Integer destinationIntegratorFacilityId, Integer destinationCaisiProgramId)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.destinationIntegratorFacilityId=?1 and x.destinationCaisiProgramId=?2");
		query.setParameter(1, destinationIntegratorFacilityId);
		query.setParameter(2, destinationCaisiProgramId);

		@SuppressWarnings("unchecked")
		List<Referral> results = query.getResultList();

		return(results);
	}

	public List<Referral> findBySourceFacilityAndDemographicId(Integer sourceIntegratorFacilityId, Integer sourceCaisiDemographicId)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.sourceIntegratorFacilityId=?1 and x.sourceCaisiDemographicId=?2");
		query.setParameter(1, sourceIntegratorFacilityId);
		query.setParameter(2, sourceCaisiDemographicId);

		@SuppressWarnings("unchecked")
		List<Referral> results = query.getResultList();

		return(results);
	}
}
