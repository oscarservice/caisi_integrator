package org.oscarehr.caisi_integrator.site.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class JsfUtils
{
	public static final String REDIRECT_QUERY_STRING="faces-redirect=true";
	
	public static String getRequestParameter(String key)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		return(externalContext.getRequestParameterMap().get(key));
	}

	public static HttpServletRequest getHttpServletRequest()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		return((HttpServletRequest)externalContext.getRequest());
	}

	public static HttpSession getSession()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		return((HttpSession)externalContext.getSession(true));
	}

	private static void addMessage(FacesMessage.Severity severity, String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		if (fc != null) fc.addMessage(null, new FacesMessage(severity, message, message));
	}

	public static void addErrorMessage(String message)
	{
		addMessage(FacesMessage.SEVERITY_ERROR, message);
	}

	public static void addInfoMessage(String message)
	{
		addMessage(FacesMessage.SEVERITY_INFO, message);
	}
	
	public static String getContextPath()
	{
		return(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
	}
	
	/**
	 * Gets a managed object search order is from request, to session, to application.
	 * Note it appears that request objects don't actually show up in the request scope... dunno why
	 * 
	 * @param name
	 * @return
	 */
	public static Object getObject(FacesContext context, String name)
	{
		ExternalContext externalContext = context.getExternalContext();

		Object o = externalContext.getRequestMap().get(name);
		if (o == null) o = externalContext.getSessionMap().get(name);
		if (o == null) o = externalContext.getApplicationMap().get(name);

		return(o);
	}

}