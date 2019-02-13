package org.oscarehr.caisi_integrator.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.caisi_integrator.util.AccumulatorMap;
import org.springframework.stereotype.Repository;

@Repository
public class CachedDemographicDao extends AbstractDao<CachedDemographic>
{
	public CachedDemographicDao()
	{
		super(CachedDemographic.class);
	}
	
	/**
	 * Find people who might be the same person from other facilities
	 * @param excludeIntegratorFacilityId this is so the callers own facility is excluded, you can pass in null to include all.
	 * @return a list of Map.Entry<CachedDemographic, Integer> where Integer is their score, it is sorted by highest score first.
	 */
	public ArrayList<MatchingCachedDemographicScore> findMatchingDemographics(Integer excludeIntegratorFacilityId, int maxEntriesToReturn, int minimumScore, String firstName, String lastName,
			Calendar birthDate, String hin, String gender, String sin, String province, String city)
	{
		// trim parameters to null, avoids giving points to "" matching ""
		firstName=StringUtils.trimToNull(firstName);
		lastName=StringUtils.trimToNull(lastName);
		hin=StringUtils.trimToNull(hin);
		gender=StringUtils.trimToNull(gender);
		sin=StringUtils.trimToNull(sin);
		province=StringUtils.trimToNull(province);
		city=StringUtils.trimToNull(city);
				
		//------------
		// score board
		//------------
		// we want the total max score to be 100 (or really close but less) for easy end user comprehension
		
		String baseSqlStatement="select * from "+modelClass.getSimpleName()+" where integratorFacilityId<>?";
		
		// this map contains demographicIds.
		AccumulatorMap<CachedDemographic> scoreMap=new AccumulatorMap<CachedDemographic>();
		
		if (hin!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 22, baseSqlStatement+" and hin=?", hin);
		if (sin!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 20, baseSqlStatement+" and sin=?", sin);

		// keep in mind the name matching is cumulative, i.e. an exact match should match 3 times here for 3 times the score.
		if (firstName!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 6, baseSqlStatement+" and firstName sounds like ?", firstName);
		if (lastName!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 7, baseSqlStatement+" and lastName sounds like ?", lastName);
		if (firstName!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 5, baseSqlStatement+" and firstName like ?", "%"+firstName+"%");
		if (lastName!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 2, baseSqlStatement+" and lastName like ?", "%"+lastName+"%");
		if (firstName!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 6, baseSqlStatement+" and firstName=?", firstName);
		if (lastName!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 7, baseSqlStatement+" and lastName=?", lastName);

		if (birthDate!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 15, baseSqlStatement+" and birthDate=?", birthDate);
		if (gender!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 5, baseSqlStatement+" and gender=?", gender);
		if (province!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 2, baseSqlStatement+" and province=?", province);
		if (city!=null) findAndAddScore(excludeIntegratorFacilityId, scoreMap, 3, baseSqlStatement+" and city=?", city);
		
		List<Entry<CachedDemographic, Integer>> sorted=scoreMap.getEntriesSortedByValue(false).subList(0, Math.min(scoreMap.size(), maxEntriesToReturn));

		Iterator<Entry<CachedDemographic, Integer>> it=sorted.iterator();
		ArrayList<MatchingCachedDemographicScore> results=new ArrayList<MatchingCachedDemographicScore>();
		while (it.hasNext()) 
		{
			Entry<CachedDemographic, Integer> entry = it.next();
			if (entry.getValue()>=minimumScore)
			{
				MatchingCachedDemographicScore x=new MatchingCachedDemographicScore();
				x.cachedDemographic=entry.getKey();
				x.score=entry.getValue();
				results.add(x);
			}
		}
		
		return(results);
	}

    private void findAndAddScore(Integer excludeFacilityId, AccumulatorMap<CachedDemographic> scoreMap, int score, String sqlCommand, Object parameter)
	{
    	Query query = entityManager.createNativeQuery(sqlCommand,modelClass);
		int counter=1;
    	
    	if (excludeFacilityId==null) query.setParameter(counter++, -1);
    	else query.setParameter(counter++, excludeFacilityId);

		if (parameter instanceof Calendar) query.setParameter(counter++, (Calendar)parameter, TemporalType.DATE);
		else query.setParameter(counter++, parameter);
		
		@SuppressWarnings("unchecked")
		List<CachedDemographic> results=query.getResultList();
		for (CachedDemographic cachedDemographic : results)
		{
			scoreMap.increment(cachedDemographic, score);
		}
	}
	

	public List<CachedDemographic> findByHealthNumber(String hin, String hinType)
	{
		String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.hin=?1 and x.hinType=?2";
		
    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, hin);
		query.setParameter(2, hinType);

		@SuppressWarnings("unchecked")
		List<CachedDemographic> results=query.getResultList();			
		return(results);
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

	public List<FacilityIdIntegerCompositePk> findAllIds()
	{
		String sqlCommand="select x.facilityDemographicPk from "+modelClass.getSimpleName()+" x";
		
    	Query query = entityManager.createQuery(sqlCommand);

		@SuppressWarnings("unchecked")
		List<FacilityIdIntegerCompositePk> results=query.getResultList();			
		return(results);
	}

        public void makeHash(CachedDemographic cachedDemographic)
        {
            if (cachedDemographic.getBirthDate()==null || nothingIn(cachedDemographic.getHin()))
            {
                String id1=cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId().toString();
                String id2=cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId().toString();
                cachedDemographic.setIdHash(id1+"_"+id2);
            }
            else
            {
                try
                {
                    String id = cachedDemographic.getHin()+cachedDemographic.getBirthDate();
                    MessageDigest m = MessageDigest.getInstance("MD5");
                    byte[] data = id.getBytes();
                    m.update(data, 0, data.length);
                    BigInteger i1 = new BigInteger(1, m.digest());
                    BigInteger i2 = BigInteger.valueOf(cachedDemographic.getBirthDate().hashCode());
                    cachedDemographic.setIdHash(String.format("%1$032X", i1.add(i2)));
                }
                catch (NoSuchAlgorithmException ex)
                {
                    Logger.getLogger(CachedDemographicDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            cachedDemographic.setFirstName(null);
            cachedDemographic.setLastName(null);
            cachedDemographic.setSin(null);
            cachedDemographic.setHin(null);
            cachedDemographic.setHinVersion(null);
            cachedDemographic.setHinType(null);
        }

        private boolean nothingIn(String s)
        {
            return (s==null || s.trim().equals(""));
        }
}
