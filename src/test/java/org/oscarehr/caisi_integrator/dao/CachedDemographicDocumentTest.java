package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicDocumentTest extends DaoTestFixtures
{
	private CachedDemographicDocumentDao cachedDemographicDocumentDao = (CachedDemographicDocumentDao)SpringUtils.getBean("cachedDemographicDocumentDao");

	@Test
	public void demographicAllergyTest()
	{
		// create
		FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(543);

		CachedDemographicDocument document = new CachedDemographicDocument();
		document.setFacilityIntegerPk(pk);
		document.setContentType("my contentType");
		document.setDocFilename("test filename");
		document.setReviewDateTime(new Date());

		cachedDemographicDocumentDao.persist(document);

		// test find 
		CachedDemographicDocument result = cachedDemographicDocumentDao.find(pk);
		assertEquals("my contentType", result.getContentType());
		assertEquals("test filename", result.getDocFilename());
		assertNotNull(result.getReviewDateTime());
	}

}
