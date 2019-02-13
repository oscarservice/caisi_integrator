package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicAllergyTest extends DaoTestFixtures
{
	private CachedDemographicAllergyDao cachedDemographicAllergyDao = (CachedDemographicAllergyDao)SpringUtils.getBean("cachedDemographicAllergyDao");

	@Test
	public void demographicAllergyTest()
	{
		// create
		FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(543);

		CachedDemographicAllergy allergy = new CachedDemographicAllergy();
		allergy.setFacilityIdIntegerCompositePk(pk);
		allergy.setDescription("my description");
		allergy.setEntryDate(new Date());
		allergy.setReaction("my reaction");
		allergy.setCaisiDemographicId(9987);

		cachedDemographicAllergyDao.persist(allergy);

		// test find 
		CachedDemographicAllergy result = cachedDemographicAllergyDao.find(pk);
		assertEquals("my description", result.getDescription());
		assertNotNull(result.getEntryDate());
		assertEquals("my reaction", result.getReaction());
		
		List<CachedDemographicAllergy> results=cachedDemographicAllergyDao.findByFacilityIdAndDemographicId(facility1.getId(), 9987);
		assertTrue(results.size()>0);
	}

}
