package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;
import java.util.List;

public class ProgramDeleteIdWrapper implements Serializable
{

	List<Integer> ids = null;

	public ProgramDeleteIdWrapper()
	{

	}

	public ProgramDeleteIdWrapper(List<Integer> ids)
	{
		this.ids = ids;
	}

	public List<Integer> getIds()
	{
		return ids;
	}

	public void setIds(List<Integer> ids)
	{
		this.ids = ids;
	}

}
