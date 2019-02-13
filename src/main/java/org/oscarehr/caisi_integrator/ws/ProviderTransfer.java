package org.oscarehr.caisi_integrator.ws;

import java.util.ArrayList;

import org.oscarehr.caisi_integrator.dao.CachedProvider;
import org.oscarehr.caisi_integrator.util.Role;

public class ProviderTransfer
{
	private CachedProvider cachedProvider = null;
	private ArrayList<Role> roles = null;

	public CachedProvider getCachedProvider()
	{
		return cachedProvider;
	}

	public void setCachedProvider(CachedProvider cachedProvider)
	{
		this.cachedProvider = cachedProvider;
	}

	public ArrayList<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(ArrayList<Role> roles)
	{
		this.roles = roles;
	}

}
