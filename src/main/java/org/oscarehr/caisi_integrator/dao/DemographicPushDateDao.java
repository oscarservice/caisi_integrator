package org.oscarehr.caisi_integrator.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.util.SpringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicPushDateDao extends AbstractDao<DemographicPushDate>
{
	public DemographicPushDateDao()
	{
		super(DemographicPushDate.class);
	}

	public List<DemographicPushDate> findAfterDate(Calendar afterThisDate)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.lastPushDate>?1");

		query.setParameter(1, afterThisDate);

		@SuppressWarnings("unchecked")
		List<DemographicPushDate> results = query.getResultList();

		return(results);
	}

	public ArrayList<Integer> getUpdatedLinkedDemographicIdsFromFacilityAfterDate(Integer facilityId, Calendar updateAfterThisDate)
	{
		// algorithm
		//----------
		// get everyone who's updated.
		// for each person get all their links
		//    if a link is in our facility, add it to the results list
		//
		// to optimise the above, we remove people from the updatedList if they are 
		// in the links list, i.e. we know they're updated already so no need to check again
		// because of this optimistaion we need to keep processing the list until it's empty instead of from index 0 to size of list.

		DemographicLinkDao demographicLinkDao = (DemographicLinkDao) SpringUtils.getBean("demographicLinkDao");
		List<DemographicPushDate> demographicPushDates = findAfterDate(updateAfterThisDate);
		ArrayList<FacilityIdIntegerCompositePk> updateIdsToInspect = new ArrayList<FacilityIdIntegerCompositePk>();

		for (DemographicPushDate demographicPushDate : demographicPushDates)
		{
			updateIdsToInspect.add(demographicPushDate.getId());
		}

		ArrayList<Integer> resultsToReturn = new ArrayList<Integer>();

		while (updateIdsToInspect.size() > 0)
		{
			FacilityIdIntegerCompositePk idToInspect = updateIdsToInspect.get(0);

			TreeSet<FacilityIdIntegerCompositePk> linkedIds = demographicLinkDao.getAllLinkedDemographics(idToInspect, false);
			for (FacilityIdIntegerCompositePk linkedId : linkedIds)
			{
				if (facilityId.equals(linkedId.getIntegratorFacilityId()))
				{
					resultsToReturn.add(linkedId.getCaisiItemId());
				}

				updateIdsToInspect.remove(linkedId);
			}
		}

		return(resultsToReturn);
	}
}
