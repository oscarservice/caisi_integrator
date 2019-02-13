package org.oscarehr.caisi_integrator.ws.client_tests;

import org.oscarehr.caisi_integrator.ws.HelloWorldWs;
import org.oscarehr.caisi_integrator.ws.HelloWorldWsService;
import org.oscarehr.caisi_integrator.util.CxfClientUtils;

public class HelloWorldTestClient
{
	public static void main(String... argv) throws Exception
	{
		HelloWorldWsService service = new HelloWorldWsService();
		HelloWorldWs helloWorld = service.getHelloWorldWsPort();
		
		CxfClientUtils.configureClientConnection(helloWorld);
		
		System.err.println(helloWorld.helloWorld());
		System.err.println(helloWorld.helloWorld2("foo"));
	}
}
