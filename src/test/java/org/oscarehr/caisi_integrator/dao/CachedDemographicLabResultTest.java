package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicLabResultTest extends DaoTestFixtures
{
	private CachedDemographicLabResultDao cachedDemographicLabResultDao = (CachedDemographicLabResultDao)SpringUtils.getBean("cachedDemographicLabResultDao");

	@Test
	public void demographicLabResultTest()
	{
		// create
		FacilityIdLabResultCompositePk pk = new FacilityIdLabResultCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setLabResultId("blahblah");

		CachedDemographicLabResult labResult = new CachedDemographicLabResult();
		labResult.setFacilityIdLabResultCompositePk(pk);
		labResult.setCaisiDemographicId(1234);
		labResult.setData("my data");
		labResult.setType("my type");

		cachedDemographicLabResultDao.persist(labResult);

		// test find 
		CachedDemographicLabResult result = cachedDemographicLabResultDao.find(pk);
		assertEquals(1234, result.getCaisiDemographicId());
		assertEquals("my data", result.getData());
		assertEquals("my type", result.getType());
		
		List<CachedDemographicLabResult> results=cachedDemographicLabResultDao.findByFacilityIdAndDemographicId(facility1.getId(), 1234);
		assertTrue(results.size()>0);

	}

}
