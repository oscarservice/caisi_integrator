package org.oscarehr.caisi_integrator.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicLinkDao extends AbstractDao<DemographicLink>
{
	@Autowired
	private CachedDemographicDao cachedDemographicDao;
	
	public DemographicLinkDao()
	{
		super(DemographicLink.class);
	}

	/**
	 * This method will return all demographics which are linked to the one passed in including transitive associations, but excluding myself.
	 * This method specifically returns a treeset so that the results are ordered / sorted.
	 */
	public TreeSet<FacilityIdIntegerCompositePk> getAllLinkedDemographics(FacilityIdIntegerCompositePk sourcePk, boolean excludeSelf)
	{
		TreeSet<FacilityIdIntegerCompositePk> links = new TreeSet<FacilityIdIntegerCompositePk>();
		links.add(sourcePk);

		links = getAllLinkedDemographics(links, new TreeSet<FacilityIdIntegerCompositePk>());

		if (excludeSelf) links.remove(sourcePk);

		return(links);
	}

	/**
	 * excludes self/sourcePk.
	 */
	private Set<FacilityIdIntegerCompositePk> addSameHealthNumberIndividuals(Set<FacilityIdIntegerCompositePk> links, FacilityIdIntegerCompositePk sourcePk, Set<FacilityIdIntegerCompositePk> excludeList)
	{
		CachedDemographic cachedDemographic=cachedDemographicDao.find(sourcePk);
		if (cachedDemographic!=null && cachedDemographic.getHin()!=null && cachedDemographic.getHinType()!=null)
		{
			List<CachedDemographic> sameHCs=cachedDemographicDao.findByHealthNumber(cachedDemographic.getHin(), cachedDemographic.getHinType());
			for (CachedDemographic cachedDemographicTemp : sameHCs)
			{
				if (sourcePk.equals(cachedDemographicTemp.getId())) continue;
				if (excludeList!=null && excludeList.contains(cachedDemographicTemp.getId())) continue;
				links.add(cachedDemographicTemp.getId());
			}
		}
		
		return(links);
	}

	private TreeSet<FacilityIdIntegerCompositePk> getAllLinkedDemographics(Set<FacilityIdIntegerCompositePk> sourcePks, TreeSet<FacilityIdIntegerCompositePk> knownLinks)
	{
		knownLinks.addAll(sourcePks);

		for (FacilityIdIntegerCompositePk tempPk : sourcePks)
		{
			Set<FacilityIdIntegerCompositePk> tempResults = getDirectlyLinkedDemographics(tempPk, knownLinks);
			knownLinks.addAll(getAllLinkedDemographics(tempResults, knownLinks));
		}

		return(knownLinks);
	}

	public Set<FacilityIdIntegerCompositePk> getDirectlyLinkedDemographics(FacilityIdIntegerCompositePk sourcePk, Set<FacilityIdIntegerCompositePk> excludeList)
	{
		HashSet<FacilityIdIntegerCompositePk> pkResults = new HashSet<FacilityIdIntegerCompositePk>();

		addSameHealthNumberIndividuals(pkResults, sourcePk, excludeList);			

		// directly linked demographics has 2 sets of data, one where the demographic appears
		// in the first column, then when it appears in the second, i.e. looking for 'a' can be (a,b) (a,c) (a,d) and (g,a), (h,a) etc..
		getDirectlyLinkedDemographicsFirstColumn(sourcePk, excludeList, pkResults);
		getDirectlyLinkedDemographicsSecondColumn(sourcePk, excludeList, pkResults);

		return(pkResults);
	}

	private void getDirectlyLinkedDemographicsSecondColumn(FacilityIdIntegerCompositePk sourcePk, Set<FacilityIdIntegerCompositePk> excludeList, Set<FacilityIdIntegerCompositePk> pkResults)
	{
		// get it where source user is in the second set of columns
		Query query = entityManager.createNativeQuery("select integratorDemographicFacilityId1,caisiDemographicId1 from " + modelClass.getSimpleName() + " where integratorDemographicFacilityId2=?1 and caisiDemographicId2=?2");
		query.setParameter(1, sourcePk.getIntegratorFacilityId());
		query.setParameter(2, sourcePk.getCaisiItemId());

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (Object[] temp : results)
		{
			FacilityIdIntegerCompositePk tempPk = new FacilityIdIntegerCompositePk((Integer)temp[0], (Integer)temp[1]);

			if (excludeList == null || !excludeList.contains(tempPk))
				pkResults.add(tempPk);
		}
	}

	private void getDirectlyLinkedDemographicsFirstColumn(FacilityIdIntegerCompositePk sourcePk, Set<FacilityIdIntegerCompositePk> excludeList, Set<FacilityIdIntegerCompositePk> pkResults)
	{
		// get it where source user is in the first set of columns
		Query query = entityManager.createNativeQuery("select integratorDemographicFacilityId2,caisiDemographicId2 from " + modelClass.getSimpleName() + " where integratorDemographicFacilityId1=?1 and caisiDemographicId1=?2");
		query.setParameter(1, sourcePk.getIntegratorFacilityId());
		query.setParameter(2, sourcePk.getCaisiItemId());
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (Object[] temp : results)
		{
			FacilityIdIntegerCompositePk tempPk = new FacilityIdIntegerCompositePk((Integer)temp[0], (Integer)temp[1]);

			if (excludeList == null || !excludeList.contains(tempPk))
				pkResults.add(tempPk);
		}
	}

	/**
	 * will return an entry if the two pk's are linked regardless of the ordering of the pair i.e. (pk1,pk2) matches (pk2,pk1) 
	 */
	public DemographicLink findByPair(FacilityIdIntegerCompositePk pk1, FacilityIdIntegerCompositePk pk2)
	{
		DemographicLink demographicLink = findExactPair(pk1, pk2);

		// check for reverse pair
		if (demographicLink == null)
			demographicLink = findExactPair(pk2, pk1);

		return(demographicLink);
	}

	/**
	 * This method does a straight match for the entry, i.e. (pk1,pk2) will not match (pk2,pk1)
	 * this method is intended to be called internally by the findByPair method only.
	 */
	private DemographicLink findExactPair(FacilityIdIntegerCompositePk pk1, FacilityIdIntegerCompositePk pk2)
	{
		String sqlCommand = "select x from " + modelClass.getSimpleName() + " x where x.integratorDemographicFacilityId1=?1 and x.caisiDemographicId1=?2 and x.integratorDemographicFacilityId2=?3 and x.caisiDemographicId2=?4";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, pk1.getIntegratorFacilityId());
		query.setParameter(2, pk1.getCaisiItemId());
		query.setParameter(3, pk2.getIntegratorFacilityId());
		query.setParameter(4, pk2.getCaisiItemId());

		return(getSingleResultOrNull(query));
	}
}
