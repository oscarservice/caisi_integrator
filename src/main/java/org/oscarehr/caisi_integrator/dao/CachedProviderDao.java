package org.oscarehr.caisi_integrator.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.oscarehr.caisi_integrator.util.AdditionalSchemaGenerationSql;
import org.oscarehr.caisi_integrator.util.QueueCache;
import org.oscarehr.caisi_integrator.util.Role;
import org.springframework.stereotype.Repository;

@Repository
public class CachedProviderDao extends AbstractDao<CachedProvider>
{
	private static final QueueCache<FacilityIdStringCompositePk, HashSet<Role>> rolesCache = new QueueCache<FacilityIdStringCompositePk, HashSet<Role>>(4, 100, DateUtils.MILLIS_PER_HOUR);

	public CachedProviderDao()
	{
		super(CachedProvider.class);
	}

	public List<CachedProvider> findAll()
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x");

		@SuppressWarnings("unchecked")
		List<CachedProvider> results = query.getResultList();

		return(results);
	}

	public void setProviderRoles(FacilityIdStringCompositePk facilityIdStringCompositePk, Collection<Role> roles)
	{
		deleteProviderRoles(facilityIdStringCompositePk);
		addProviderRoles(facilityIdStringCompositePk, roles);
	}

	private void deleteProviderRoles(FacilityIdStringCompositePk facilityIdStringCompositePk)
	{
		Query query = entityManager.createNativeQuery("delete from CachedProviderRole where caisiItemId=?1 and integratorFacilityId=?2");
		query.setParameter(1, facilityIdStringCompositePk.getCaisiItemId());
		query.setParameter(2, facilityIdStringCompositePk.getIntegratorFacilityId());
		query.executeUpdate();

		rolesCache.remove(facilityIdStringCompositePk);
	}

	private void addProviderRoles(FacilityIdStringCompositePk facilityIdStringCompositePk, Collection<Role> roles)
	{
		if (roles == null) return;

		Query query = entityManager.createNativeQuery("insert into CachedProviderRole (caisiItemId,integratorFacilityId,providerRole) values (?1, ?2, ?3)");
		query.setParameter(1, facilityIdStringCompositePk.getCaisiItemId());
		query.setParameter(2, facilityIdStringCompositePk.getIntegratorFacilityId());

		for (Role providerRole : roles)
		{
			query.setParameter(3, providerRole.name());
			query.executeUpdate();
		}
	}

	public HashSet<Role> getProviderRoles(FacilityIdStringCompositePk facilityIdStringCompositePk)
	{
		HashSet<Role> results = rolesCache.get(facilityIdStringCompositePk);

		if (results == null)
		{
			Query query = entityManager.createNativeQuery("select providerRole from CachedProviderRole where caisiItemId=?1 and integratorFacilityId=?2", String.class);
			query.setParameter(1, facilityIdStringCompositePk.getCaisiItemId());
			query.setParameter(2, facilityIdStringCompositePk.getIntegratorFacilityId());

			@SuppressWarnings("unchecked")
			List<String> tempResult = query.getResultList();

			results = new HashSet<Role>();
			for (String tempString : tempResult)
			{
				results.add(Role.valueOf(tempString));
			}

			rolesCache.put(facilityIdStringCompositePk, results);
		}

		// put into new hashset to prevent changing the one in the cache
		return(new HashSet<Role>(results));
	}

	@AdditionalSchemaGenerationSql
	public String[] getAdditionalCustomSql()
	{
		String[] sql = { "create table CachedProviderRole (caisiItemId varchar(16) not null, integratorFacilityId int(11) not null, providerRole varchar(32) not null)" };

		return(sql);
	}
}
