package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;
import java.util.List;

import org.oscarehr.caisi_integrator.dao.FacilityIdDemographicIssueCompositePk;

public class DeleteCachedDemographicIssuesWrapper implements Serializable
{

	private Integer demographicNo;
	private List<FacilityIdDemographicIssueCompositePk> keys;

	public DeleteCachedDemographicIssuesWrapper()
	{

	}

	public DeleteCachedDemographicIssuesWrapper(Integer demographicNo, List<FacilityIdDemographicIssueCompositePk> keys)
	{
		this.demographicNo = demographicNo;
		this.keys = keys;
	}

	public Integer getDemographicNo()
	{
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo)
	{
		this.demographicNo = demographicNo;
	}

	public List<FacilityIdDemographicIssueCompositePk> getKeys()
	{
		return keys;
	}

	public void setKeys(List<FacilityIdDemographicIssueCompositePk> keys)
	{
		this.keys = keys;
	}

}
