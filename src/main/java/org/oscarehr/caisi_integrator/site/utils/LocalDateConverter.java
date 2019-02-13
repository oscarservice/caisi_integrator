package org.oscarehr.caisi_integrator.site.utils;

import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.time.DateFormatUtils;

@FacesConverter(forClass=Calendar.class, value="localDateConverter")
public class LocalDateConverter implements Converter
{
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
	{
		throw(new UnsupportedOperationException("Not implemented yet."));
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
	{
		Calendar cal=(Calendar)o;
		return(DateFormatUtils.ISO_DATE_FORMAT.format(cal));
	}
}
