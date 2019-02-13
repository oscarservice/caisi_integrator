package org.oscarehr.caisi_integrator.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
abstract class AbstractDao<T extends AbstractModel<?>>
{
	protected Class<T> modelClass;

	@PersistenceContext
	protected EntityManager entityManager = null;

	protected AbstractDao(Class<T> modelClass)
	{
		this.modelClass=modelClass;
	}
	
	/**
	 * aka update
	 */
	public void merge(T o)
	{
		entityManager.merge(o);
	}

	/**
	 * aka create
	 */
	public void persist(T o)
	{
		entityManager.persist(o);
	}

	/**
	 * You can only remove attached instances.
	 */
	public void remove(T o)
	{
		entityManager.remove(o);
	}

	/**
     * You can only refresh attached instances.
	 */
	public void refresh(T o)
	{
		entityManager.refresh(o);
	}
	
    public T find(Object id)
	{
		return(entityManager.find(modelClass, id));
	}

	public void remove(Object id)
	{
		T abstractModel=find(id);
		if (abstractModel!=null) remove(abstractModel);
	}

    protected T getSingleResultOrNull(Query query)
	{
		@SuppressWarnings("unchecked")
		List<T> results=query.getResultList();
		if (results.size()==1) return(results.get(0));
		else if (results.size()==0) return(null);
		else throw(new NonUniqueResultException("SingleResult requested but result was not unique : "+results.size()));
	}
	
    protected Object getSingleResultOrNullNoType(Query query)
	{
		List<?> results=query.getResultList();
		if (results.size()==1) return(results.get(0));
		else if (results.size()==0) return(null);
		else throw(new NonUniqueResultException("SingleResult requested but result was not unique : "+results.size()));
	}
	
	public void replace(T o) throws IllegalAccessException, InvocationTargetException
    {
		T existing=find(o.getId());
		
		if (existing==null) persist(o);
		else BeanUtils.copyProperties(existing, o);
    }

	public int getCountAll()
	{
		String sqlCommand="select count(*) from "+modelClass.getSimpleName();
		Query query = entityManager.createNativeQuery(sqlCommand, Integer.class);		
		return((Integer)query.getSingleResult());
	}
}
