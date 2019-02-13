package org.oscarehr.caisi_integrator.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.dao.EventLog.ActionPrefix;
import org.oscarehr.caisi_integrator.dao.EventLog.DataActionValue;
import org.oscarehr.caisi_integrator.util.LoggedInInfo;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * We don't want logging in the same transaction as regular work.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EventLogDao extends AbstractDao<EventLog>
{
	public EventLogDao()
	{
		super(EventLog.class);
	}

	/**
	 * This is part of a series of convenience methods for logging events.
	 * The source is pulled off a thread local variable.
	 * The action is the action qualifier i.e. x in "LOGIC:x" the logic prefix will be prepended automatically.
	 */
	public EventLog createLogicEventEntry(String logicActionQualifier, String parameters)
	{
		return(createEventEntry(ActionPrefix.LOGIC.name() + MiscUtils.ID_SEPARATOR + logicActionQualifier, parameters));
	}

	/**
	 * This is part of a series of convenience methods for logging events.
	 * The source is pulled off a thread local variable.
	 */
	public EventLog createDataReadEventEntry(AbstractModel<?> modelObject)
	{
		if (modelObject == null) return(null);

		return(createEventEntry(ActionPrefix.DATA.name() + MiscUtils.ID_SEPARATOR + DataActionValue.READ.name(), modelObject.getClass().getSimpleName() + MiscUtils.ID_SEPARATOR + modelObject.getId()));
	}

	/**
	 * This is part of a series of convenience methods for logging events.
	 * The source is pulled off a thread local variable.
	 */
	public <T extends AbstractModel<?>> void createDataReadEventEntries(Collection<T> modelObjects)
	{
		for (T modelObject : modelObjects)
		{
			createDataReadEventEntry(modelObject);
		}
	}

	/**
	 * This is part of a series of convenience methods for logging events.
	 * The source is pulled off a thread local variable.
	 */
	public EventLog createDataSearchEventEntry(String parameters)
	{
		return(createEventEntry(ActionPrefix.DATA.name() + MiscUtils.ID_SEPARATOR + DataActionValue.SEARCH_RESULT.name(), parameters));
	}

	/**
	 * This is part of a series of convenience methods for logging events.
	 * The source is pulled off a thread local variable.
	 */
	public EventLog createDataEventEntry(DataActionValue dataActionValue, AbstractModel<?> modelObject)
	{
		if (modelObject == null) return(null);

		return(createEventEntry(ActionPrefix.DATA.name() + MiscUtils.ID_SEPARATOR + dataActionValue.name(), modelObject.getClass().getSimpleName() + MiscUtils.ID_SEPARATOR + modelObject.getId()));
	}

	/**
	 * This is a convenience method used to log performance statistics.
	 */
	public EventLog createPerformanceEventEntry(String methodInvoked, long nanosTaken)
	{
		return(createEventEntry(ActionPrefix.PERFORMANCE.name() + MiscUtils.ID_SEPARATOR + methodInvoked, String.valueOf(nanosTaken)));
	}

	/**
	 * This is part of a series of convenience methods for logging events.
	 * The source is pulled off a thread local variable.
	 * A full action string is like "DATA:READ" where it includes the Action prefix.
	 */
	private EventLog createEventEntry(String fullActionString, String parameters)
	{
		LoggedInInfo loggedInfo = LoggedInInfo.loggedInInfo.get();

		EventLog eventLog = new EventLog();

		if (loggedInfo!=null) eventLog.setSource(loggedInfo.getSourceString());
		else eventLog.setSource(LoggedInInfo.PUBLIC_SOURCE_STRING);
		
		eventLog.setAction(fullActionString);
		eventLog.setParameters(parameters);
		persist(eventLog);

		return(eventLog);
	}

	@Override
	public void refresh(EventLog o)
	{
		throw(new UnsupportedOperationException("can't refresh an unupdateable object"));
	}

	public List<EventLog> findAll(int startIndex, int itemsToReturn)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " limit " + startIndex + ',' + itemsToReturn, modelClass);

		@SuppressWarnings("unchecked")
		List<EventLog> results = query.getResultList();

		return(results);
	}

	public List<EventLog> findByActionParametersSource(String action, String parameters, String source, int startIndex, int itemsToReturn)
	{
		Query query = entityManager.createNativeQuery("select * from " + modelClass.getSimpleName() + " where action like ?1 and parameters like ?2 and source like ?3 limit " + startIndex + ',' + itemsToReturn, modelClass);

		query.setParameter(1, action);
		query.setParameter(2, parameters);
		query.setParameter(3, source);

		@SuppressWarnings("unchecked")
		List<EventLog> results = query.getResultList();

		return(results);
	}

	public int getCountByActionParametersSource(String action, String parameters, String source)
	{
		Query query = entityManager.createNativeQuery("select count(*) from " + modelClass.getSimpleName() + " where action like ?1 and parameters like ?2 and source like ?3", Integer.class);

		query.setParameter(1, action);
		query.setParameter(2, parameters);
		query.setParameter(3, source);

		return((Integer)query.getSingleResult());
	}
}
