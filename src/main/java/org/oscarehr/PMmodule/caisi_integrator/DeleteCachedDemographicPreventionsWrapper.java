package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;
import java.util.List;

public class DeleteCachedDemographicPreventionsWrapper implements Serializable
{

	private Integer demographicNo;
	private List<Integer> nonDeletedIds;

	public DeleteCachedDemographicPreventionsWrapper()
	{
	}

	public DeleteCachedDemographicPreventionsWrapper(Integer demographicNo, List<Integer> nonDeletedIds)
	{
		this.demographicNo = demographicNo;
		this.nonDeletedIds = nonDeletedIds;
	}

	public Integer getDemographicNo()
	{
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo)
	{
		this.demographicNo = demographicNo;
	}

	public List<Integer> getNonDeletedIds()
	{
		return nonDeletedIds;
	}

	public void setNonDeletedIds(List<Integer> nonDeletedIds)
	{
		this.nonDeletedIds = nonDeletedIds;
	}

}
