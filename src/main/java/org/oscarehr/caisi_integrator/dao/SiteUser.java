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

import org.oscarehr.caisi_integrator.util.EncryptionUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.caisi_integrator.util.Named;

@Entity
public class SiteUser extends AbstractModel<Integer> implements Named
{
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
		this.password=EncryptionUtils.getSha1(password);
	}
	
	public boolean checkPassword(String password)
	{
		if (password==null) return(false);
		
		// backwards compatibility check for old passwords
		if (Arrays.equals(this.password, password.getBytes())) return(true);
		
		// new sha1 password check
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
