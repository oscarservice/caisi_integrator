package org.oscarehr.caisi_integrator.util;

import java.util.GregorianCalendar;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.oscarehr.caisi_integrator.dao.Facility;
import org.oscarehr.caisi_integrator.dao.FacilityDao;

public class FacilityUpdateInInterceptor extends AbstractPhaseInterceptor<Message>
{
	private FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
	private EventLogDao eventLogDao = (EventLogDao) SpringUtils.getBean("eventLogDao");

	public FacilityUpdateInInterceptor()
	{
		super(Phase.PRE_INVOKE);
	}

	public void handleMessage(Message message) throws Fault
	{

		LoggedInInfoWebService loggedInInfo = (LoggedInInfoWebService) LoggedInInfo.loggedInInfo.get();
		//load the facility
		Facility facility = loggedInInfo.getFacility();

		if (facility != null)
		{

			// update last login
			facility.setLastLogin(new GregorianCalendar());
			facilityDao.merge(facility);

			eventLogDao.createLogicEventEntry("LOGIN" + MiscUtils.ID_SEPARATOR + "SUCCESS", "IP:" + loggedInInfo.getRequestingIp());
		}
		else
		{
			MiscUtils.getLogger().error("Facility was null - last login not updated");
		}

	}

}
