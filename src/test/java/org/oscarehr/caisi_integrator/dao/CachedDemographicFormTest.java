package org.oscarehr.caisi_integrator.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.oscarehr.caisi_integrator.util.SpringUtils;

public class CachedDemographicFormTest extends DaoTestFixtures
{
	private CachedDemographicFormDao cachedDemographicFormDao=(CachedDemographicFormDao)SpringUtils.getBean("cachedDemographicFormDao");
	
	@Test
	public void cachedDemographicFormTest()
	{
		FacilityIdIntegerCompositePk pk=new FacilityIdIntegerCompositePk();
		pk.setIntegratorFacilityId(facility1.getId());
		pk.setCaisiItemId(4523);
		
		CachedDemographicForm cachedDemographicForm=new CachedDemographicForm();
		cachedDemographicForm.setFacilityIdIntegerCompositePk(pk);
		cachedDemographicForm.setCaisiDemographicId(543);
		cachedDemographicForm.setCaisiProviderId("624");
		cachedDemographicForm.setEditDate(new Date());
		cachedDemographicForm.setFormName("test form");
		cachedDemographicForm.setFormData("test form data");
		
		cachedDemographicFormDao.persist(cachedDemographicForm);
		
		
		// --- find ---
		CachedDemographicForm form=cachedDemographicFormDao.find(pk);
		assertEquals(543, form.getCaisiDemographicId().intValue());
		assertEquals("624", form.getCaisiProviderId());
		
		List<CachedDemographicForm> forms=cachedDemographicFormDao.findByFacilityDemographicIdFormName(form.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), form.getCaisiDemographicId(), "test form");
		assertTrue(forms.size()>0);		
	}
}
