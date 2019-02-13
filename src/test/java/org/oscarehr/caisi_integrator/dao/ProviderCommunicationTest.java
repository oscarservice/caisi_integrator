package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class ProviderCommunicationTest extends DaoTestFixtures
{
	private ProviderCommunicationDao providerCommunicationDao = (ProviderCommunicationDao)SpringUtils.getBean("providerCommunicationDao");

	@Test
	public void providerCommunicationTest()
	{
		ProviderCommunication providerCommunication = new ProviderCommunication();
		providerCommunication.setData("this brown cow".getBytes());
		providerCommunication.setDestinationIntegratorFacilityId(1111);
		providerCommunication.setDestinationProviderId("aaaa");
		providerCommunication.setSourceIntegratorFacilityId(2222);
		providerCommunication.setSourceProviderId("bbbb");
		providerCommunication.setType("test type");

		providerCommunicationDao.persist(providerCommunication);

		// --- find ---
		ProviderCommunication providerCommunication1 = providerCommunicationDao.find(providerCommunication.getId());

		assertEquals("this brown cow", new String(providerCommunication1.getData()));
		assertEquals(1111, providerCommunication1.getDestinationIntegratorFacilityId().intValue());
		assertEquals("aaaa", providerCommunication1.getDestinationProviderId());
		assertEquals(2222, providerCommunication1.getSourceIntegratorFacilityId().intValue());
		assertEquals("bbbb", providerCommunication1.getSourceProviderId());
		assertEquals("test type", providerCommunication1.getType());

		// --- find by type ---
		List<ProviderCommunication> findResults=providerCommunicationDao.findByDestinationFacilityAndProviderIdAndType(1111, "aaaa", "test type", true);
		assertTrue(findResults.size()>0);
	}

}
