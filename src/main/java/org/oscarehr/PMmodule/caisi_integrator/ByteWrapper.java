package org.oscarehr.PMmodule.caisi_integrator;

import java.io.Serializable;

public class ByteWrapper implements Serializable
{

	private byte[] data;

	public ByteWrapper()
	{

	}

	public ByteWrapper(byte[] data)
	{
		this.data = data;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

}
