package org.oscarehr.caisi_integrator.site.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass=Integer.class, value="hideLessThan5Converter")
public class HideLessThan5Converter implements Converter
{
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
	{
		throw(new UnsupportedOperationException("Not implemented yet."));
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
	{
		Integer i=(Integer)o;
		
		if (i>=5) return(i.toString());
		else return("<5");
	}
}
