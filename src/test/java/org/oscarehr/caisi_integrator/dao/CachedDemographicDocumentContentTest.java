package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicDocumentContentTest extends DaoTestFixtures
{
	private CachedDemographicDocumentContentsDao cachedDemographicDocumentContentsDao = (CachedDemographicDocumentContentsDao)SpringUtils.getBean("cachedDemographicDocumentContentsDao");

	@Test
	public void demographicAllergyTest()
	{
		// create
		FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(543);

		CachedDemographicDocumentContents documentContents = new CachedDemographicDocumentContents();
		documentContents.setFacilityIntegerCompositePk(pk);
		documentContents.setFileContents("my file contents blah blah blah".getBytes());

		cachedDemographicDocumentContentsDao.persist(documentContents);

		// test find 
		CachedDemographicDocumentContents result = cachedDemographicDocumentContentsDao.find(pk);
		assertNotNull(result.getFileContents());
	}

}
