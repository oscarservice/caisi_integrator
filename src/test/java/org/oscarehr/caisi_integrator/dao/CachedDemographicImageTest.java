package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicImageTest extends DaoTestFixtures
{
    private CachedDemographicDao cachedDemographicDao=(CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");
    private CachedDemographicImageDao cachedDemographicImageDao=(CachedDemographicImageDao)SpringUtils.getBean("cachedDemographicImageDao");
	
	@Test
	public void demographicImageTest() throws IOException
	{
		// create
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(543);
		
		CachedDemographic cachedDemographic=new CachedDemographic();
		cachedDemographic.setFacilityIdIntegerCompositePk(pk);
		cachedDemographicDao.persist(cachedDemographic);
		
		
		CachedDemographicImage image=new CachedDemographicImage();
		image.setFacilityIdIntegerCompositePk(pk);
		
		InputStream is=getClass().getResourceAsStream("/test_image.jpg");
		byte[] sourceImage=IOUtils.toByteArray(is);
		image.setImage(sourceImage);
		
		image.setUpdateDate(new Date());
		
		cachedDemographicImageDao.persist(image);
		
		// test find 
		CachedDemographicImage result=cachedDemographicImageDao.find(pk);
		assertTrue(result.getImage().length>50); // it can be >0 but since when is an image less than 50 bytes?
			
		// delete
		cachedDemographicImageDao.remove(pk);
		image=cachedDemographicImageDao.find(pk);
		assertNull(image);
	}
	
}
