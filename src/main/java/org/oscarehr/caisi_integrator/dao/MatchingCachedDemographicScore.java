package org.oscarehr.caisi_integrator.dao;

public class MatchingCachedDemographicScore implements Comparable<MatchingCachedDemographicScore>
{
	public CachedDemographic cachedDemographic = null;
	public int score = 0;

	@Override
    public int compareTo(MatchingCachedDemographicScore o)
    {
	    return score-o.score;
    }
}