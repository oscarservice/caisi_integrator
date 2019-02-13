package org.oscarehr.caisi_integrator.exception;

public class CaisiException extends Exception
{
	public CaisiException()
	{
		// empty constructor
	}

	public CaisiException(String message)
	{
		super(message);
	}

	public CaisiException(Throwable cause)
	{
		super(cause);
	}

	public CaisiException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
