package org.oscarehr.caisi_integrator.util;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;

public class IntegratorGZIPInInterceptor extends GZIPInInterceptor
{
	public IntegratorGZIPInInterceptor()
	{
		super();
	}

	public void handleMessage(Message message) throws Fault
	{
		String enableCompression = ConfigXmlUtils.getPropertyString("integrator_server", "compression");

		if (enableCompression != null && enableCompression.equals("true"))
		{
			super.handleMessage(message);
		}
	}

}
