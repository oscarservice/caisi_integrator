package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class ReferralTest extends DaoTestFixtures
{
	private CachedDemographicDao cachedDemographicDao = (CachedDemographicDao)SpringUtils.getBean("cachedDemographicDao");
	private CachedProgramDao cachedProgramDao = (CachedProgramDao)SpringUtils.getBean("cachedProgramDao");
    private CachedProviderDao cachedProviderDao=(CachedProviderDao)SpringUtils.getBean("cachedProviderDao");
	private ReferralDao referralDao = (ReferralDao)SpringUtils.getBean("referralDao");

	@Test
	public void referralTest()
	{
		// --- create ---
		// program
		{
			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setIntegratorFacilityId(facility1.getId());
			pk.setCaisiItemId(994);

			CachedProgram cachedProgram = new CachedProgram();
			cachedProgram.setFacilityIdIntegerCompositePk(pk);
			cachedProgram.setDescription("the desc");
			cachedProgram.setName("the name");
			cachedProgram.setStatus("active");
			cachedProgram.setType("bed");
			cachedProgramDao.persist(cachedProgram);
		}

		// demographic
		{
			FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
			pk.setIntegratorFacilityId(facility2.getId());
			pk.setCaisiItemId(455);

			CachedDemographic cachedDemographic = new CachedDemographic();
			cachedDemographic.setFacilityIdIntegerCompositePk(pk);
			cachedDemographic.setFirstName("bob ");
			cachedDemographic.setLastName("Dole");
			cachedDemographicDao.persist(cachedDemographic);
		}

		// provider
		{
			FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
			pk.setIntegratorFacilityId(facility2.getId());
			pk.setCaisiItemId("674");

			CachedProvider cachedProvider = new CachedProvider();
			cachedProvider.setFacilityIdStringCompositePk(pk);
			cachedProviderDao.persist(cachedProvider);
		}

		Referral referral = new Referral();
		referral.setDestinationIntegratorFacilityId(facility1.getId());
		referral.setDestinationCaisiProgramId(994);
		referral.setPresentingProblem("my problem");
		referral.setReasonForReferral("my reason for referral");
		referral.setReferralDate(new Date());
		referral.setSourceCaisiDemographicId(455);
		referral.setSourceIntegratorFacilityId(facility2.getId());
		referral.setSourceCaisiProviderId("674");

		referralDao.persist(referral);

		// --- find ---
		Referral referral1 = referralDao.find(referral.getId());

		assertEquals("my problem", referral1.getPresentingProblem());
		assertEquals("my reason for referral", referral1.getReasonForReferral());
		assertEquals(994, referral1.getDestinationCaisiProgramId().longValue());
		assertEquals(455, referral1.getSourceCaisiDemographicId().longValue());
		assertEquals("674", referral1.getSourceCaisiProviderId());

		// --- update ---
		referral1.setPresentingProblem("update problem");
		referralDao.merge(referral1);

		Referral referral2 = referralDao.find(referral.getId());
		assertEquals("update problem", referral2.getPresentingProblem());

		// --- delete ---
		referralDao.remove(referral1.getId());
		Referral Referral3 = referralDao.find(referral.getId());
		assertNull(Referral3);
	}

}
