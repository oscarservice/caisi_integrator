package org.oscarehr.caisi_integrator.dao;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.DataCache;
import org.apache.openjpa.persistence.jdbc.Index;

/**
 * The usage of this class / dao is for creation only since this is a log. Once this row is created it should never be deleted or updated.
 */
@Entity
@DataCache(enabled = false)
public class EventLog extends AbstractModel<Long>
{
	public enum ActionPrefix
	{
		DATA, LOGIC, PERFORMANCE
	}

	public enum DataActionValue
	{
		READ, WRITE, DELETE, SEARCH_RESULT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Index
	private GregorianCalendar date = new GregorianCalendar();

	@Column(length = 255)
	@Index
	private String source = null;

	@Column(nullable = false, length = 255)
	@Index
	private String action = null;

	@Column(length = 255)
	private String parameters = null;

	@Override
	public Long getId()
	{
		return id;
	}

	public GregorianCalendar getDate()
	{
		return date;
	}

	public void setDate(GregorianCalendar date)
	{
		this.date = date;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getParameters()
	{
		return parameters;
	}

	public void setParameters(String parameters)
	{
		this.parameters = parameters;
	}

	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpaPreventUpdate()
	{
		throw(new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
}
