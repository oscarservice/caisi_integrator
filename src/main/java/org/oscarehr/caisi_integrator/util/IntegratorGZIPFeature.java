package org.oscarehr.caisi_integrator.util;

import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;

/**
 * This class is used to control GZIP compression of messages.
 * Attaching this feature to an endpoint will allow the endpoint to handle
 * compressed requests, and will cause outgoing responses to be compressed if
 * the client indicates (via the Accept-Encoding header) that it can handle
 * them.
 * <pre>
 * <![CDATA[
 * <jaxws:endpoint ...>
 *   <jaxws:features>
 *     <bean class="org.apache.cxf.transport.common.gzip.GZIPFeature"/>
 *   </jaxws:features>
 * </jaxws:endpoint>
 * ]]>
 * </pre>
 * Attaching this feature to a client will cause outgoing request messages 
 * to be compressed and incoming compressed responses to be uncompressed. 
 * Accept-Encoding header is sent to let the service know 
 * that your client can accept compressed responses. 
 */
@NoJSR250Annotations
public class IntegratorGZIPFeature extends AbstractFeature
{

	private static final IntegratorGZIPInInterceptor IN = new IntegratorGZIPInInterceptor();
	private static final IntegratorGZIPOutInterceptor OUT = new IntegratorGZIPOutInterceptor();

	/**
	 * The compression threshold to pass to the outgoing interceptor.
	 */
	int threshold = -1;

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus)
	{
		provider.getInInterceptors().add(IN);
		if (threshold == -1)
		{
			provider.getOutInterceptors().add(OUT);
			provider.getOutFaultInterceptors().add(OUT);
		}
		else
		{
			IntegratorGZIPOutInterceptor out = new IntegratorGZIPOutInterceptor();
			out.setThreshold(threshold);
			remove(provider.getOutInterceptors());
			remove(provider.getOutFaultInterceptors());
			provider.getOutInterceptors().add(out);
			provider.getOutFaultInterceptors().add(out);
		}
	}

	private void remove(List<Interceptor<? extends Message>> outInterceptors)
	{
		int x = outInterceptors.size();
		while (x > 0)
		{
			--x;
			if (outInterceptors.get(x) instanceof GZIPOutInterceptor)
			{
				outInterceptors.remove(x);
			}
		}
	}

	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}

	public int getThreshold()
	{
		return threshold;
	}
}
