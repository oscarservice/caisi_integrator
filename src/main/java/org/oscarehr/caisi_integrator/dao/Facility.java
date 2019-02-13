package org.oscarehr.caisi_integrator.dao;

import java.util.Arrays;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.apache.ws.security.util.Base64;
import org.oscarehr.caisi_integrator.util.EncryptionUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.Named;

@Entity
public class Facility extends AbstractModel<Integer> implements Named
{
	private static Logger logger = MiscUtils.getLogger();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(nullable = false, length = 32, unique = true)
	private String name = null;

	@Column(nullable = false, columnDefinition = "tinyblob")
	private byte[] password = null;

	@Temporal(TemporalType.TIMESTAMP)
	private GregorianCalendar lastLogin = null;

	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean disabled = false;

	@Override
	public Integer getId()
	{
		return(id);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = MiscUtils.validateAndNormaliseUserName(name);
	}

	public void setPassword(String password)
	{
		if (password == null) throw(new IllegalArgumentException("password can't be null"));
		this.password = EncryptionUtils.getSha1(password);

		if (logger.isDebugEnabled())
		{
			logger.debug("setPassword provided pw : " + password);
			logger.debug("setPassword provided pw enc : " + Arrays.toString(EncryptionUtils.getSha1(password)));
		}
	}

	/**
	 * This method is required so calls that require get/set pairs works like JSF
	 * it should not attempt to return the password though so it'll just return null.
	 */
	public String getPassword()
	{
		return(null);
	}

	public String getPasswordAsBase64()
	{
		return Base64.encode(this.password);
	}

	public boolean checkPassword(String password)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("provided pw : " + password);
			logger.debug("db pw : " + Arrays.toString(this.password));
			logger.debug("provided pw enc : " + Arrays.toString(EncryptionUtils.getSha1(password)));
		}

		if (password == null) return(false);
		return(Arrays.equals(this.password, EncryptionUtils.getSha1(password)));
	}

	public GregorianCalendar getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(GregorianCalendar lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

}
