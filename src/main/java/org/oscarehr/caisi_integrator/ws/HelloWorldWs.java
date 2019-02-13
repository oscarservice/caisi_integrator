package org.oscarehr.caisi_integrator.ws;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

@WebService
@Component
public class HelloWorldWs extends AbstractWs
{
	// http://127.0.0.1:8080/caisi_integrator/ws/HelloWorld/helloWorld
	public String helloWorld()
	{
		return("Hello World! " + (new java.util.Date()));
	}

	// http://127.0.0.1:8080/caisi_integrator/ws/HelloWorld/helloWorld2?name=foo
	public String helloWorld2(String name)
	{
		return("Hello " + name + "! " + (new java.util.Date()));
	}
}
