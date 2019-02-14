package org.oscarehr.caisi_integrator.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class ImportLogDao extends AbstractDao<ImportLog>
{

	public ImportLogDao()
	{
		super(ImportLog.class);
	}

	public List<ImportLog> findByFacility(Integer facilityId)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.facilityId = ?1");
		query.setParameter(1, facilityId);

		@SuppressWarnings("unchecked")
		List<ImportLog> results = query.getResultList();

		return(results);
	}

	public List<ImportLog> findByFacilitySince(Integer facilityId, Date startDate)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.facilityId = ?1 and x.dateIntervalStart >= ?2");
		query.setParameter(1, facilityId);
		query.setParameter(2, startDate);

		@SuppressWarnings("unchecked")
		List<ImportLog> results = query.getResultList();

		return(results);
	}

	public ImportLog findLastCompletedByFacility(Integer facilityId)
	{
		String sql = "select i from " + modelClass.getSimpleName() + " i WHERE i.facilityId = ?1 AND i.status = ?2 order by i.id DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, facilityId);
		query.setParameter(2, "COMPLETED");
		query.setMaxResults(1);

		return this.getSingleResultOrNull(query);
	}

	public List<ImportLog> findByFilenameAndChecksum(Integer facilityId, String filename, String checksum)
	{
		Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x where x.facilityId = ?1 and x.filename = ?2 and x.checksum = ?3 order by x.id");
		query.setParameter(1, facilityId);
		query.setParameter(2, filename);
		query.setParameter(3, checksum);

		@SuppressWarnings("unchecked")
		List<ImportLog> results = query.getResultList();

		return(results);
	}

}
