package org.oscarehr.caisi_integrator.exception;

public class IntegrationException extends CaisiException
{
	public IntegrationException()
	{
		// empty constructor
	}

	public IntegrationException(String message)
	{
		super(message);
	}

	public IntegrationException(Throwable cause)
	{
		super(cause);
	}

	public IntegrationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
