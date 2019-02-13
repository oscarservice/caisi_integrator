package org.oscarehr.caisi_integrator.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class ProviderCommunicationDao extends AbstractDao<ProviderCommunication>
{
    public ProviderCommunicationDao()
	{
		super(ProviderCommunication.class);
	}

    public List<ProviderCommunication> findByDestinationFacilityAndProviderIdAndType(Integer destinationIntegratorFacilityId, String destinationProviderId, String type, Boolean active)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.destinationIntegratorFacilityId=?1 and x.destinationProviderId=?2 and x.type=?3"+(active!=null?" and x.active=?4":""));
		query.setParameter(1, destinationIntegratorFacilityId);
		query.setParameter(2, destinationProviderId);
		query.setParameter(3, type);
		if (active!=null) query.setParameter(4, active);

		@SuppressWarnings("unchecked")
		List<ProviderCommunication> results = query.getResultList();

		return(results);
	}
}
