package org.oscarehr.caisi_integrator.site.ui.admin;

import java.util.ArrayList;
import java.util.Collections;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;
import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;
import org.oscarehr.caisi_integrator.util.Named;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class ViewFacilitiesJsfBean
{
	@Transient
	private FacilityDao facilityDao = (FacilityDao)SpringUtils.getBean("facilityDao");

	public ListDataModel<Facility> getAllFacilities()
	{
		// it's re-wrapped so I can sort it, JPA collections are immutable
		ArrayList<Facility> allfacilities = new ArrayList<Facility>(facilityDao.findAll());
		Collections.sort(allfacilities, Named.NAME_COMPARATOR);
		
		ListDataModel<Facility> results = new ListDataModel<Facility>(allfacilities);
		return(results);
	}
}
