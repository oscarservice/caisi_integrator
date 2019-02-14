package org.oscarehr.caisi_integrator.ws.transfer;

import java.io.Serializable;
import java.util.ArrayList;

import org.oscarehr.caisi_integrator.dao.CachedProvider;
import org.oscarehr.caisi_integrator.util.Role;

public class ProviderTransfer implements Serializable
{
	private static final long serialVersionUID = 8127876095215434516L;
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
