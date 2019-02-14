package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;

public class IntegratorFileFooter implements Serializable
{
	private int checksum = -1;

	public IntegratorFileFooter()
	{

	}

	public IntegratorFileFooter(int checksum)
	{
		this.checksum = checksum;
	}

	public int getChecksum()
	{
		return checksum;
	}

	public void setChecksum(int checksum)
	{
		this.checksum = checksum;
	}

}
