package org.oscarehr.caisi_integrator.dao;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class HomelessPopulationReport extends AbstractModel<Integer>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Index
	private GregorianCalendar reportTime = null;

	/**
	 * This is the number of clients who were admitted into a bed program and either haven't been discharged or was discharged with in the last year. 
	 */
	@Column(nullable = false)
	private int clientCountInPastYear;

	@Column(nullable = false)
	private int currentClientCount;

	@Column(nullable = false)
	private int usage1To10DaysInPast4Years;

	@Column(nullable = false)
	private int usage11To179DaysInPast4Years;

	@Column(nullable = false)
	private int usage180To730DaysInPast4Years;

	@Column(nullable = false)
	private int mortalityInPastYear;

	@Column(nullable = false)
	private int currentHiv;

	@Column(nullable = false)
	private int currentCancer;

	@Column(nullable = false)
	private int currentDiabetes;

	@Column(nullable = false)
	private int currentSeizure;

	@Column(nullable = false)
	private int currentLiverDisease;

	@Column(nullable = false)
	private int currentSchizophrenia;

	@Column(nullable = false)
	private int currentBipolar;

	@Column(nullable = false)
	private int currentDepression;

	@Column(nullable = false)
	private int currentCognitiveDisability;

	@Column(nullable = false)
	private int currentPneumonia;

	@Column(nullable = false)
	private int currentInfluenza;

	@Override
	public Integer getId()
	{
		return(id);
	}

	public GregorianCalendar getReportTime()
	{
		return reportTime;
	}

	public void setReportTime(GregorianCalendar reportTime)
	{
		this.reportTime = reportTime;
	}

	public int getClientCountInPastYear()
	{
		return clientCountInPastYear;
	}

	public void setClientCountInPastYear(int clientCountInPastYear)
	{
		this.clientCountInPastYear = clientCountInPastYear;
	}

	public int getCurrentClientCount()
	{
		return currentClientCount;
	}

	public void setCurrentClientCount(int currentClientCount)
	{
		this.currentClientCount = currentClientCount;
	}

	public int getUsage1To10DaysInPast4Years()
	{
		return usage1To10DaysInPast4Years;
	}

	public void setUsage1To10DaysInPast4Years(int usage1To10DaysInPast4Years)
	{
		this.usage1To10DaysInPast4Years = usage1To10DaysInPast4Years;
	}

	public int getUsage11To179DaysInPast4Years()
	{
		return usage11To179DaysInPast4Years;
	}

	public void setUsage11To179DaysInPast4Years(int usage11To179DaysInPast4Years)
	{
		this.usage11To179DaysInPast4Years = usage11To179DaysInPast4Years;
	}

	public int getUsage180To730DaysInPast4Years()
	{
		return usage180To730DaysInPast4Years;
	}

	public void setUsage180To730DaysInPast4Years(int usage180To730DaysInPast4Years)
	{
		this.usage180To730DaysInPast4Years = usage180To730DaysInPast4Years;
	}

	public int getMortalityInPastYear()
	{
		return mortalityInPastYear;
	}

	public void setMortalityInPastYear(int mortalityInPastYear)
	{
		this.mortalityInPastYear = mortalityInPastYear;
	}

	public int getCurrentHiv()
	{
		return currentHiv;
	}

	public void setCurrentHiv(int currentHiv)
	{
		this.currentHiv = currentHiv;
	}

	public int getCurrentCancer()
	{
		return currentCancer;
	}

	public void setCurrentCancer(int currentCancer)
	{
		this.currentCancer = currentCancer;
	}

	public int getCurrentDiabetes()
	{
		return currentDiabetes;
	}

	public void setCurrentDiabetes(int currentDiabetes)
	{
		this.currentDiabetes = currentDiabetes;
	}

	public int getCurrentSeizure()
	{
		return currentSeizure;
	}

	public void setCurrentSeizure(int currentSeizure)
	{
		this.currentSeizure = currentSeizure;
	}

	public int getCurrentLiverDisease()
	{
		return currentLiverDisease;
	}

	public void setCurrentLiverDisease(int currentLiverDisease)
	{
		this.currentLiverDisease = currentLiverDisease;
	}

	public int getCurrentSchizophrenia()
	{
		return currentSchizophrenia;
	}

	public void setCurrentSchizophrenia(int currentSchizophrenia)
	{
		this.currentSchizophrenia = currentSchizophrenia;
	}

	public int getCurrentBipolar()
	{
		return currentBipolar;
	}

	public void setCurrentBipolar(int currentBipolar)
	{
		this.currentBipolar = currentBipolar;
	}

	public int getCurrentDepression()
	{
		return currentDepression;
	}

	public void setCurrentDepression(int currentDepression)
	{
		this.currentDepression = currentDepression;
	}

	public int getCurrentCognitiveDisability()
	{
		return currentCognitiveDisability;
	}

	public void setCurrentCognitiveDisability(int currentCognitiveDisability)
	{
		this.currentCognitiveDisability = currentCognitiveDisability;
	}

	public int getCurrentPneumonia()
	{
		return currentPneumonia;
	}

	public void setCurrentPneumonia(int currentPneumonia)
	{
		this.currentPneumonia = currentPneumonia;
	}

	public int getCurrentInfluenza()
	{
		return currentInfluenza;
	}

	public void setCurrentInfluenza(int currentInfluenza)
	{
		this.currentInfluenza = currentInfluenza;
	}

}
