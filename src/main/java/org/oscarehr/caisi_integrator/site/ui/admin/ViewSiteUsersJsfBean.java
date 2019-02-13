package org.oscarehr.caisi_integrator.site.ui.admin;

import java.util.ArrayList;
import java.util.Collections;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;
import javax.persistence.Transient;

import org.oscarehr.caisi_integrator.dao.SiteUser;
import org.oscarehr.caisi_integrator.dao.SiteUserDao;
import org.oscarehr.caisi_integrator.util.Named;
import org.oscarehr.caisi_integrator.util.SpringUtils;

@ManagedBean
public class ViewSiteUsersJsfBean
{
	@Transient
	private SiteUserDao siteUserDao = (SiteUserDao)SpringUtils.getBean("siteUserDao");

	public ListDataModel<SiteUser> getAllSiteUsers()
	{
		// it's re-wrapped so I can sort it, JPA collections are immutable
		ArrayList<SiteUser> allSiteUsers = new ArrayList<SiteUser>(siteUserDao.findAll());
		Collections.sort(allSiteUsers, Named.NAME_COMPARATOR);
		
		ListDataModel<SiteUser> results = new ListDataModel<SiteUser>(allSiteUsers);
		return(results);
	}
}
